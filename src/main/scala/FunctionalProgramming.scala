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

    // Random break here: How does scala know whether its taking in 2 arg and returning a string output
    // or is it taking in 1 arg and retuning 2 string output?
    // Answer: Its from the Function2[String, String, String] declaration
    // They 2 in Function2 means it takes in 2 arguments
    // The last String means it returns a String
    def apply(arg1: String, arg2: String): String = arg1 + arg2
  }

  println(stringConcatenator.apply("Hello, ", "Scala")) // OR
  println(stringConcatenator("Hello, ", "Scala")) // both return "Hello, Scala"

  println("")
  println("//////////////////////////")
  println("Scala Syntax Sugars")
  println("//////////////////////////")

  // Turning this:
  val doubler: Function1[Int, Int] = new Function1[Int, Int] {
    override def apply(x: Int): Int = x * 2
  }
  println(doubler(4)) // 8

  // into this:
  val doublerSugared = (x: Int) => x * 2
  println(doublerSugared(4)) // 8
  // Scala dont even need you to specify the types of x and return type

  println("")
  println("//////////////////////////")
  println("Higher Order Functions: Map, Flatmap")
  println("//////////////////////////")

  // Takes in a function and returns a function
  val aMappedList: List[Int] = List(1, 2, 3).map(x => x + 1)
  println(aMappedList) // List(2,3,4)
  // Recall this List(2,3,4) is actually a new list

  // Flatmap
  val aFlatMappedList: List[Int] = List(1, 2, 3).flatMap(x => List(x, x + 1))
  println(aFlatMappedList) // List(1,2,2,3,3,4)

  // Filter --> to return even number
  val aFilteredList: List[Int] = List(1, 2, 3, 4, 5, 6).filter(x => x % 2 == 0)
  println(aFilteredList) // List(2,4,6)

  // Return all pairs between the numbers 1,2,3 and the letters a,b,c
  val allPairs = List(1, 2, 3).flatMap(number =>
    List("a", "b", "c").map(letter => (number, letter))
  )
  // for every number, we will take the list abc and append it to the number
  // i.e. a to 1, a to 2, a to 3, then b to 1, b to 2...
  println(allPairs) // List((1,a), (1,b), (1,c),

  println("")
  println("//////////////////////////")
  println("For comprehensions, NOT For Loops")
  println("//////////////////////////")

  val alternativePair = for {
    number <- List(1, 2, 3)
    letter <- List("a", "b", "c")
  } yield (number, letter)
  println(
    alternativePair
  ) // List((1,a), (1,b), (1,c), (2,a), (2,b), (2,c), (3,a), (3,b), (3,c))

  // yield is like the return statement
  // now you see why its different from normal for loops

  println("")
  println("//////////////////////////")
  println("Collections")
  println("//////////////////////////")

  println("")
  println("List")
  val aList = List(1, 2, 3)
  val firstElement = aList.head // 1
  val restOfElements = aList.tail // List(2,3)
  val aPrependedList = 0 :: aList // List(0,1,2,3)
  println(aPrependedList)
  val anExtendedList = aList ++ List(4, 5, 6)
  println(anExtendedList) // List(1,2,3,4,5,6)
  println(aList(1))
  println(aList) // List(1,2,3) - immutable

  println("")
  println("Sequences")
  val aSequence: Seq[Int] = Seq(1, 2, 3)
  println(aSequence)

  // Indexing a sequence
  println(aSequence.apply(1))
  // or
  println(aSequence(1))

  println("")
  println("Vectors")
  val aVector = Vector(1, 2, 3)
  println(aVector)

  println("")
  println("Sets: Identifying repeats")
  val aSet = Set(1, 2, 3, 3, 2)
  println(aSet)
  val sethas5 = aSet.contains(5)
  print("Does Set has 5? " + sethas5 + "\n") // false
  val anAddedSet = aSet + 4
  println("Added Set: " + anAddedSet) // Set(1,2,3,4)
  val aRemovedSet = anAddedSet - 3
  println("Removed Set: " + aRemovedSet) // Set(1,2,4)

  println("")
  println("Ranges")
  val aRange = 1 to 10
  val TwoByTwo = aRange.map(x => x * 2).toList
  println(TwoByTwo)

  println("")
  println("Tuples")
  val aTuple = (1, "hello", true)
  println(aTuple)

  println("")
  println("Maps")
  val aMap: Map[String, Int] = Map(("Alice" -> 123), "Bob" -> 456)
  println("Mapping String to Int: " + aMap)
}
