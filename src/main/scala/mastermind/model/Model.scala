package mastermind.model

import cats.implicits._
import mastermind.model.Peg._

case class Code(a: Peg, b: Peg, c: Peg, d: Peg) {
  def toList: List[Peg] = List(a, b, c, d)
}

object Code {
  val allPegs =
    Set(White, Black, Red, Yellow, Blue, Green)

  val allPossibleCodes: Set[Code] =
    for {
      a <- allPegs
      b <- allPegs
      c <- allPegs
      d <- allPegs
    } yield Code(a, b, c, d)

  def fromString(s: String): Either[String, Code] =
    (s match {
      case s"Code($a,$b,$c,$d)" => List(a, b, c, d).asRight
      case _                    => s"$s is not a valid Code sorry!".asLeft
    }) >>= (_.map(Peg.fromString).sequence) >>= {
      case List(a, b, c, d) => Code(a, b, c, d).asRight
      case _                => s"$s is not a valid Code sorry!".asLeft
    }
}

case class Feedback(values: Map[FeedbackPeg, Int])

object Feedback {
  val feedBackForCorrectAnswer: Feedback  =
    Feedback(Map(FeedbackPeg.Black -> 4, FeedbackPeg.White -> 0))

  // 15 such possibilities
  val allPossibleFeedbacks: Set[Feedback] =
    (for {
      blackCount <- 0 to 4
      whiteCount <- 0 to 4
    } yield Feedback(Map(FeedbackPeg.Black -> blackCount, FeedbackPeg.White -> whiteCount)))
      .filter(_.values.values.sum <= 4)
      .toSet
}

case class CodeBreakResult(code: Code, numberOfGuessesUsed: Int)
