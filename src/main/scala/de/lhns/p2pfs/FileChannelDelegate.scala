package de.lhns.p2pfs

import java.nio.channels.{FileChannel, FileLock, ReadableByteChannel, WritableByteChannel}
import java.nio.{ByteBuffer, MappedByteBuffer}

case class FileChannelDelegate(fileChannel: FileChannel) extends FileChannel {
  override def read(dst: ByteBuffer): Int =
    fileChannel.read(dst)

  override def read(dsts: Array[ByteBuffer], offset: Int, length: Int): Long =
    fileChannel.read(dsts, offset, length)

  override def write(src: ByteBuffer): Int =
    fileChannel.write(src)

  override def write(srcs: Array[ByteBuffer], offset: Int, length: Int): Long =
    fileChannel.write(srcs, offset, length)

  override def position(): Long =
    fileChannel.position()

  override def position(newPosition: Long): FileChannel =
    fileChannel.position(newPosition)

  override def size(): Long =
    fileChannel.size()

  override def truncate(size: Long): FileChannel =
    fileChannel.truncate(size)

  override def force(metaData: Boolean): Unit =
    fileChannel.force(metaData)

  override def transferTo(position: Long, count: Long, target: WritableByteChannel): Long =
    fileChannel.transferTo(position, count, target)

  override def transferFrom(src: ReadableByteChannel, position: Long, count: Long): Long =
    fileChannel.transferFrom(src, position, count)

  override def read(dst: ByteBuffer, position: Long): Int =
    fileChannel.read(dst, position)

  override def write(src: ByteBuffer, position: Long): Int =
    fileChannel.write(src, position)

  override def map(mode: FileChannel.MapMode, position: Long, size: Long): MappedByteBuffer =
    fileChannel.map(mode, position, size)

  override def lock(position: Long, size: Long, shared: Boolean): FileLock =
    fileChannel.lock(position, size, shared)

  override def tryLock(position: Long, size: Long, shared: Boolean): FileLock =
    fileChannel.tryLock()

  override def implCloseChannel(): Unit =
    fileChannel.close()
}
