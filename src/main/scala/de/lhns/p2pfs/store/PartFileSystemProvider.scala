package de.lhns.p2pfs.store

import java.net.URI
import java.nio.channels.SeekableByteChannel
import java.nio.file.attribute.{BasicFileAttributes, FileAttribute, FileAttributeView}
import java.nio.file.spi.FileSystemProvider
import java.nio.file._
import java.util

class PartFileSystemProvider extends FileSystemProvider {
  override def getScheme: String = ???

  override def newFileSystem(uri: URI, env: util.Map[String, _]): FileSystem = ???

  override def getFileSystem(uri: URI): FileSystem = ???

  override def getPath(uri: URI): Path = ???

  override def newByteChannel(path: Path, options: util.Set[_ <: OpenOption], attrs: FileAttribute[_]*): SeekableByteChannel = ???

  override def newDirectoryStream(dir: Path, filter: DirectoryStream.Filter[_ >: Path]): DirectoryStream[Path] = ???

  override def createDirectory(dir: Path, attrs: FileAttribute[_]*): Unit = ???

  override def delete(path: Path): Unit = ???

  override def copy(source: Path, target: Path, options: CopyOption*): Unit = ???

  override def move(source: Path, target: Path, options: CopyOption*): Unit = ???

  override def isSameFile(path: Path, path2: Path): Boolean = ???

  override def isHidden(path: Path): Boolean = ???

  override def getFileStore(path: Path): FileStore = ???

  override def checkAccess(path: Path, modes: AccessMode*): Unit = ???

  override def getFileAttributeView[V <: FileAttributeView](path: Path, `type`: Class[V], options: LinkOption*): V = ???

  override def readAttributes[A <: BasicFileAttributes](path: Path, `type`: Class[A], options: LinkOption*): A = ???

  override def readAttributes(path: Path, attributes: String, options: LinkOption*): util.Map[String, AnyRef] = ???

  override def setAttribute(path: Path, attribute: String, value: Any, options: LinkOption*): Unit = ???
}
