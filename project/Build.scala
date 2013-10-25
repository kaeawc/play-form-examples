import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-nested-forms"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    jdbc,
    anorm,
    cache,
    "org.bouncycastle" % "bcprov-jdk15on" % "1.49"
  )
  
  val main = play.Project(
    appName,
    appVersion,
    appDependencies
  ).settings(
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-unchecked",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions"
    )
  )
}
