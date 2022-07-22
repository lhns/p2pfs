package de.lhns.p2pfs

import cats.effect.{Resource, Sync}

import java.net.URI
import java.nio.channels.FileChannel
import java.nio.file._
import java.nio.file.attribute.{BasicFileAttributes, FileAttribute}
import java.nio.file.spi.FileSystemProvider
import java.util

object ZipFileSystemUtils {
  private def zipFsCreateFileFix(fileSystem: FileSystem): FileSystem =
    FileSystemDelegate.mapPath(fileSystem) { path =>
      PathDelegate.delegateDeep(
        PathDelegate.undelegatePathArgs(path)
      )(new PathDelegate(_) {
        override def getFileSystem: FileSystem = new FileSystemDelegate(super.getFileSystem) {
          override def provider(): FileSystemProvider = new FileSystemProviderDelegate(
            FileSystemProviderDelegate.undelegatePathArgs(super.provider())
          ) {
            override def newFileChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): FileChannel = {
              val forWrite = options.contains(StandardOpenOption.WRITE) || options.contains(StandardOpenOption.APPEND)

              if (forWrite) {
                try super.readAttributes(path, classOf[BasicFileAttributes], LinkOption.NOFOLLOW_LINKS) != null
                catch {
                  case _: NoSuchFileException =>
                    super.newFileChannel(path, options, attrs: _*).close()
                    if (options.contains(StandardOpenOption.CREATE_NEW)) {
                      val newOptions = new util.HashSet[OpenOption](options)
                      newOptions.remove(StandardOpenOption.CREATE_NEW)
                      newOptions.add(StandardOpenOption.CREATE)
                      return super.newFileChannel(path, newOptions, attrs: _*)
                    }
                }
              }

              super.newFileChannel(path, options, attrs: _*)
            }
          }
        }
      })
    }

  def zipFileSystem[F[_] : Sync](zipPath: Path, create: Boolean): Resource[F, FileSystem] =
    Resource.fromAutoCloseable(Sync[F].blocking {
      val zipUri = URI.create("jar:" + zipPath.toUri.toString)
      zipFsCreateFileFix(FileSystems.newFileSystem(zipUri, {
        val env = new util.HashMap[String, String]()
        env.put("create", create.toString)
        env
      }))
    })
}
