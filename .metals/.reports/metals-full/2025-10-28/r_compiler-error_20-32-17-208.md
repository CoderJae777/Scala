file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Advanced.scala
### java.lang.StringIndexOutOfBoundsException: Range [665, 671) out of bounds for length 670

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 665
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
  ..@@
}

```



#### Error stacktrace:

```
java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:55)
	java.base/jdk.internal.util.Preconditions$1.apply(Preconditions.java:52)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:213)
	java.base/jdk.internal.util.Preconditions$4.apply(Preconditions.java:210)
	java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:98)
	java.base/jdk.internal.util.Preconditions.outOfBoundsCheckFromToIndex(Preconditions.java:112)
	java.base/jdk.internal.util.Preconditions.checkFromToIndex(Preconditions.java:349)
	java.base/java.lang.String.checkBoundsBeginEnd(String.java:4865)
	java.base/java.lang.String.substring(String.java:2834)
	dotty.tools.pc.completions.CompletionProvider.mkItem$1(CompletionProvider.scala:248)
	dotty.tools.pc.completions.CompletionProvider.completionItems(CompletionProvider.scala:347)
	dotty.tools.pc.completions.CompletionProvider.$anonfun$1(CompletionProvider.scala:149)
	scala.collection.immutable.List.map(List.scala:247)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:141)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: Range [665, 671) out of bounds for length 670