ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val V = new {
  val logbackClassic = "1.2.11"
}

lazy val root = (project in file("."))
  .settings(
    name := "p2pfs",

    resolvers ++= Seq(
      "libp2p" at "https://dl.cloudsmith.io/public/libp2p/jvm-libp2p/maven/",
      "consensys" at "https://artifacts.consensys.net/public/maven/maven/"
    ),

    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % V.logbackClassic,
      "co.fs2" %% "fs2-io" % "3.2.10",
      "com.github.serceman" % "jnr-fuse" % "0.5.7",
      "io.libp2p" % "jvm-libp2p-minimal" % "0.9.0-RELEASE",
      "org.cryptomator" % "fuse-nio-adapter" % "1.3.4",
      "org.typelevel" %% "cats-effect" % "3.3.12",
    )
  )
