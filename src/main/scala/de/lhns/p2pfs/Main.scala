package de.lhns.p2pfs

import cats.effect._
import cats.syntax.functor._
import jnr.ffi.Platform
import org.cryptomator.frontend.fuse.mount.{EnvironmentVariables, FuseMountFactory, Mount, Mounter}
import ru.serce.jnrfuse.Mountable

import java.net.URI
import java.nio.channels.FileChannel
import java.nio.file._
import java.nio.file.attribute.{BasicFileAttributes, FileAttribute}
import java.nio.file.spi.FileSystemProvider
import java.util

object Main extends IOApp {
  def fsResource[F[_] : Async](
                                mountable: Mountable,
                                mountPoint: Path,
                                debug: Boolean = false,
                                fuseOpts: Array[String] = Array.empty
                              ): Resource[F, Unit] =
    Resource.make {
      Async[F].blocking(mountable.mount(mountPoint, false, debug, fuseOpts))
    } { _ =>
      Async[F].blocking(mountable.umount())
    }.void

  lazy val mounter: Option[Mounter] = {
    Option.when(FuseMountFactory.isFuseSupported)(FuseMountFactory.getMounter)
  }

  implicit class MounterOps(val mounter: Mounter) extends AnyVal {
    def mountResource(path: Path, mountPoint: Path): Resource[IO, Mount] =
      Resource.make {
        IO.blocking {
          mounter.mount(
            path,
            EnvironmentVariables.create()
              .withMountPoint(mountPoint)
              .withFlags(mounter.defaultMountFlags())
              .withFileNameTranscoder(mounter.defaultFileNameTranscoder())
              .build()
          )
        }
      } { mount =>
        IO.blocking {
          mount.unmountForced()
          mount.close()
        }
      }
  }

  /*
  val test = {
    new StrictProtocolBinding[]() {}

    val node = new HostBuilder()
      .protocol(new Ping())
      .listen("/ip4/127.0.0.1/tcp/0")
      .build()

    Resource.make {
      IO.fromCompletableFuture(IO(node.start())).void
    } { _ =>
      IO.fromCompletableFuture(IO(node.stop())).void
    }
  }
   */

  def zipFsCreateFileFix(path: Path): Path =
    PathDelegate.delegateResults(
      PathDelegate.undelegateArgs(path)
    )(new PathDelegate(_) {
      override def getFileSystem: FileSystem = new FileSystemDelegate(super.getFileSystem) {
        override def provider(): FileSystemProvider = new FileSystemProviderDelegate(
          FileSystemProviderDelegate.undelegateArgs(super.provider())
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

  override def run(args: List[String]): IO[ExitCode] = {
    val mountPoint = Paths.get(Platform.getNativePlatform.getOS match {
      case Platform.OS.WINDOWS => "J:\\"
      case _ => "/tmp/mnt"
    })

    val jarUri = URI.create("jar:" + Paths.get("zipfstest.zip").toUri.toString)
    val fs = FileSystems.newFileSystem(jarUri, {
      val env = new util.HashMap[String, String]()
      env.put("create", "true")
      env
    })

    val path = zipFsCreateFileFix(fs.getPath("/"))
    //val path = Paths.get("mounted")

    mounter.get.mountResource(
      path = path,
      mountPoint = mountPoint
    ).use(_ =>
      IO.readLine >>
        IO.blocking(fs.close())
    )
      .as(ExitCode.Success)
  }
}
