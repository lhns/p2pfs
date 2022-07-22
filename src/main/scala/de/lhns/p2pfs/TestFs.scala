package de.lhns.p2pfs

import jnr.ffi.{Pointer, Struct}
import ru.serce.jnrfuse.struct.{FileStat, FuseFileInfo}
import ru.serce.jnrfuse.{ErrorCodes, FuseException, FuseFillDir, FuseStubFS}

import java.nio.file.Path

class TestFs extends FuseStubFS {
  val helloPath = "/hello"
  val helloText = "Hello World!"

  override def getattr(path: String, stat: FileStat): Int = {
    if (path == "/") {
      stat.st_mode.set(FileStat.S_IFDIR | BigInt("755", 8).toInt)
      stat.st_nlink.set(2)
      0
    } else if (path == helloPath) {
      stat.st_mode.set(FileStat.S_IFREG | BigInt("444", 8).toInt)
      stat.st_nlink.set(1)
      stat.st_size.set(helloText.getBytes.length)
      0
    } else {
      -ErrorCodes.ENOENT
    }
  }

  override def readdir(path: String, buf: Pointer, filter: FuseFillDir, offset: Long, fi: FuseFileInfo): Int = {
    if (path == "/") {
      filter.apply(buf, ".", null, 0)
      filter.apply(buf, "..", null, 0)
      filter.apply(buf, helloPath.substring(1), null, 0)
      0
    } else {
      -ErrorCodes.ENOENT
    }
  }

  override def open(path: String, fi: FuseFileInfo): Int = {
    if (path == helloPath) {
      0
    } else {
      -ErrorCodes.ENOENT
    }
  }

  override def read(path: String, buf: Pointer, size: Long, offset: Long, fi: FuseFileInfo): Int = {
    if (path == helloPath) {

      val bytes = helloText.getBytes
      val length = bytes.length
      var mutSize = size
      if (offset < length) {
        if (offset + size > length) {
          mutSize = length - offset
        }
        buf.put(0, bytes, 0, bytes.length)
      } else {
        mutSize = 0
      }
      mutSize.toInt
    } else {
      -ErrorCodes.ENOENT
    }
  }
}
