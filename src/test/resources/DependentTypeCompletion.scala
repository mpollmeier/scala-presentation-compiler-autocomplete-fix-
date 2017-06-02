package test

// tests for autocomplete on repl

object Test {
  trait Conv[In] {
    type Out
    def apply(in: In): Out
  }
  object Conv {
    type Aux[In, Out0] = Conv[In] { type Out = Out0 }
    implicit val int2String = new Conv[Int] {
      type Out = String
      override def apply(i: Int) = i.toString
    }
  }

  // autocomplete works on repl: `test.Test.withParens().<TAB>` shows completions for String
  def withParens[Out]()(implicit conv: Conv.Aux[Int, Out]): Out = "5".asInstanceOf[Out]

  // autocomplete doesn't work on repl: `test.Test.withoutParens.` doesn't suggest anything
  // when saving intermediate result it works though: `val a = test.Test.withoutParens; a.<TAB>`
  def withoutParens[Out](implicit conv: Conv.Aux[Int, Out]): Out = "5".asInstanceOf[Out]
}

// this works fine
object Test2 {
  trait A
  implicit val a: A = ???
  def withParens()(implicit a: A): String = "something"
  def withoutParens(implicit a: A): String = "something"
}


object Usage {
  // val a: Int = "asd"
  val a = "asd"
}
