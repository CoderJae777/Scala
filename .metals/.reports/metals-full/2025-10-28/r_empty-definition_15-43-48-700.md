error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/FunctionalProgramming.scala:scala/Int#
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/FunctionalProgramming.scala
empty definition using pc, found symbol in pc: scala/Int#
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Int#
	 -scala/Predef.Int#
offset: 583
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
    override def apply(v1: I@@nt): Int = v1 + 1
  }

  simpleIncrementer.apply(23) // OR
  simpleIncrementer(23) // both return 24

  val stringConcatenator = new Function2[String, String, String] {

    // Random break here: How does scala know whether its taking in 2 arg and returning a string output
    // or is it taking in 1 arg and retuning 2 string output?
    // Answer: Because of the apply function in this case, so in other words, its other lines in the codeblock
    def apply(arg1: String, arg2: String): String = arg1 + arg2
  }

  println(stringConcatenator.apply("Hello, ", "Scala")) // OR
  println(stringConcatenator("Hello, ", "Scala")) // both return "Hello, Scala"
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/Int#