# Scala

---

_This markdown file documents everything learned in 50.054 Compiler Design and Program Analysis related to Scala_

## Content Page

---

- [Lambda Calculus to Scala](#lambda-calculus-to-scala)
- [Other Terminologies](#other-terminologies)
  - [REPL](#repl)
  - [Immutable](#immutable)
- [Running Scala](#running-scala)
- [Running test cases in cohort](#running-test-cases-in-cohort)
- [Functions and Methods](#functions-and-methods)
- [OOP in Scala](#oop-in-scala)
- [Variable Types](#variable-types)
  - [Val vs Var](#val-vs-var)
- [Type Inference](#type-inference)
- [Expressions vs Statements](#expressions-vs-statements)
- [If Else](#if-else)
- [Recursion](#recursion)
  - [Regular Recursion](#regular-recursion)
  - [Tail Recursion Optimisation](#tail-recursion-optimisation)
- [Map](#map)
  - [FlatMap](#flatmap)
- [Fold](#fold)
  - [foldRight vs foldLeft](#foldright-vs-foldleft)
- [Filter](#filter)
- [For expression in Scala](#for-expression-in-scala)
- [Enum (Algebraic Data Type example)](#enum-algebraic-data-type-example)
  - [Using enum (ADT example)](#using-enum-adt-example)
- [List](#list)
  - [Creating an immutable list](#creating-an-immutable-list)
  - [Accessing Elements](#accessing-elements)
  - [Attempting to Modify a list](#attempting-to-modify-a-list)
  - [Iterating over a list](#iterating-over-a-list)
  - [Flattening a list](#flattening-a-list)
  - [Pattern Matching with Lists](#pattern-matching-with-lists)
  - [Pattern Constructors](#pattern-constructors)
- [Match](#match)
  - [Summing a List (match)](#summing-a-list-match)
  - [Indexing an element (match)](#indexing-an-element-match)
  - [findMax() function (match)](#findmax-function-match)
- [Span Function](#span-function)
- [Currying](#currying)
- [Function Composition](#function-composition)
- [Generic/ Polymorphic ADT](#generic-polymorphic-adt)
- [Subtyping inside Enum](#subtyping-inside-enum)
- [Covariant and Invariant](#covariant-and-invariant)
- [Function Overloading](#function-overloading)
- [case Class](#case-class)
- [Type Class (Traits)](#type-class-traits)
- [Higher Kinded Type & Functor Type Class](#higher-kinded-type--functor-type-class)
- [Kinds vs Types](#kinds-vs-types)
- [Functor Laws](#functor-laws)
  - [Why the need?](#why-the-need)
- [Foldable Type Class](#foldable-type-class)
- [Error Handling with Option Type](#error-handling-with-option-type)
- [Error Handling with Either Type](#error-handling-with-either-type)
- [Derived Type Class](#derived-type-class)
- [(Continued) Derived show implementation](#continued-derived-show-implementation)
- [Option](#option)
  - [Uses of Option](#uses-of-option)
- [Applicative Functor](#applicative-functor)

---

## Lambda Calculus to Scala

[back-to-top](#content-page)
| Lambda Calculus | Scala |
| -------------------- | ------------------------------ |
| Variable | x |
| Constant | 1, 2, true, false |
| Lambda abstraction λx.t| (x: T) ⇒ e |
| Function application t₁ t₂| e1(e2) |
| Conditional | if (e1) { e2 } else { e3 } |
| Let Binding | val x = e1; e2 |
| Recursion | def f(x:Int): Int = f(x); f(1) |

Every Scala expression can be represented in lambda calculus form.

Scala runs imperatively &rarr; step-by-step like C or Java

---

## Other Terminologies

---

##### REPL

[back-to-top](#content-page)

Read-Eval-Print Loop &rarr; an interactive programming environment where you can type code line by line and immediately see the result.

**_Scala is a REPL_**

---

##### Immutable

[back-to-top](#content-page)

Cannot be changed after creation

---

## Running Scala

Make sure your file extensions is `.scala`

Make sure all your code is nested under an object that extends app

```scala
object name_of_file extends App {

  // Code here

}
```

```scala

```

---

[back-to-top](#content-page)

1. Using Scalac and Scala commands

_Make sure to cd into the right directory first_

```bash
# Compile
scalac name_of_file.scala

# Run
scala name_of_file.scala
```

---

2. Using build.sbt (Alternative)

Make sure the scala files you are running is in exactly:

- src
  - main
    - scala
      - name_of_file.scala

```bash
# Activate sbt at the root directory
sbt

# Running
clean
compile
run

```

---

3. VSC inbuilt run (Another alternative way to run Scala)

_Need to download Scala CLI first_

---

## Running test cases in cohort

```bash
# Running all Test Cases
test

# Running one Specific Test Case
testOnly *TestEx1
```

---

## Functions and Methods

[back-to-top](#content-page)

```scala
def name_of_function (name_of_parameters : type_of_parameter) : return_type = {
   body_of_function
}

// Example:
def add(x:Int, y:Int): Int = {
  x + y
}
```

- `def`
  - Keyword to define a function with type parameters
- `add`
  - Function name
- `(x:Int, y:Int)`
  - Function will take in parameter x, with type Integer and y, with type Integer as well
- `:Int`
  - Return type will be an Int

---

Another Example

```scala
def flatten[A](l: List[List[A]]) = { }
```

- `[A]` &rarr; Type parameter
  - it makes the function generic, meaning it can work on lists of any type
- `(l :List[List[A]])` &rarr; Parameter list
  - function takes one parameter called l
- `= {}` &rarr; `{}` is the function body

---

## OOP in Scala

[back-to-top](#content-page)

```Scala
trait FlyBehavior{
  def fly()
}

abstract class Bird(species:String, fb:FlyBehavior){
  def getSpecies():String = this.species
  def fly():Unit=this.fb.fly()
}

class Duck extends Bird("Duck", new FlyBehavior(){
  override def fly() = println("I can't fly!")
})

class BlueJay extends Bird("BlueJay", new FlyBehavior(){
  override def fly() = println("Swoosh")
})
```

- trait defines an interface
- class constructors are in-line
- Unit is a type, similar to void in Java
- Functions and methods' return types can be inferred by compiler

---

## Variable Types

[back-to-top](#content-page)

### Val vs Var

```scala
// immutable
val x = 5

// mutable
var y = 10
```

## Type Inference

Scala often figures out type automatically

```scala
val n = 42 //inferred as Integer
val s = "hi" //inferred as String
val n: Int = 42 //or you can declare yourself
```

---

## Expressions vs Statements

[back-to-top](#content-page)

- Expression has value _e.g. 1+2 evaluates to 3_
- Statement does something but has no meaningful value _e.g. println("hi")_

---

## If Else

[back-to-top](#content-page)

```scala
val max = if (a>b) a else b
```

- if a is bigger than b, return a, else return b

---

## Recursion

[back-to-top](#content-page)

### Regular Recursion

```scala
def sum(l: List[Int]): Int = l match {
  case Nil      => 0
  case x :: xs  => x + sum(xs)
}

sum(List(1,2,3))

// sum(1::2::3::Nil)
// → 1 + sum(2::3::Nil)
// → 1 + (2 + sum(3::Nil))
// → 1 + (2 + (3 + 0))
// Unwind call stack
// → 1 + (2 + 3 )
// → 1 + 5
// → 6
```

Each recursive call has to wait for the next one to finish

### Tail Recursion Optimisation

- recursive call is the last operation
- "throws" away the previous recursion

```scala

import scala.annotation.tailrec

def sumTail(l: List[Int]): Int = {
  @tailrec
  def go(lst: List[Int], acc: Int): Int = lst match {
    case Nil      => acc
    case x :: xs  => go(xs, acc + x)  // recursive call is LAST
  }
  go(l, 0)
}

sumTail(List(1,2,3))

// go([1,2,3], 0)
// → go([2,3], 1)
// → go([3], 3)
// → go([], 6)
// → 6
// Done ~

```

- `acc`

  - the 'accumulator', a variable that accumulates or keeps track of the running total of the computations as we go deeper into recursion

  - instead of waiting to add everything after coming back up, just add along the way

---

## Map

[back-to-top](#content-page)

- Idea: apply the function to every element in the list

```scala
val nums = List(1,2,3,4)
val doubled = nums.map(x => x * 2)
println(doubled)

// Output: List(2,4,6,8)
```

[Bad Example] Trying to add 10 to each elements in the list

```scala
def addToEach(x:Int, l:List[Int]):List[Int] = l match {
  case Nil => Nil
  case (y::ys) => {
    val yx = y+x
    yx::addToEach(x,ys)
  }
}

// addToEach(10, List(1,2,3))

// Input x=10, l = [1,2,3]
// y = 1, ys = [2,3]
// Compute yx = 1 + 10 = 11
// Call yx::addToEach(10, [2,3])

// Input x=10, l = [2,3]
// y = 2, ys = [3]
// yx = 2 + 10 = 12
// Call yx::addToEach(10, [3])

// Input x=10, l = [3]
// y = 3, ys = []
// yx = 3 + 10 = 13
// Call yx::addToEach(10, [])

// Final call his Nil

// 13 :: Nil
// 12 :: (13::Nil)
// 11 :: (12 :: (13::Nil))
// Result = List(11, 12, 13)

```

From &uarr; to &darr;

```scala
def addToEach(x:Int, l:List[Int]):List[Int] = l.map(y=>y+x)
```

---

### FlatMap

- Map + Flatten (joining multiple lists into 1)

```scala
// Regular Map
val nums = List(1, 2, 3)
val result = nums.map(x => List(x, x + 1))

// List(List(1, 2), List(2, 3), List(3, 4))


// FlatMap
val nums = List(1, 2, 3)
val result = nums.flatMap(x => List(x, x + 1))

// List(1, 2, 2, 3, 3, 4)

```

---

## Fold

[back-to-top](#content-page)

- Reduce a collection down to a single value by repeatedly applying a function
- has:
  - foldLeft &rarr; process left to right
  - foldRight &rarr; process right to left
  - fold &rarr; pick a direction depending on the collection, usually left
- Requires:

  - A starting value (the accumulator).

  - A binary operation (a function that combines the accumulator with each element).

General Form

```scala
list.fold(initialValue)((acc,element) => newAcc)

// Start with initialValue,
// then for each element,
// update the acc (accumulator).
```

Example

```scala
//  Goal : 1 + 2 + 3 + 4
val nums = List(1, 2, 3, 4)
val result = nums.fold(0)((acc, x) => acc + x)
println(result)


// Step 0: acc = 0, x = null, new acc = null
// Step 1: acc = 0, x = 1, new acc = acc + x = 1
// Step 2: acc = 1, x = 2, new acc = acc + x = 3
// Step 3: acc = 3, x = 3, new acc = acc + x = 6
// Step 4: acc = 6, x = 4, new acc = acc + x = 10

// Final output: (((0 + 1) +2) +3) +4
```

### foldRight vs foldLeft

- foldLeft &rarr; [Tail-recursive](#tail-recursion-optimisation)

```scala
List(1,2,3).foldLeft(0)(_ - _)

// ((0-1)-2)-3 = -6

List(1,2,3).foldRight(0)(_ - _)

// 1-(2-(3-0)) = 2

```

---

## Filter

[back-to-top](#content-page)

- Takes a predicate function (a function that returns true/false)
- Keeps only elenments for which the function is true
- Returns a new list, original stays unchanged

```scala
val nums = List(1,2,3,4,5,6)
val evens = nums.filter(x => x % 2 == 0)
println(evens)

Output:
List(2,4,6)

```

---

## For expression in Scala

[back-to-top](#content-page)

- combination of flatmap and map

```python
for x in [1,2,3]
  x+1
```

is the same as:

```
map(lambda x:x+1, [1.2.3])
```

in Scala

```scala
for {x <- List(1,2,3)} yield (x+1)
```

using map

```scala
List(1,2,3).map(x=>x+1)
```

---

## Enum (Algebraic Data Type example)

[back-to-top](#content-page)

```scala
enum MathExp {
  case Plus(e1:MathExp, e2:MathExp)
  case Minus(e1:MathExp, e2:MathExp)
  case Multiply(e1:MathExp, e2:MathExp)
  case Div(e1:MathExp, e2:MathExp)
  case Const(v:Int)
}

import MathExp.*
val

def eval(e:MathExp):Int = e match {
  case Plus(e1, e2) => eval(e1) + eval(e2)
  case Minus(e1, e2) => eval(e1) - eval(e2)
  case Mult(e1, e2) => eval(e1) * eval(e2)
  case Div(e1, e2) => eval(e1) / eval(e2)
  case Const(i) => i
}

```

- `enum` keyword lets you define a closed set of possible values for a type
- `MathExp` is a type
- Each `case` (like `Plus`,`Minus`, etc) is a **constructor** &rarr; a way to create one "variant" of that type
- As all case belong to the same enum, the compiler knows all possibilities

### Using enum (ADT example)

```scala
import MathExp.*
val expression = Mult(Plus(Const(1), Const(2), Const(3)))

// (1+2)*3
```

---

## List

[back-to-top](#content-page)

### Creating an immutable list

```Scala

val l = List(1, 2, 3)
```

- recall 'val' defines an immutable variable

### Accessing Elements

```Scala
l(1)
```

- Similar to python's indexing

### Attempting to Modify a list

```Scala
l(1) = 3
```

- _ERROR_ &rarr; Scala's list are immutable

### Iterating over a list

```Scala
for (i <- l) {println(i)}

Output:
  1
  2
  3
```

### Flattening a list

```scala
def flatten[A](l: List[List[A]]): List[A] =
  l.flatMap(inner => inner)

flatten(List(List(1,2), List(3,4), List(5)))
// Result: List(1, 2, 3, 4, 5)

```

### Pattern Matching with Lists

- use [Match](#match)

```scala
l match {
  case Nil => "empty"
  case (x :: xs) => "not empty"
}

```

### Pattern Constructors

```Scala

//Nil and '::'

Nil -> represents the empty list
:: -> constructs a list by perpending an element to another list
```

E.g.

```Scala


1 :: 2 :: Nil = List(1,2)
```

- A list in scala is either:
  - Empty &rarr; Nil
  - Non-empty &rarr; a head element plus a tail list, written as head :: tail

---

## Match

[back-to-top](#content-page)

```Scala
l match {
  case Nil      => "empty"
  case (x :: xs) => "not empty"
}
```

- match is like a switch-case for structured data
- in the above code, we are matching l with 2 potential patterns, an empty list or a non-empty list
- l is being tested against 2 patterns
- if l is an empty list, it will return "empty"
- if l is a non-empty list, it means x will match the first element (the head) and xs will match the tail (a list of all the remaining elements)

```Scala
val list = List(10, 20, 30)

list match {
  case Nil       => println("empty")
  case (x :: xs) => println(s"head = $x, tail = $xs")
}

```

Output:

```bash
head = 10, tail = List(20,30)
```

---

### Summing a List (match)

[back-to-top](#content-page)

```Scala
def sum(l: List[Int]): Int = {
  l match {
  case Nil     => 0
  case (x::xs) => x + sum(xs)
  }
}

```

- If the list is empty (Nil), return 0.
- If the list has a head (x) and tail (xs), return x + sum(xs).
  → This recursively sums all elements.

Example:

```Scala
sum(List(1,2,3))
// = 1 + sum(List(2,3))
// = 1 + (2 + sum(List(3)))
// = 1 + (2 + (3 + sum(Nil)))
// = 1 + (2 + (3 + 0))
// = 6

```

### Indexing an element (match)

[back-to-top](#content-page)

```Scala
def first(l: List[Int]): Int = l match {
  case Nil => None
  case (x::xs) => x
}
```

- This is like [accessing an element](#accessing-elements) but with extra steps
- Why is this better? It handles an OutOfBounds error by returning None instead
- def first(....) &rarr; defining a function named first
- Parameter: l:List[Int] &rarr; l is a list of integers
- : Int &rarr; the function returns an Int

---

### findMax() function (match)

[back-to-top](#content-page)

```Scala
def findMax(l:List[Int]):Int = {
  l match{

    case Nil => Int.MinValue
    case (x::xs) =>
      val tailMax = findMax(xs)
      if (x>tailMax) x else tailMax
  }
}
```

- def findMax &rarr; defines a function named findMax
- (l: List[Int]) &rarr; takes one parameter l, which must be a list of integers
- : Int &rarr; return type is an integer
- = &rarr; everything after the = is the body

#### Case #1

```scala
case Nil => Int.MinValue
```

- if l is empty, return Int.MinValue
- MinValue is the built-in smallest integer of scala (-2,147,483,648)
- We need to return this because we are guarenteeing that any real integer in the list will be greater than this
- In the event the list has only 1 value that is -50 for sure, it is STILL the biggest one despite being the only one, so it will be compared to the Int.MinValue and "win"

#### Case #2

```scala
case x :: xs =>
  val tailMax = findMax(xs)
  if (x > tailMax) x else tailMax
```

- if l is non-empty, Scala splits it into
  - x &rarr; the head (first element)
  - xs &rarr; the tail (the rest of the list)
- Then, it recursively calls findMax on the tail(xs) which will find the maximum of the rest of the listL
- it compares the head of the list(x) with the maximum of the tail (tailMax)
- if head is larger, return x, else return taiMax

**Example**

1. **Start**
   - `l = List(3, 7, 2)` → not empty
   - `x = 3`, `xs = List(7, 2)`
   - `tailMax = findMax(List(7, 2))`

---

2. **Recursive call: `findMax(List(7, 2))`**
   - `x = 7`, `xs = List(2)`
   - `tailMax = findMax(List(2))`

---

3. **Recursive call: `findMax(List(2))`**
   - `x = 2`, `xs = Nil`
   - `tailMax = findMax(Nil)`

---

4. **Base case: `findMax(Nil)`**
   - Returns `Int.MinValue`

---

5. **Unwinding recursion**
   - Compare `2` vs `Int.MinValue` → **2**
   - Compare `7` vs `2` → **7**
   - Compare `3` vs `7` → **7**

---

✅ Final result: `7`

## Span Function

[back-to-top](#content-page)

INCOMPLETE

```scala
// span function takes a list and returns a list of pairs
// each pair consists of the first element and each of the other elements in the list
// span(List(1, 2, 3, 4))
// Output: List((1,2), (1,3), (1,4))
def span[A](l: List[A], check: A => Boolean): (List[A], List [A]) = l match {
    case Nil => Nil
    case (x::xs) if check(x) => {
        val (ys, zs) = span(xs, check)
        (x::ys, zs)
    }
}
```

---

## Currying

[back-to-top](#content-page)

Normal Form

```scala
def tax(price:Double, rate:Double):Double = price * rate

// When calling tax, need to always provide price and rate as arguments

```

Currying Ver/Form

```scala
def curriedTax(rate: Double)(price: Double): Double = price * rate
  val gst_with_rate = curriedTax(0.08)(_: Double)
  val gst_with_price = curriedTax(_: Double)(100)

// Good because i can FIX rate and have price as argument or FIX price and have rate as argument instead of having to always providing both

```

Why?

- Code reusability
- Partial application, plusone becomes a function, dont diedie need 2 parameters

---

## Function Composition

[back-to-top](#content-page)

- Combining 2 or more functions to produce a new function

```scala
def f(x: Int): Int = x + 1
def g(x: Int): Int = x * 2

val h = f andThen g // h(x) = g(f(x))
val k = f compose g // k(x) = f(g(x))

h(3) // f(3) = 4, g(4) = 8 → result: 8
k(3) // g(3) = 6, f(6) = 7 → result: 7

andThen: f andThen g = g(f(x))
compose: f compose g = f(g(x))
```

---

## Generic/ Polymorphic ADT

[back-to-top](#content-page)

recall [ADT](#enum-algebraic-data-type-example)

```scala
enum List[+A] {
  case Nil
  case Cons(x: A, xs: MyList[A])
}

```

- `List[+A]` is generic: it can be `List[Int]`, `List[String]`, etc.
- BUT it cannot have both Int and String in the same 

## Subtyping inside Enum

```scala
enum Shape {
  case Circle(radius: Double)
  case Rectangle(width: Double, height: Double)
}

// Double is a variable type in Scala, similar to float

val c: Shape = Shape.Circle(2.0)
val r: Shape = Shape.Rectangle(3.0, 4.0)

println(area(c)) // Output: 12.566...
println(area(r)) // Output: 12.0
```

- `Shape.Circle` and `Shape.Rectangle` are both subtypes of `Shape`
- You can write functions that accept `Shape` and use pattern matching to handle each subtype
- It allows you to treat all cases as the same base type (Shape), but also access specific data for each subtype

---

## Covariant and Invariant

_(not tested)_

[back-to-top](#content-page)

```scala
class InvariantBox[A]
class CovariantBox[+A]

val cov: CovariantBox[Any] = new CovariantBox[String] // OK
val inv: InvariantBox[Any] = new InvariantBox[String] // Error
```

- the + determines whether its Co or In

---

## Function Overloading

[back-to-top](#content-page)

- use the same function name for multiple implementations in different type context

```scala
def greet(name: String): String = s"Hello, $name!"
def greet(name: String, age: Int): String = s"Hello, $name! You are $age years old."
```

- Issue 1: Type mismatch error
- Issue 2: Duplicated Code

---

## case Class

[back-to-top](#content-page)

- enum with only 1 constructor

```scala
enum Person {
  case Person (name: String, contacts:List[Contact])
}

// can be written as:

case class Person(nume: String, contacts: List[Contact])
```

---

## Type Class (Traits)

[back-to-top](#content-page)

- recall Trait in Java is a class where its methods are compulsory when instantiated

- a "contract" for behavior that you can give to any type, even those you didn't write youtself

_Imagine you have different kinds of vehicles: cars, bikes, boats. You want to "drive" them, but not all vehicles have a steering wheel or pedals. Instead of forcing every vehicle to inherit from a "Drivable" class, you create a "Drivable" contract. If a vehicle can be driven, you provide instructions for how to drive it._

**Type class**: The "Drivable" contract.
**Type class instance**: The specific instructions for each vehicle.
**Generic function**: A function that can "drive" any vehicle, as long as it has instructions.

```scala
// Define a type class called Drivable for any type A
trait Drivable[A] = {
  // Specifies that any type A with a Drivable instance MUST HAVE the drive method
  def drive(a: A): String
}

// Type Class instances
// These are case classes representing different vehicle types
case class Car(model: String)
case class Bike(brand: String)
case class Boat(name: String)

///////////////////////////////////////////////////////
// You only need a case class when you want to create a new type.
// For built-in types, you can directly create type class instances.
///////////////////////////////////////////////////////

// Drivable instance for Car (Preferred for Scala 3)
given Drivable[Car] with {
  def drive (c:Car): string = s"Driving car: ${c.model}"
}

// Drivable instance for Car (Older but still works)
given carDrivable: Drivable[Car] = new Drivable[Car] {
  def drive(c: Car): String = s"Driving car: ${c.model}"
}
// c is the name
// Car is the type class instance
```

- `given` &rarr; the type class instance keyword

---

Class Example

```scala

trait JS[A] {
  def toJS(v:A):String
}

// Type class instance for Int
given JS[Int] with {
  def toJS(v: Int): String = v.toString
}

// Type class instance for String
given JS[String] with {
  def toJS(v: String): String = s"'${v}'"
}


given toJSList[A](using jsa:JS[A]):JS[List[A]] = new JS[List[A]] {
  def toJS(as:List[A]):String = {
    val j = as.map(a=>jsa.toJS(a)).mkString(",")
    s"[${j}]"
  }
}
```

- `toJSList` is used to define a type class instance for `List[A]`
- `val j...mkString(", ")` &rarr; For each element a in the list, convert it to JS using
- `enum` variable type is private, whereas `type class` can still change (recall [enum](#algebraic-data-type))

---

## Higher Kinded Type & Functor Type Class

[back-to-top](#content-page)

- a type that takes another type constructor as a parameter
- Lets you write generic code for type constructors (like List, Option, Either), not just for concrete types (like Int, String).
- a regular generic type: `List[Int]` &rarr; List takes Int
- a higher-kinded type: `Functor[F[_]]` &rarr; Functor takes something like List, Option

```scala
trait Functor[T[_]] {
  def map[A,B](t:T[A])(f:A => B):T[B]
}
```

## Kinds vs Types

- **Values** like `42, "hello"` have **Types** `Int, String` respectively
- **Types** themselves have a "type of types" &rarr; and those are called **KINDS**
- **Kinds** describes what shape of type arguments a type constructor accepts
- Kinds are to types what types are to values
- A type constructor like `List` has kind _ &rarr; _
- List[_] by itself is not a type &rarr; takes 1 type argument (of kind _) and produces a new type (also of kind _)

```scala
val list = List(1,2,3)
```

- `list.head` has value: 1, Type: Int, Kind: \*
- `list` has value List(1,2,3), Type: List(Int), Kind : \*
- List(the type constructor) has Kind: \*&rarr;\*

## Functor Laws

1. Identity Law

Mapping with the identity function (id(x) = x) should not change the functor’s contents.

```scala
List(1,2,3).map(x => x) == List(1,2,3)

```

2. Composition Law

Recall [function composition](#function-composition)

```scala
fa.map(f).map(g) == fa.map(g compose f)

val f: Int => Double = _ * 2.0
val g: Double => String = _.toString

List(1,2,3).map(f).map(g)
// = List("2.0", "4.0", "6.0")

List(1,2,3).map(g compose f)
// = List("2.0", "4.0", "6.0")

```

### Why the need?

A 'bad' functor could cheat:

- ignore the function you give to `map`
- apply the functon multiple times/ in the wrong order
- randomly shuffle or duplicate elements

---

## Foldable Type Class

recall [fold](#fold)

---

## Error Handling with Option Type

[back-to-top](#content-page)
A better alternative to try-catch exception

```scala
enum Option[+A] {
    case None
    case Some(v:A)
}

```

```scala
def eval(e:MathExp):Option[Int] = e match {
case MathExp.Plus(e1, e2) => eval(e1) match {
  case None => None
    case Some(v1) => eval(e2) match {
      case None => None
      case Some(v2) => Some(v1 + v2)
  }
}
// case for catching division by 0
// if u divide by 0 it just returns 'None' by checking the denominator
case MathExp.Div(e1, e2) => eval(e1) match {
  case None => None
  case Some(v1) => eval(e2) match {
    case None => None
    case Some(0) => None
    case Some(v2) => Some(v1 / v2)
  }
}
case MathExp.Const(i) => Some(i)
}

eval(Div(Const(1), Minus(Const(2), Const(2))))
// Output: None
```

---

## Error Handling with Either Type

[back-to-top](#content-page)

- Handle Errors with Option Type has one remaining issue
  - i.e. there is no info with the error in None &rarr; if got multiple cases to check multiple errors, we dont if the error is us trying to divide by 0 or sth else

```scala
def eval(e: MathExp): Either[ErrMsg,Int] = e match {
    case MathExp.Plus(e1, e2) =>
    eval(e1) match {
      case Left(m) => Left(m)
      case Right(v1) => eval(e2) match {
        case Left(m) => Left(m)
        case Right(v2) => Right(v1 + v2)
      }
    }
// cases omitted for Minus and Mult
case MathExp.Div(e1, e2) =>
  eval(e1) match {
    case Left(m) => Left(m)
    case Right(v1) => eval(e2) match {
      case Left(m) => Left(m)
      case Right(0) => Left(s"div by zero caused by ${e.toString}")
      case Right(v2) => Right(v1 / v2)
  }
}
case MathExp.Const(i) => Right(i)
}
eval(Div(Const(1), Minus(Const(2), Const(2))))
// yields Left(div by zero caused by Div(Const(1),Minus(Const(2),Const(2))))
```

## Derived Type Class

- a type class instance that the compiler auto-generates for an algebraic data type (case class/ enum/ sealed trait)

- for what? Dont need nearly identical boilerplate for every case class

Simple Show type class

```scala
trait Show[T]:
  def show(t: T): String
```

A case class

```scala
case class Person(name: String, age: Int)
```

Hand-written Show instance

```scala
given Show[Person] with
  def show(p: Person): String =
    s"Person(name=${p.name}, age=${p.age})"
```

Usage

```scala
val p = Person("Ada", 30)
println(summon[Show[Person]].show(p))
// Output: Person(name=Ada, age=30)
```

Now you add a field to the model: Person has country and you did not change the Show

```scala
case class Person(name:String, age:Int, country: String)
```

Usage

```scala
val p2 = Person("Ada", 30, "UK")
println(summon[Show[Person]].show(p2))
```

Output: `Person(name=Ada, age=30)`
The country is missing from the output!

Solution : Edit the Show instance to include the new field

```scala
given Show[Person] with
  def show(p: Person): String =
    s"Person(name=${p.name}, age=${p.age}, country=${p.country})"
```

Now imagine you have 500 new field to add.

## (Continued) Derived show implementation

```scala

```

Usage

```scala
case class Person(name: String, age: Int) derives Show
val p = Person("Ada", 30)
println(summon[Show[Person]].show(p))
// Person(Ada, 30)
```

Now you add a field

```scala
case class Person(name: String, age: Int, country: String) derives Show
val p = Person("Ada", 30, "UK")
println(summon[Show[Person]].show(p))
// Person(Ada, 30, UK)
```

Done~

---

## Option

`Option[A]` is a container that represents "maybe a value" &rarr; either Some(a:A) when a value exists, or None when it doesn't.

It is Scala's safe alternative to null and expresses optionality in the type system.

```scala
val s: Option[String] = Some("hello")
val n: Option[String] = None
val maybe = Option(null)    // => None
val also = Option("world")  // => Some("world")
```

#### Uses of Option

Replacing null

```scala
// before:
val name: String = System.getenv("NAME")
val display = if (name!=null) name else "Guest"

// Option
val nameOpt: Option[String] = Option(System.getenv("NAME"))
val display = nameOpt.getOrElse("Guest")
```

---

## Applicative Functor
