package mastermind

import mastermind.algorithms.{Algorithm, BruteForce, Knuth}
import mastermind.model.{Code, CodeBreakResult}
import better.files._

object RunAlgorithm {

  def main(args: Array[String]): Unit =
    args.headOption match {
      case Some("knuth")       => go(Knuth, outputDir + "knuth.csv")
      case Some("brute-force") => go(BruteForce, outputDir + "brute-force.csv")
      case _                   => println(errorMessage)
    }

  val outputDir    = "out/mastermind/"
  val errorMessage = "please specify one of the algorithms found here: either `knuth` or `brute-force`"

  def go(algorithm: Algorithm, path: String): Unit = {
    deleteFileContents(path)
    solveAllPossibleCodes(algorithm, path)
  }

  def deleteFileContents(path: String): Unit =
    File(path).clear()

  def solveAllPossibleCodes(algorithm: Algorithm, path: String): Unit =
    Code.allPossibleCodes.iterator
      .map(HiddenCode(_))
      .map(algorithm.breakCode)
      .map(x => { println(x); x })
      .map { case CodeBreakResult(code, numberOfGuessesUsed) => s""""$code", $numberOfGuessesUsed""" }
      .foreach(File(path).appendLine)

}
