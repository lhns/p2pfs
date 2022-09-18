package de.lhns.p2pfs.store

import java.nio.file.attribute.UserPrincipalLookupService
import java.nio.file.spi.FileSystemProvider
import java.nio.file._
import java.{lang, util}

class PartFileSystem extends FileSystem {
  override def provider(): FileSystemProvider = ???

  override def close(): Unit = ???

  override def isOpen: Boolean = ???

  override def isReadOnly: Boolean = ???

  override def getSeparator: String = ???

  override def getRootDirectories: lang.Iterable[Path] = ???

  override def getFileStores: lang.Iterable[FileStore] = ???

  override def supportedFileAttributeViews(): util.Set[String] = ???

  override def getPath(first: String, more: String*): Path = ???

  override def getPathMatcher(syntaxAndPattern: String): PathMatcher = ???

  override def getUserPrincipalLookupService: UserPrincipalLookupService = ???

  override def newWatchService(): WatchService = ???
}
