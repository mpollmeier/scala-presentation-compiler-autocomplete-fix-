package pc

import scala.io.Source
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter._
import org.scalatest._

class PresentationCompilerTest extends WordSpec with Matchers {

  "completions" in {
    val settings = new Settings
    val sbtClasspath = System.getProperty("sbt-classpath")
    settings.classpath.value = s".:${sbtClasspath}"
    val iMain = new IMain(settings){
      override protected def parentClassLoader = settings.getClass.getClassLoader
    }
    // val completer = new PresentationCompilerCompleter(iMain) // standard behaviour
    val completer = new MyPresentationCompilerCompleter(iMain) //fixed autocompletion

    val source = Source.fromFile("src/test/resources/DependentTypeCompletion.scala").mkString
    val ret = iMain.compileString(source)
    ret shouldBe true

    // val candidates = completer.complete(before = "test.Test2.withParens().", after = "").candidates //works
    // val candidates = completer.complete(before = "test.Test2.withoutParens.", after = "").candidates //works
    // val candidates = completer.complete(before = "test.Test1.withParens().", after = "").candidates //works
    val candidates = completer.complete(before = "test.Test1.withoutParens.", after = "").candidates //didn't work before, now works with my custom completer
    candidates should contain ("toLowerCase")

    // println("returned candidates: " + candidates)
    // scala.reflect.io.File("candidates-withParens.txt").writeAll(candidates.mkString("\n"))
    // scala.reflect.io.File("candidates-withoutParens.txt").writeAll(candidates.mkString("\n"))
  }

}
