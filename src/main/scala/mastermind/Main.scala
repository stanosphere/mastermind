package mastermind

import mastermind.algorithms.{BruteForce, Knuth}
import mastermind.model.Code
import mastermind.model.Peg._

object Main extends App {

  val answer = Answer(Code(White, Green, Green, Green))

  val knuth      = new Knuth(answer)
  val bruteForce = new BruteForce(answer)

  val res = bruteForce.breakCode()

  println(res)
}
