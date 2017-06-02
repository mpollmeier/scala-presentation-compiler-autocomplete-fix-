package pc

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.Results.Success
import scala.tools.nsc.interpreter.{ IMain, PresentationCompilerCompleter }
import org.scalatest._

class PresentationCompilerTest extends WordSpec with Matchers {

  "completions" in {
    val settings = new Settings
    val sbtClasspath = System.getProperty("sbt-classpath")
    settings.classpath.value = s".:${sbtClasspath}"
    val iMain = new IMain(settings){
      override protected def parentClassLoader = settings.getClass.getClassLoader
    }
    val completer = new PresentationCompilerCompleter(iMain)

    val ret = iMain.interpret("val x = 55")
    // val ret = iMain.interpret("val x = \"55\"")
    ret shouldBe Success

    val candidates = completer.complete(before = "x.", after = "")
    println(candidates)
  }

}
