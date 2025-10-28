error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/PatternMatching.scala:scala/Predef.identity().
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/PatternMatching.scala
empty definition using pc, found symbol in pc: scala/Predef.identity().
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -identity.
	 -identity#
	 -identity().
	 -scala/Predef.identity.
	 -scala/Predef.identity#
	 -scala/Predef.identity().
offset: 911
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/PatternMatching.scala
text:
```scala
object PatternMatching extends App {
  println("//////////////////////////")
  println("Pattern Matching")
  println("//////////////////////////")
  val anInteger = 55
  val order = anInteger match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => anInteger + "th"
  }

  println("")
  println("Pattern Matching with Case Classes")
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 54)

  val personGreeting = bob match {
    case Person(n, a) if a < 18 =>
      s"Hello, my name is $n and I am $a years old."
    case Person(n, a) => s"Hello, my name is $n and I am $a years old."
    case _            => "I don't know who I am"
  }

  println(personGreeting)

  println("")
  println("Pattern Matching with Tuples")
    val aTuple = ("Gay", "Lord")
    val description = aTuple match {
      case (sexuality, "Lord") => s"$id@@entity is a lord"
      case (identity, "Peasant") => s"$identity is a peasant"
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/Predef.identity().