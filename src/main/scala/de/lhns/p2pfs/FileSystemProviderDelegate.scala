package de.lhns.p2pfs

import de.lhns.p2pfs.PathDelegate.undelegate

import java.io.{InputStream, OutputStream}
import java.net.URI
import java.nio.channels.{AsynchronousFileChannel, FileChannel, SeekableByteChannel}
import java.nio.file._
import java.nio.file.attribute.{BasicFileAttributes, FileAttribute, FileAttributeView}
import java.nio.file.spi.FileSystemProvider
import java.util
import java.util.concurrent.ExecutorService

case class FileSystemProviderDelegate(fileSystemProvider: FileSystemProvider) extends FileSystemProvider {
  override def getScheme: String =
    fileSystemProvider.getScheme

  override def newFileSystem(uri: URI, env: util.Map[String, _]): FileSystem =
    fileSystemProvider.newFileSystem(uri, env)

  override def newFileSystem(path: Path, env: util.Map[String, _]): FileSystem =
    fileSystemProvider.newFileSystem(path, env)

  override def getFileSystem(uri: URI): FileSystem =
    fileSystemProvider.getFileSystem(uri)

  override def getPath(uri: URI): Path =
    fileSystemProvider.getPath(uri)

  override def newInputStream(path: Path, options: OpenOption*): InputStream =
    fileSystemProvider.newInputStream(path, options: _*)

  override def newOutputStream(path: Path, options: OpenOption*): OutputStream =
    fileSystemProvider.newOutputStream(path, options: _*)

  override def newFileChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): FileChannel =
    fileSystemProvider.newFileChannel(path, options, attrs: _*)

  override def newAsynchronousFileChannel(path: Path, options: util.Set[_ <: OpenOption], executor: ExecutorService, attrs: FileAttribute[_]*): AsynchronousFileChannel =
    fileSystemProvider.newAsynchronousFileChannel(path, options, executor, attrs: _*)

  override def newByteChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): SeekableByteChannel =
    fileSystemProvider.newByteChannel(path, options, attrs: _*)

  override def newDirectoryStream(dir: Path, filter: DirectoryStream.Filter[_ >: Path]): DirectoryStream[Path] =
    fileSystemProvider.newDirectoryStream(dir, filter)

  override def createDirectory(dir: Path, attrs: FileAttribute[_]*): Unit =
    fileSystemProvider.createDirectory(dir, attrs: _*)

  override def createSymbolicLink(link: Path, target: Path, attrs: FileAttribute[_]*): Unit =
    fileSystemProvider.createSymbolicLink(link, target, attrs: _*)

  override def createLink(link: Path, existing: Path): Unit =
    fileSystemProvider.createLink(link, existing)

  override def delete(path: Path): Unit =
    fileSystemProvider.delete(path)

  override def deleteIfExists(path: Path): Boolean =
    fileSystemProvider.deleteIfExists(path)

  override def readSymbolicLink(link: Path): Path =
    fileSystemProvider.readSymbolicLink(link)

  override def copy(source: Path, target: Path, options: CopyOption*): Unit =
    fileSystemProvider.copy(source, target, options: _*)

  override def move(source: Path, target: Path, options: CopyOption*): Unit =
    fileSystemProvider.move(source, target, options: _*)

  override def isSameFile(path: Path, path2: Path): Boolean =
    fileSystemProvider.isSameFile(path, path2)

  override def isHidden(path: Path): Boolean =
    fileSystemProvider.isHidden(path)

  override def getFileStore(path: Path): FileStore =
    fileSystemProvider.getFileStore(path)

  override def checkAccess(path: Path, modes: AccessMode*): Unit =
    fileSystemProvider.checkAccess(path, modes: _*)

  override def getFileAttributeView[V <: FileAttributeView](path: Path, `type`: Class[V], options: LinkOption*): V =
    fileSystemProvider.getFileAttributeView(path, `type`, options: _*)

  override def readAttributes[A <: BasicFileAttributes](path: Path, `type`: Class[A], options: LinkOption*): A =
    fileSystemProvider.readAttributes(path, `type`, options: _*)

  override def readAttributes(path: Path, attributes: String, options: LinkOption*): util.Map[String, AnyRef] =
    fileSystemProvider.readAttributes(path, attributes, options: _*)

  override def setAttribute(path: Path, attribute: String, value: Any, options: LinkOption*): Unit =
    fileSystemProvider.setAttribute(path, attribute, value, options: _*)
}

