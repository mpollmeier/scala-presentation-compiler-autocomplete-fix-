package pc

import scala.tools.nsc.interactive.{ Global, Response }
import scala.reflect.internal.util.BatchSourceFile
import scala.tools.nsc._
import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.reporters.ConsoleReporter

class PresentationCompiler {
  // https://stackoverflow.com/questions/27470669/scala-reflect-internal-fatalerror-package-scala-does-not-have-a-member-int
  val settings = new Settings
  val sbtClasspath = System.getProperty("sbt-classpath")
  settings.classpath.value = s".:${sbtClasspath}"
  val in = new IMain(settings){
    override protected def parentClassLoader = settings.getClass.getClassLoader()
  }

  // we want the compiler output to be virtual
  val target = new VirtualDirectory("", None)
  settings.outputDirs.setSingleOutput(target)

  // can be replaced by a custom instance
  // of AbstractReporter to gain control.
  val reporter = new ConsoleReporter(settings)

  val compiler = new Global(settings, reporter)

  def compile(code: String) = {
    val source = new BatchSourceFile("<virtual>", code)
    val response = new Response[Unit]
    compiler.askReload(List(source), response)    
    response.get.left.foreach { x =>
      println(x)
      // success
    }
  }
}
