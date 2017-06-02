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
    // settings.classpath.value = s".:/home/mp/tmp/pcplod-usage/target/scala-2.12/classes:/home/mp/.coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-xml_2.12/1.0.6/scala-xml_2.12-1.0.6.jar:/home/mp/.coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.12.2/scala-reflect-2.12.2.jar:/home/mp/.coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-compiler/2.12.2/scala-compiler-2.12.2.jar:/home/mp/.coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.2/scala-library-2.12.2.jar"
    val iMain = new IMain(settings){
      override protected def parentClassLoader = settings.getClass.getClassLoader
    }
    val completer = new MyPresentationCompilerCompleter(iMain)

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
