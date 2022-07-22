package de.lhns.p2pfs

import java.io.File
import java.net.URI
import java.nio.file._
import java.util
import scala.annotation.tailrec

case class PathDelegate(path: Path) extends Path {
  override def getFileSystem: FileSystem =
    path.getFileSystem

  override def isAbsolute: Boolean =
    path.isAbsolute

  override def getRoot: Path =
    path.getRoot

  override def getFileName: Path =
    path.getFileName

  override def getParent: Path =
    path.getParent

  override def getNameCount: Int =
    path.getNameCount

  override def getName(index: Int): Path =
    path.getName(index)

  override def subpath(beginIndex: Int, endIndex: Int): Path =
    path.subpath(beginIndex, endIndex)

  override def startsWith(other: Path): Boolean =
    path.startsWith(other)

  override def startsWith(other: String): Boolean =
    path.startsWith(other)

  override def endsWith(other: Path): Boolean =
    path.endsWith(other)

  override def endsWith(other: String): Boolean =
    path.endsWith(other)

  override def normalize(): Path =
    path.normalize()

  override def resolve(other: Path): Path =
    path.resolve(other)

  override def resolve(other: String): Path =
    path.resolve(other)

  override def resolveSibling(other: Path): Path =
    path.resolveSibling(other)

  override def resolveSibling(other: String): Path =
    path.resolveSibling(other)

  override def relativize(other: Path): Path =
    path.relativize(other)

  override def toUri: URI =
    path.toUri

  override def toAbsolutePath: Path =
    path.toAbsolutePath

  override def toRealPath(options: LinkOption*): Path =
    path.toRealPath(options: _*)

  override def toFile: File =
    path.toFile

  override def register(watcher: WatchService, events: Array[WatchEvent.Kind[_]], modifiers: WatchEvent.Modifier*): WatchKey =
    path.register(watcher, events, modifiers: _*)

  override def register(watcher: WatchService, events: WatchEvent.Kind[_]*): WatchKey =
    path.register(watcher, events: _*)

  override def iterator(): util.Iterator[Path] =
    path.iterator()

  override def compareTo(other: Path): Int =
    path.compareTo(other)
}

object PathDelegate {
  @tailrec
  def undelegate(path: Path): Path =
    path match {
      case PathDelegate(path) => undelegate(path)
      case path => path
    }

  def undelegatePathArgs(path: Path): PathDelegate =
    new PathDelegate(path) {
      override def startsWith(other: Path): Boolean =
        path.startsWith(undelegate(other))

      override def endsWith(other: Path): Boolean =
        path.endsWith(undelegate(other))

      override def resolve(other: Path): Path =
        super.resolve(undelegate(other))

      override def resolveSibling(other: Path): Path =
        super.resolveSibling(undelegate(other))

      override def relativize(other: Path): Path =
        super.relativize(undelegate(other))

      override def compareTo(other: Path): Int =
        undelegate(path).compareTo(undelegate(other))
    }

  def delegateDeep(path: Path)(delegate: Path => PathDelegate): PathDelegate =
    new PathDelegate(delegate(path)) {
      override def getRoot: Path =
        delegate(super.getRoot)

      override def getFileName: Path =
        delegate(super.getFileName)

      override def getParent: Path =
        delegate(super.getParent)

      override def getName(index: Int): Path =
        delegate(super.getName(index))

      override def subpath(beginIndex: Int, endIndex: Int): Path =
        delegate(super.subpath(beginIndex, endIndex))

      override def normalize(): Path =
        delegate(super.normalize())

      override def resolve(other: Path): Path =
        delegate(super.resolve(other))

      override def resolve(other: String): Path =
        delegate(super.resolve(other))

      override def resolveSibling(other: Path): Path =
        delegate(super.resolveSibling(other))

      override def resolveSibling(other: String): Path =
        delegate(super.resolveSibling(other))

      override def relativize(other: Path): Path =
        delegate(super.relativize(other))

      override def toAbsolutePath: Path =
        delegate(super.toAbsolutePath)

      override def toRealPath(options: LinkOption*): Path =
        delegate(super.toRealPath(options: _*))

      override def iterator(): util.Iterator[Path] = {
        val iterator = super.iterator()

        new util.Iterator[Path] {
          override def hasNext: Boolean = iterator.hasNext

          override def next(): Path = delegate(iterator.next())
        }
      }
    }
}