object FileSystemProviderDelegate {
  def undelegatePathArgs(fileSystemProvider: FileSystemProvider): FileSystemProviderDelegate =
    new FileSystemProviderDelegate(fileSystemProvider) {
      override def newFileSystem(path: Path, env: util.Map[String, _]): FileSystem =
        fileSystemProvider.newFileSystem(undelegate(path), env)

      override def newInputStream(path: Path, options: OpenOption*): InputStream =
        fileSystemProvider.newInputStream(undelegate(path), options: _*)

      override def newOutputStream(path: Path, options: OpenOption*): OutputStream =
        fileSystemProvider.newOutputStream(undelegate(path), options: _*)

      override def newFileChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): FileChannel =
        fileSystemProvider.newFileChannel(undelegate(path), options, attrs: _*)

      override def newAsynchronousFileChannel(path: Path, options: util.Set[_ <: OpenOption], executor: ExecutorService, attrs: FileAttribute[_]*): AsynchronousFileChannel =
        fileSystemProvider.newAsynchronousFileChannel(undelegate(path), options, executor, attrs: _*)

      override def newByteChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): SeekableByteChannel =
        fileSystemProvider.newByteChannel(undelegate(path), options, attrs: _*)

      override def newDirectoryStream(dir: Path, filter: DirectoryStream.Filter[_ >: Path]): DirectoryStream[Path] =
        fileSystemProvider.newDirectoryStream(undelegate(dir), filter)

      override def createDirectory(dir: Path, attrs: FileAttribute[_]*): Unit =
        fileSystemProvider.createDirectory(undelegate(dir), attrs: _*)

      override def createSymbolicLink(link: Path, target: Path, attrs: FileAttribute[_]*): Unit =
        fileSystemProvider.createSymbolicLink(undelegate(link), undelegate(target), attrs: _*)

      override def createLink(link: Path, existing: Path): Unit =
        fileSystemProvider.createLink(undelegate(link), undelegate(existing))

      override def delete(path: Path): Unit =
        fileSystemProvider.delete(undelegate(path))

      override def deleteIfExists(path: Path): Boolean =
        fileSystemProvider.deleteIfExists(undelegate(path))

      override def readSymbolicLink(link: Path): Path =
        fileSystemProvider.readSymbolicLink(undelegate(link))

      override def copy(source: Path, target: Path, options: CopyOption*): Unit =
        fileSystemProvider.copy(undelegate(source), undelegate(target), options: _*)

      override def move(source: Path, target: Path, options: CopyOption*): Unit =
        fileSystemProvider.move(undelegate(source), undelegate(target), options: _*)

      override def isSameFile(path: Path, path2: Path): Boolean =
        fileSystemProvider.isSameFile(undelegate(path), undelegate(path2))

      override def isHidden(path: Path): Boolean =
        fileSystemProvider.isHidden(undelegate(path))

      override def getFileStore(path: Path): FileStore =
        fileSystemProvider.getFileStore(undelegate(path))

      override def checkAccess(path: Path, modes: AccessMode*): Unit =
        fileSystemProvider.checkAccess(undelegate(path), modes: _*)

      override def getFileAttributeView[V <: FileAttributeView](path: Path, `type`: Class[V], options: LinkOption*): V =
        fileSystemProvider.getFileAttributeView(undelegate(path), `type`, options: _*)

      override def readAttributes[A <: BasicFileAttributes](path: Path, `type`: Class[A], options: LinkOption*): A =
        fileSystemProvider.readAttributes(undelegate(path), `type`, options: _*)

      override def readAttributes(path: Path, attributes: String, options: LinkOption*): util.Map[String, AnyRef] =
        fileSystemProvider.readAttributes(undelegate(path), attributes, options: _*)

      override def setAttribute(path: Path, attribute: String, value: Any, options: LinkOption*): Unit =
        fileSystemProvider.setAttribute(undelegate(path), attribute, value, options: _*)
    }
}
