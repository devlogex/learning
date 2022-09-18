trait A {
  val x=1
}

trait B {
  val x=2

}
AnyRef
class C extends A with B{
  def out = {

    print(x)
  }
}

object Main {
  def main(args: Array[String]) = {
    (new C).out
  }
}
