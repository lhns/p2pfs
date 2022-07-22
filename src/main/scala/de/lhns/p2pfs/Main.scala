package de.lhns.p2pfs

import cats.effect._
import de.lhns.p2pfs.MounterUtils.Syntax._
import jnr.ffi.Platform

import java.nio.file._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val mountPoint = Paths.get(Platform.getNativePlatform.getOS match {
      case Platform.OS.WINDOWS => "J:\\"
      case _ => "/tmp/mnt"
    })

    ZipFileSystemUtils.zipFileSystem[IO](
      Paths.get("zipfstest.zip"),
      create = true
    )
      .flatMap { fs =>
        val path = fs.getPath("/")
        //val path = Paths.get("mounted")

        MounterUtils.mounter.get.mountResource(
          path = path,
          mountPoint = mountPoint
        )
      }
      .use(_ => IO.readLine)
      .as(ExitCode.Success)
  }
}
