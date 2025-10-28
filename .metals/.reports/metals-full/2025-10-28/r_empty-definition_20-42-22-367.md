error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Advanced.scala:scala/None.
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Advanced.scala
empty definition using pc, found symbol in pc: scala/None.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -None.
	 -None#
	 -None().
	 -scala/Predef.None.
	 -scala/Predef.None#
	 -scala/Predef.None().
offset: 1191
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Advanced.scala
text:
```scala
object Advanced extends App {

  println("//////////////////////////")
  println("Lazy Evaluation")
  println("//////////////////////////")

  // Call-by-need: its right-hand side is not evaluated at definition time.

  // It’s evaluated the first time you use it,
  // then the result is cached and reused.
  lazy val aLazyValue = 2
  lazy val lazyValueWithSideEffect = {
    println("I am so lazy!")
  }
  println(lazyValueWithSideEffect)
  // Observe the output, the "I am so lazy!" is printed only once,
  // and its printed only when we first access lazyValueWithSideEffect
  // the unit () is printed because the block returns Unit type
  // if you println(println) you get () as output

  // Useful in infinite collections
  // When you need to just run something once and only when needed

  println("")
  println("//////////////////////////")
  println("Option: for null safety")
  println("//////////////////////////")
  def methodWhichCanReturnNull(): String = "Hello, Scala"
  val anOption = Option(methodWhichCanReturnNull())

  val stringProcessing = anOption match {
    case Some(string) => s"I have obtained a valid string: $string"
    case @@None         => "I have no string"
  }

  // Very unreadable and defensive
//   def methodWhichCanThrowException(): String = throw new RuntimeException
//   try {
//     methodWhichCanThrowException()
//   } catch {
//     case e: Exception => "I caught an exception: " + e.getMessage
//   }

  // Alternative
  def methodWhichCanThrowExceptionBetter(): String = throw new RuntimeException
  val atry = Try(methodWhichCanThrowExceptionBetter())

  val anotherStringProcessing = atry match {
    case Success(string) => s"I have obtained a valid string: $string"
    case Failure(exception) =>
      "I caught an exception: " + exception.getMessage
  }

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/None.