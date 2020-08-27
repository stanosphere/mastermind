package mastermind.algorithms

import mastermind.HiddenCode
import mastermind.model.CodeBreakResult

trait Algorithm extends Product {
  def breakCode(a: HiddenCode): CodeBreakResult
}

//object Algorithm {
//  def a
//}
