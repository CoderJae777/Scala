// ------------------------ //
// Uncomment EVERTHING to run
// Ctrl A + Ctrl / to uncomment all
// ------------------------ //




// Minimal definitions to make the parser combinator example runnable

sealed trait Result[+A]
case class Ok[A](value: A) extends Result[A]
case class Failed(msg: String) extends Result[Nothing]

// Token types
sealed trait LToken
case object XTok extends LToken
case object YTok extends LToken

// AST types
sealed trait T
case object XX extends T
case object YX extends T

// Parser combinator --> main boilerplate for Parser
// T --> type of tokens the parser will process
// A --> type of value the parser will produce if it succeeds e.g. T, LToken, Int
// p is a function that takes a List[T] and returns a Result[(A, List[T])]
// Ok(a,toks1) means success with value a and remaining tokens toks1
// Failed(err) means failure with error message err
// This lets every parser report both the data it produced and
// the tokens it has yet to process.

// map is a method to transform the result of the parser
// [B] is the type of the new value after transformation
// f: A=>B is a function that transforms A to B
// returns a new Parser that applies f to the result of the original parser if it succeeds
case class Parser[T, A](p: List[T] => Result[(A, List[T])]) {
  def map[B](f: A => B): Parser[T, B] = Parser { toks =>
    p(toks) match {
      case Failed(err)    => Failed(err)
      case Ok((a, toks1)) => Ok((f(a), toks1))
    }
  }

  // flatMap is a method to chain parsers
  // [B] is the type of value produced by the next parser
  // f: A => Parser[T, B] is a function that takes the result of the first parser
  // and returns a new parser that produces a value of type B
  def flatMap[B](f: A => Parser[T, B]): Parser[T, B] = Parser { toks =>
    p(toks) match {
      case Failed(err)    => Failed(err)
      case Ok((a, toks1)) => f(a).p(toks1)
    }
  }
}



object Parsec extends App {



  // Primitive parsers for tokens
  // It looks at the head of the input list;
  // if that head is an XTok, it succeeds and returns both the matched token and the tail, otherwise it fails.
  def xTok: Parser[LToken, LToken] =
    Parser {
      case XTok :: rest => Ok((XTok, rest))
      case _            => Failed("Expected XTok")
    }

  def yTok: Parser[LToken, LToken] =
    Parser {
      case YTok :: rest => Ok((YTok, rest))
      case _            => Failed("Expected YTok")
    }

  // T ::= xx
  // this parser tries to match two consecutive XTok tokens
  def parseXX: Parser[LToken, T] =
    for {
      _ <- xTok
      _ <- xTok
    } yield XX

  // T ::= yx
  def parseYX: Parser[LToken, T] =
    for {
      _ <- yTok
      _ <- xTok
    } yield YX

  def or[A](p1: Parser[LToken, A], p2: Parser[LToken, A]): Parser[LToken, A] =
    Parser { toks =>
      p1.p(toks) match {
        case Failed(_) => p2.p(toks)
        case ok        => ok
      }
    }

  // choice combinator: tries each parser in order, returns first success or last failure
  def choice[A](parsers: List[Parser[LToken, A]]): Parser[LToken, A] =
    parsers.reduceLeft(or)

  val parseT: Parser[LToken, T] = choice(List(parseXX, parseYX))

  // Example runs
  println(parseT.p(List(XTok, XTok))) // Ok((XX,List()))
  println(parseT.p(List(YTok, XTok))) // Ok((YX,List()))
  println(parseT.p(List(XTok, YTok))) // Failed(...)
  println(parseT.p(List(YTok, YTok))) // Failed(...)
}

// Grammar
// X ::= XXa | Y
// Y ::= Yb | c

// Step 1 : Flatten
// X ::= XXa
// X ::= Y
// Y ::= Yb
// Y ::= c

// Step 2 : Eliminate Left Recursion
// X ::= XXa | Y
// Y ::= cY'
// Y' ::= bY' | ε

// Step 3 : Eliminate left recursion for X
// X  ::= YX'
// X' ::= XaX'
// X' ::= ε
// Y  ::= cY'
// Y' ::= bY'
// Y' ::= ε

// !Left recursion is only when the leftmost symbol is the same as the non-terminal being defined! //
// i.e. X ::= Xa... is left recursive because it starts with X
// but X ::= YX is not left recursive because it starts with Y although it ends with X
