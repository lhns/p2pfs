package de.lhns.p2pfs

import java.nio.file._
import java.nio.file.attribute.UserPrincipalLookupService
import java.nio.file.spi.FileSystemProvider
import java.{lang, util}

case class FileSystemDelegate(fileSystem: FileSystem) extends FileSystem {
  override def provider(): FileSystemProvider =
    fileSystem.provider()

  override def close(): Unit =
    fileSystem.close()

  override def isOpen: Boolean =
    fileSystem.isOpen

  override def isReadOnly: Boolean =
    fileSystem.isReadOnly

  override def getSeparator: String =
    fileSystem.getSeparator

  override def getRootDirectories: lang.Iterable[Path] =
    fileSystem.getRootDirectories

  override def getFileStores: lang.Iterable[FileStore] =
    fileSystem.getFileStores

  override def supportedFileAttributeViews(): util.Set[String] =
    fileSystem.supportedFileAttributeViews()

  override def getPath(first: String, more: String*): Path =
    fileSystem.getPath(first, more: _*)

  override def getPathMatcher(syntaxAndPattern: String): PathMatcher =
    fileSystem.getPathMatcher(syntaxAndPattern)

  override def getUserPrincipalLookupService: UserPrincipalLookupService =
    fileSystem.getUserPrincipalLookupService

  override def newWatchService(): WatchService =
    fileSystem.newWatchService()
}

object FileSystemDelegate {
  def mapPath(fileSystem: FileSystem)(f: Path => PathDelegate): FileSystemDelegate =
    new FileSystemDelegate(fileSystem) {
      override def getRootDirectories: lang.Iterable[Path] = {
        val iterable = super.getRootDirectories

        new lang.Iterable[Path] {
          override def iterator(): util.Iterator[Path] = {
            val iterator = iterable.iterator()

            new util.Iterator[Path] {
              override def hasNext: Boolean = iterator.hasNext

              override def next(): Path = f(iterator.next())
            }
          }
        }
      }

      override def getPath(first: String, more: String*): Path =
        f(super.getPath(first, more: _*))
    }
}
