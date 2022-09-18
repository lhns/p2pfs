package de.lhns.p2pfs.store

import cats.Functor
import cats.syntax.functor._
import scodec.bits.ByteVector

trait PartStore[F[_]] {
  def partSize: Int

  def getPart(id: PartId): F[Part]

  def putPart(part: Part): F[Unit]

  def getTree(treeId: TreeId): F[Tree]

  def putTree(tree: Tree): F[Unit]
}

object PartStore {
  def fromKvStore[F[_] : Functor](kvStore: KvStore[F], partSize: Int): PartStore[F] = {
    val _partSize = partSize
    new PartStore[F] {
      override def partSize: Int = _partSize

      override def getPart(id: PartId): F[Part] =
        kvStore.getBytes(s"part/${id.id}").map(Part(_))

      override def putPart(part: Part): F[Unit] =
        kvStore.putBytes(s"part/${part.id.id}", part.bytes)

      override def getTree(treeId: TreeId): F[Tree] =
        kvStore.getString(s"tree/${treeId.id}").map(string => /*parse json*/ ???)

      override def putTree(tree: Tree): F[Unit] =
        kvStore.putString(tree.id.id, ???)
    }
  }
}

case class PartId(id: String)

case class Part(bytes: ByteVector) {
  lazy val id: PartId = PartId(bytes.sha256.toBase64UrlNoPad)
}

case class TreeId(id: String)

case class Tree(parts: Seq[PartId]) {
  lazy val id: TreeId = TreeId("") // TODO
}
