error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala:scala/App#
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala
empty definition using pc, found symbol in pc: scala/App#
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -App#
	 -scala/Predef.App#
offset: 20
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala
text:
```scala
object OOP extends A@@pp {
  println("//////////////////////////")
  println("OOP in Scala")
  println("//////////////////////////")

  println("")
  class Animal {
    val age: Int = 0
    def eat() = println("I'm eating")

  }
  // Constructing an instance of a class
  // Class Dog has methods bark and inherits method eat and val age from Animal
//   val animal = new Animal

  class Dog(name: String) extends Animal {
    def bark() = println("Woof!")
  }

  println("")
  println("//////////////////////////")
  println("Instantation and Inheritance")
  println("//////////////////////////")
  // Instance of Dog is also an instance of Animal because you extended Animal
  val HachiTheDog: Dog = new Dog("Hachi")
  HachiTheDog.eat()
  HachiTheDog.bark()

  val BusterTheDog: Animal = new Dog("Buster")
  BusterTheDog.eat()
  // BusterTheDog.bark() // not allowed

  // Huh? Why?
  // Answer: because you declared Buster as an Animal type
  // So the compiler only allows you to call methods defined in Animal

  println("")
  println("//////////////////////////")
  println("Abstract classes - cannot be instantiated")
  println("//////////////////////////")

  // for what?
  // To block users from creating instances of the base class
  // BUT still allow them to extend the base class
  // useful for storing specific methods
  abstract class CanMeow extends Animal {
    def meow(): Unit // abstract method
  }

  class Cat extends Animal {
    def meow(): Unit = println("Meow")
  }

  val kitty = new Cat
  kitty.meow()
  kitty.eat()

  HachiTheDog.eat()
  HachiTheDog.bark()
  // HachiTheDog.meow() --> not allowed because Dog class did not extend CanMeow
  // BusterTheDog.meow() --> not allowed because Dog class did not extend CanMeow

  println("")
  println("//////////////////////////")
  println("Traits - compulsory methods to implement")
  println("//////////////////////////")

  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  trait Amphibious {
    def swim(): Unit
  }

  // And yes you can extend multiple traits
  class Crocodile extends Animal with Carnivore with Amphibious {
    // This override function eat is compulsory because of the Carnivore trait
    override def eat(animal: Animal): Unit =
      println(s"I'm a croc and I'm eating ${animal.toString()}")

    // This override function swim is compulsory because of the Amphibious trait
    override def swim(): Unit =
      println("I'm swimming in the water")
  }

  println("")
  println("//////////////////////////")
  println("Infix notation")
  println("//////////////////////////")

  // Only works for methods with a single parameter/argument
  val Billy = new Crocodile
  Billy.eat(kitty)
  Billy eat kitty // infix notation

  println("")
  println("//////////////////////////")
  println("Ridiculous naming is allowed in scala")
  println("//////////////////////////")
  trait Thinking {
    def ?!(thought: String): Unit
  }

  // Also, unrelated, but with is not tied to traits only
  // You have to first use extends then add with for more traits

  class Human extends Thinking {
    override def ?!(thought: String): Unit =
      println(s"I'm thinking: $thought")
  }

  val Bob = new Human
  Bob.?!("How is this allowed in Scala?")

  // Now this with infix notation AHHAHAHA
  Bob ?! "How is this allowed in Scala?"

  println("")
  println("//////////////////////////")
  println("Operators are methods too")
  println("//////////////////////////")

  val basicMath = 1 + 2
  val anotherBasicMath = 1.+(2) // same as above
  println(basicMath)
  println(anotherBasicMath)

  println("")
  println("//////////////////////////")
  println("Anonymous classes")
  println("//////////////////////////")

  // recall here that Carnivore is a trait
  // So BY RIGHT we cannot instantiate it directly
  // but we can create an anonymous class that extends Carnivore
  // THEN we can instantiate that class immediately
  val dinosaur = new Carnivore {
    override def eat(animal: Animal): Unit =
      println("I'm a dinosaur and I'm eating you!")
  }
  // for what? Cause im lazy
  // if i just want a one-off implementation of a class
  // i dont need to create a whole new class to extend Carnivore

  println("")
  println("//////////////////////////")
  println("Singleton")
  println("//////////////////////////")
  // only one instance of this object will ever exist
  object MySingleton {
    val myOnlySpecialValue = 42169
    def myOnlySpecialMethod(): Int = 42
    def apply(x: Int): Int = x + 1
  }

  println(MySingleton.myOnlySpecialValue)
  println(MySingleton.apply(65))
  println(MySingleton(65)) // same

  println("")
  println("//////////////////////////")
  println("Companion objects")
  println("//////////////////////////")

  object Animal {
    private val canLiveIndefinitely = false
  }

  // Companions can access each other's private members --> probably wont use it

  println("")
  println("//////////////////////////")
  println("Case classes vs Class")
  println("//////////////////////////")

  case class Person(name: String, age: Int)
  Person("Bob", 54) // no new keyword needed
  val jim = Person("Jim", 23)
  val sarah = Person("Sarah", 23)

  class PersonNotCaseClass(name: String, age: Int)
  val jimNotCaseClass = new PersonNotCaseClass("Jim", 23)

  // Main Advantages of case classes: Structural equality
  jim == sarah // false, structural equality
  val jim2 = Person("Jim", 23)
  jim == jim2 // true

  // whereass for a normal class
  jimNotCaseClass == jimNotCaseClass // false
  val jimNotCaseClass2 = new PersonNotCaseClass("Jim", 23)
  jimNotCaseClass == jimNotCaseClass2 // false

  println("")
  println("//////////////////////////")
  println("Exception")
  println("//////////////////////////")

  // Scala is compiled by JVM
  try {
    val x: String = null
    println(x.length)
  } catch {
    case e: Exception => println("Caught an exception")
  }

  // Execute this no matter what
  finally {
    println("Finally block executed")
  }

  println("")
  println("//////////////////////////")
  println("Generics")
  println("//////////////////////////")
  abstract class MyList[T] {
    def head: T
    def tail: MyList[T]
  }

  print("")
  println("//////////////////////////")
  println("List in Scala")
  println("//////////////////////////")
  val anIntList: List[Int] = List(1, 2, 3)
  println(anIntList.head)
  println(anIntList.tail)
  val aStringList: List[String] = List("hello", "world")
  println(aStringList.head)

  // #1: Scala's val is immutable
  val reversedList = anIntList.reverse // returns a NEW list
  println(reversedList) // They look the same but they are different instances

  // Any modifications to an object must return ANOTHER object
  /*
  But why?
  Good for multithreaded environment
  Helps make sense of code ("reasoning about code")
   */

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/App#