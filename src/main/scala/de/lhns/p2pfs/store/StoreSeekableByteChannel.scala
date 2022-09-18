package de.lhns.p2pfs.store

import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

class StoreSeekableByteChannel[F[_]](store: PartStore[F], tree: Tree) extends SeekableByteChannel {
  override def read(dst: ByteBuffer): Int = {
    store.getPart()
  }

  override def write(src: ByteBuffer): Int = ???

  override def position(): Long = ???

  override def position(newPosition: Long): SeekableByteChannel = ???

  override def size(): Long = ???

  override def truncate(size: Long): SeekableByteChannel = ???

  override def isOpen: Boolean = true

  override def close(): Unit = ()
}
