package de.lhns.p2pfs

import cats.effect.{IO, Resource}
import org.cryptomator.frontend.fuse.mount.{EnvironmentVariables, FuseMountFactory, Mount, Mounter}

import java.nio.file.Path

object MounterUtils {
  lazy val mounter: Option[Mounter] = {
    Option.when(FuseMountFactory.isFuseSupported)(FuseMountFactory.getMounter)
  }

  object Syntax {
    implicit class MounterOps(val mounter: Mounter) extends AnyVal {
      def mountResource(path: Path, mountPoint: Path): Resource[IO, Mount] =
        Resource.make {
          IO.blocking {
            mounter.mount(
              path,
              EnvironmentVariables.create()
                .withMountPoint(mountPoint)
                .withFlags(mounter.defaultMountFlags())
                .withFileNameTranscoder(mounter.defaultFileNameTranscoder())
                .build()
            )
          }
        } { mount =>
          IO.blocking {
            mount.unmountForced()
            mount.close()
          }
        }
    }
  }
}
