error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/FunctionalProgramming.scala:`<none>`.
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/FunctionalProgramming.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -println.
	 -println#
	 -println().
	 -scala/Predef.println.
	 -scala/Predef.println#
	 -scala/Predef.println().
offset: 849
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/FunctionalProgramming.scala
text:
```scala
object FunctionalProgramming extends App {

  println("")
  println("//////////////////////////")
  println("Built in Apply method")
  println("//////////////////////////")
  class Person(name: String) {
    def apply(age: Int) = println(s"$name is $age years old")
  }

  val bob = new Person("Bob")
  bob.apply(69) // OR
  bob(69)

  println("")
  println("//////////////////////////")
  println("Functional Programming in JVM: FunctionX")
  println("//////////////////////////")

  val simpleIncrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  simpleIncrementer.apply(23) // OR
  simpleIncrementer(23) // both return 24

  val stringConcatenator = new Function2[String, String, String] {
    override def apply(arg1: String, arg2: String): String = arg1 + arg2
  }

  println@@(stringConcatenator.apply("Hello, ", "Scala")) // OR
  println(stringConcatenator("Hello, ", "Scala")) // both return "Hello, Scala"
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.