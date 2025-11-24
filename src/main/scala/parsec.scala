
object Parsec {
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
  val parseXX: Parser[LToken, T] =
    for {
      _ <- xTok
      _ <- xTok
    } yield T.XX

// T ::= yx
  val parseYX: Parser[LToken, T] =
    for {
      _ <- yTok
      _ <- xTok
    } yield T.YX
  def or[A](p1: Parser[LToken, A], p2: Parser[LToken, A]): Parser[LToken, A] =
    Parser { toks =>
      p1.p(toks) match {
        case Failed(_) => p2.p(toks)
        case ok        => ok
      }
    }

  val parseT: Parser[LToken, T] = or(parseXX, parseYX)
  val result = parseT.p(List(XTok, XTok)) // Ok((T.XX, List()))
  val result2 = parseT.p(List(YTok, XTok)) // Ok((T.YX, List()))
  val result3 = parseT.p(List(XTok, YTok)) // Failed(...)
}
