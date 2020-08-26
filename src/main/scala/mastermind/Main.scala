package mastermind

import mastermind.algorithms.{Algorithm, BruteForce, Knuth}
import mastermind.model.{Code, Feedback}
import mastermind.typeclasses.MapTypeclasses._
import cats.syntax.show._

object Main extends App {

  val numberOfCodesByGuessesUsed =
    Code.allPossibleCodes.iterator.take(5)
      .map(Answer(_))
      .map(Knuth)
      .map(_.breakCode())
      .toList
      .groupMapReduce(_.numberOfGuessesUsed)(_ => 1)(_ + _)
      .show

  println(numberOfCodesByGuessesUsed)

}
