package mastermind

import mastermind.algorithms.BruteForce
import mastermind.model.Code

object Main extends App {

  val numberOfCodesByGuessesUsed =
    Code.allPossibleCodes.toList
      .map(Answer(_))
      .map(new BruteForce(_))
      .map(_.breakCode())
      .groupMapReduce(_.numberOfGuessesUsed)(_ => 1)(_ + _)

  println(numberOfCodesByGuessesUsed)

}
