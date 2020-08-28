package mastermind

import better.files._
import mastermind.model.{Code, CodeBreakResult, Feedback, FeedbackPeg}
import cats.syntax.traverse._
import cats.instances.list._
import cats.instances.either._
import mastermind.typeclasses.MapTypeclasses._
import StatsSummary._
import cats.syntax.show._

object InspectResults {
  val resultDir = "out/mastermind/"

  println(Feedback.allPossibleFeedbacks)

  val q = Set(1,2,3)

  val p = (for {
  a <- q
  b <- q
  c <- q
  d <- q
  e <- q
  f <- q
  g <- q
  } yield List(a,b,c,d,e,f,g))

  println(p.map(_.sorted).size)

  def main(args: Array[String]): Unit = {
    // yes I am being naughty with my gets
    val knuthResults      = outputCsvToSummary(File(resultDir + "knuth.csv")).toOption.get
    val bruteForceResults = outputCsvToSummary(File(resultDir + "brute-force.csv")).toOption.get

    printResults("knuth",knuthResults)
    printResults("brute-force",bruteForceResults)
  }

  private def printResults(algorithmName:String,results: Map[Int,Int] ): Unit = {
    println(s"Results for $algorithmName algorithm:")
    println(results.show)
    println(StatsSummary(results).show)
  }

  private def outputCsvToSummary(file: File): Either[String, Map[Int, Int]] =
    file
      .newScanner(StringSplitter.regex("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)"))
      .map(_.trim)
      .map(_.replaceAll("\"", ""))
      .grouped(2)
      .map(lineToCodeBreakResult)
      .iterator
      .toList
      .sequence
      .map(_.groupMapReduce(_.numberOfGuessesUsed)(_ => 1)(_ + _))

  private def lineToCodeBreakResult(line: Seq[String]): Either[String, CodeBreakResult] =
    Code.fromString(line.head).map(CodeBreakResult(_, line(1).toInt))

}
