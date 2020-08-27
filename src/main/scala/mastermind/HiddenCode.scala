package mastermind

import mastermind.model.{Code, Feedback, FeedbackPeg}
import HiddenCode._

trait HiddenCode {
  // protected so that the algorithms can't just read off the answer lol
  protected val answer: Code

  def giveFeedBack(guess: Code): Feedback = {
    val numberInCorrectPosition =
      getNumberOfElementsInSamePosition(guess.toList, answer.toList)

    val numberOfCorrectColoursInIncorrectPositions =
      getHits(listToBag(guess.toList), listToBag(answer.toList)) - numberInCorrectPosition

    Feedback(
      Map(
        FeedbackPeg.Black -> numberInCorrectPosition,
        FeedbackPeg.White -> numberOfCorrectColoursInIncorrectPositions
      )
    )
  }

}

object HiddenCode {
  def apply(guess: Code): HiddenCode =
    new HiddenCode {
      override protected val answer: Code = guess
    }

  // https://en.wikipedia.org/wiki/Multiset so called because of this
  type Bag[A] = Map[A, Int]

  def getNumberOfElementsInSamePosition[A](xs: List[A], ys: List[A]): Int =
    (xs zip ys) count { case (x, y) => x == y }

  def getHits[A](xs: Bag[A], ys: Bag[A]): Int                             =
    (xs.keySet intersect ys.keySet).toList.map(a => math.min(xs(a), ys(a))).sum

  def listToBag[A](as: List[A]): Bag[A] =
    as.groupMapReduce(identity)(_ => 1)(_ + _)

}
