package de.lhns.p2pfs

import java.nio.file.FileStore
import java.nio.file.attribute.{FileAttributeView, FileStoreAttributeView}

case class FileStoreDelegate(fileStore: FileStore) extends FileStore {
  override def name(): String =
    fileStore.name()

  override def `type`(): String =
    fileStore.`type`()

  override def isReadOnly: Boolean =
    fileStore.isReadOnly

  override def getTotalSpace: Long =
    fileStore.getTotalSpace

  override def getUsableSpace: Long =
    fileStore.getUsableSpace

  override def getUnallocatedSpace: Long =
    fileStore.getUnallocatedSpace

  override def getBlockSize: Long =
    fileStore.getBlockSize

  override def supportsFileAttributeView(`type`: Class[_ <: FileAttributeView]): Boolean =
    fileStore.supportsFileAttributeView(`type`)

  override def supportsFileAttributeView(name: String): Boolean =
    fileStore.supportsFileAttributeView(name)

  override def getFileStoreAttributeView[V <: FileStoreAttributeView](`type`: Class[V]): V =
    fileStore.getFileStoreAttributeView(`type`)

  override def getAttribute(attribute: String): AnyRef =
    fileStore.getAttribute(attribute)
}