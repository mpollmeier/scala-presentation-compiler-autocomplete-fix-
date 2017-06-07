object Test1 {
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

  def withParens[Out]()(implicit conv: Conv.Aux[Int, Out]): Out = "5".asInstanceOf[Out]
  def withoutParens[Out](implicit conv: Conv.Aux[Int, Out]): Out = "5".asInstanceOf[Out]
}

object Usage {
  // Test1.withParens().
  // Test1.withoutParens.form
}
