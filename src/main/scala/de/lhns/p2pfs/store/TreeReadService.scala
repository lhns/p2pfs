package de.lhns.p2pfs.store

import cats.effect.kernel.Concurrent
import fs2.{Chunk, Stream}

import scala.util.chaining._

class TreeReadService[F[_] : Concurrent](store: PartStore[F]) {
  def read(tree: Tree, offset: Long = 0, lengthOption: Option[Long] = None): Stream[F, Byte] = {
    Stream.emits(tree.parts)
      .drop(offset / store.partSize)
      .pipe(e => lengthOption.fold(e)(length => e.take((length + (store.partSize - 1)) / store.partSize)))
      .covary[F]
      .parEvalMap(16)(store.getPart)
      .map(part => Chunk.byteVector(part.bytes))
      .unchunks
      .drop(offset % store.partSize)
      .pipe(e => lengthOption.fold(e)(e.take))
  }
}
