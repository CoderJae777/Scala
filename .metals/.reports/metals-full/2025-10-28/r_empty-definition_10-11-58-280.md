error id: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Basics.scala:`<none>`.
file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Basics.scala
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
offset: 1667
uri: file:///C:/SUTD/2025_Term7/Scala/src/main/scala/Basics.scala
text:
```scala
// Reference to this https://www.youtube.com/watch?v=F90hJltCPEI&list=PLmtsMNDRU0BxryRX4wiwrTZ661xcp6VPM&index=2

// everything inside this app will be executed
object Basics extends App {
  println("Hello, Rock the JVM!")

  // immutable value
  val meaningofLife: Int = 42
  // java equivalent
  // const int meaningofLifeJava = 42;

  // Datatype declaration is usually optional
  // Scala usually knows the type already
  val truth = true
  println(truth.getClass.getName)

  val aString = "I am a nig"
  val anotherString = "ga"
  val anotheranotherString = aString + anotherString
  println(anotheranotherString)

  val number1 = 1
  val number2 = 2
  val number3 = number1 + number2
  println(number3)
  val number4 = 3 + number1
  println(number4)

  // If expression --> if else in Scala
  val gay = 50
  val chainedIfExpression =
    if (gay > 43) "yes"
    else if (gay < 0) "-2"
    else ""
  println(chainedIfExpression)

  val aCodeBlock = {
    val aLocalvalue = 67
    aLocalvalue + 3
  }
  println(aCodeBlock)

  // functions
  def nameOfFunction(x: Int, y: String): String = {
    y + "" + x
  }
  println(nameOfFunction(5, "The number is: "))

  // factoprial function
  def factorial(n: Int): Int = {
    if (n <= 1) 1
    else n * factorial(n - 1)
  }

  println(factorial(5))
  /*
  facotrial(5) = 5 * factorial(4)
  factorial(4) = 4 * factorial(3)
  factorial(3) = 3 * factorial(2) 
  factorial(2) = 2 * factorial(1)
  factorial(1) = 1
   */

  // Non meaning value - Unit
  // Type of Side Effects --> nothing to do with the value it returns
  def returnUnit(): Unit = {
    pr@@intln("Returning Unit")
  }

  returnUnit()
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.