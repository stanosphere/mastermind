package mastermind

import cats.Show

case class StatsSummary(weightedMean: Double, mode: Int, median: Int, worstCase: Int)

object StatsSummary {

  def apply(mastermindResultSummary: Map[Int, Int]): StatsSummary =
    StatsSummary(
      weightedMean = getWeightedMean(mastermindResultSummary),
      mode = getMode(mastermindResultSummary),
      median = getMedian(mastermindResultSummary),
      worstCase = mastermindResultSummary.keySet.max
    )

  def getWeightedMean(map: Map[Int, Int]): Double = {
    println(map.iterator.map { case (a, b) => a * b }.sum, map.values.sum.toDouble)
    map.iterator.map { case (a, b) => a * b }.sum / map.values.sum.toDouble
  }

  def getMode[A](map: Map[A, Int]): A =
    map.maxBy { case (_, occurrences) => occurrences }._1

  def getMedian[A: Ordering](map: Map[A, Int]): A = {

    case class Accumulator(runningTotal: Int, res: Option[A])

    val total  = map.values.sum
    val sorted = map.toList.sortBy { case (a, _) => a }

    sorted
      .foldLeft(Accumulator(0, None)) { (acc, elem) =>
        if (acc.res.isDefined) acc
        else {
          val (k, v)          = elem
          val newRunningTotal = acc.runningTotal + v
          if (newRunningTotal > total / 2) Accumulator(0, Some(k)) else Accumulator(newRunningTotal, None)
        }
      }
      .res
      .get
  }

  implicit val statsSummaryShow: Show[StatsSummary] =
    (t: StatsSummary) => s"""
        |Weighted Mean -> ${t.weightedMean}
        |Worst Case    -> ${t.worstCase}
        |Median        -> ${t.median}
        |Mode          -> ${t.mode}
        |""".stripMargin

}
