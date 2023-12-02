package mastermind.model

sealed trait FeedbackPeg

object FeedbackPeg {
  case object Black extends FeedbackPeg
  case object White extends FeedbackPeg
}
