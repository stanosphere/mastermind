package mastermind.algorithms

import mastermind.Answer
import mastermind.model.CodeBreakResult

trait Algorithm {
  val answer: Answer
  def breakCode(): CodeBreakResult
}
