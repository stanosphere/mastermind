package mastermind.model

import mastermind.model.Peg.{Black, Blue, Green, Red, White, Yellow}

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
}

case class Feedback(values: Map[FeedbackPeg, Int])

object Feedback {
  val feedBackForCorrectAnswer: Feedback =
    Feedback(Map(FeedbackPeg.Black -> 4, FeedbackPeg.White -> 0))
}
