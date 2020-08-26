package mastermind

import mastermind.algorithms.BruteForce
import mastermind.model.Code
import mastermind.typeclasses.MapTypeclasses._
import cats.syntax.show._

object Main extends App {

  val numberOfCodesByGuessesUsed =
    Code.allPossibleCodes.toList
      .map(Answer(_))
      .map(new BruteForce(_))
      .map(_.breakCode())
      .groupMapReduce(_.numberOfGuessesUsed)(_ => 1)(_ + _)
      .show

  println(numberOfCodesByGuessesUsed)

}
