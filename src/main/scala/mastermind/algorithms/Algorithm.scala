package mastermind.algorithms

import mastermind.HiddenCode
import mastermind.model.CodeBreakResult

trait Algorithm {
  def breakCode(a: HiddenCode): CodeBreakResult
}
