name := "presentation-compiler-usage"
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)

fork in Test := false

val sbtcp = taskKey[Unit]("sbt-classpath")

// share sbt classpath with presentation compiler
// https://stackoverflow.com/questions/27470669/scala-reflect-internal-fatalerror-package-scala-does-not-have-a-member-int
sbtcp := {
  val files: Seq[File] = (fullClasspath in Compile).value.files
  val sbtClasspath : String = files.map(x => x.getAbsolutePath).mkString(":")
  println("Set SBT classpath to 'sbt-classpath' environment variable")
  System.setProperty("sbt-classpath", sbtClasspath)
  println("value: " + System.getProperty("sbt-classpath"))
}

(test in Test) := (test in Test).dependsOn(sbtcp).value
