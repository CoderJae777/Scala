error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala:`<none>`.
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala
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
offset: 3674
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/OOP.scala
text:
```scala
object OOP extends App {
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
  print@@ln(basicMath)
  println(anotherBasicMath)


  
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.