package de.lhns.p2pfs.store

import scodec.bits.ByteVector

trait KvStore[F[_]] {
  def getString(key: String): F[String]

  def getBytes(key: String): F[ByteVector]

  def putString(key: String, value: String): F[Unit]

  def putBytes(key: String, value: ByteVector): F[Unit]
}
