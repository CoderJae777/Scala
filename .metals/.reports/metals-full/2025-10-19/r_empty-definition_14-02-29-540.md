error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/functor.scala:scala/package.List#
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/functor.scala
empty definition using pc, found symbol in pc: scala/package.List#
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -List#
	 -scala/Predef.List#
offset: 637
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/functor.scala
text:
```scala
import scala.util.{Try, Success}

object Functor extends App {

  val onIncrementedList = List(1, 2, 3).map(x => x + 1)
  println(onIncrementedList)

  val anOption: Option[Int] = Some(2)
  println(anOption)

  val aTry: Try[Int] = Success(42)
  println(aTry)

  val aTransformedOption = anOption.map(x => x + 3)
  println("This is aTransformedOption: " + aTransformedOption)

  val aTransformedTry = aTry.map(x => x + 4)
  println("This is aTransformedTry: " + aTransformedTry)

  // Functors

  // Problem : we need to do the same transformation on different container types

  def do10xList(list: List[Int]): Li@@st[Int] = list.map(x => x * 10)
  def do10xListOption(opt: Option[Int]): Option[Int] = opt.map(x => x * 10)
  def do10xListTry(t: Try[Int]): Try[Int] = t.map(x => x * 10)

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  given listFunctor: Functor[List] with {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/package.List#