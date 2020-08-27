package mastermind

import mastermind.algorithms.{Algorithm, BruteForce, Knuth}
import mastermind.model.{Code, CodeBreakResult, Feedback}
import mastermind.typeclasses.MapTypeclasses._
import cats.syntax.show._
import better.files._

object Main extends App {

  val path = "out/mastermind/knuth.csv"

  Code.allPossibleCodes.iterator
    .map(Answer(_))
    .map(Knuth)
    .map(_.breakCode())
    .map(look)
    .map { case CodeBreakResult(code, numberOfGuessesUsed) => s""""$code", $numberOfGuessesUsed""" }
    .foreach(File(path).appendLine)
//      .toList
//      .groupMapReduce(_.numberOfGuessesUsed)(_ => 1)(_ + _)

//  numberOfCodesByGuessesUsed.zipWithIndex.foreach(println)

  def look[A](x: A): A = { println(x); x }

}
