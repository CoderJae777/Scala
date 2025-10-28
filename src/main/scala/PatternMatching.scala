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
  println("//////////////////////////")
  println("Pattern Matching with Tuples")
  println("//////////////////////////")

  val aTuple = ("Manchester United", "UK")
  val description = aTuple match {
    case (team, country) => s"$team is a football club based in $country."
    case _               => "Identity is unknown"
  }

  println(description)

  println("")
  println("//////////////////////////")
  println("Pattern Matching with Lists")
  println("//////////////////////////")
    val aList = List(1, 2, 3, 4, 5)
    val describeList = aList match{
        case Nil => "The list is empty"
        case head :: next => s"The list starts with $head and has tail $next"
        case _ => "Something else"
    }

  println(describeList)

}
