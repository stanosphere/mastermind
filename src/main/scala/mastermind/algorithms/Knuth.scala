package mastermind.algorithms

import mastermind.HiddenCode
import mastermind.model.Peg._
import mastermind.model.{Code, CodeBreakResult, Feedback}
import cats.data.State
import cats.implicits._

case object Knuth extends Algorithm {

  case class GameState(
      possibilities: Set[Code],
      currentGuess: Code,
      guessesMade: Int,
      usedGuesses: Set[Code]
  )

  def breakCode(hiddenCode: HiddenCode): CodeBreakResult = {
    val firstGuess   = Code(White, White, Black, Black)
    val initialState = GameState(Code.allPossibleCodes, firstGuess, 0, Set.empty[Code])

    val calc = for {
      _   <- State.set(initialState)
      _   <- doSingleTurn(hiddenCode).iterateUntil(_ == Feedback.feedBackForCorrectAnswer)
      ans <- State.get
    } yield CodeBreakResult(ans.currentGuess, ans.guessesMade)

    calc.run(initialState).value._2
  }

  def doSingleTurn(hiddenCode: HiddenCode): State[GameState, Feedback] =
    for {
      gs      <- State.get[GameState]
      feedback = hiddenCode.giveFeedBack(gs.currentGuess)
      _       <- State.modify[GameState](incrementGuessCounter)
      _       <- State.modify[GameState](eliminatePossibilities(gs.currentGuess, feedback, _))
      _       <- State.modify[GameState](getNextGuess)
    } yield feedback

  // TODO maybe use lensing for the state transition functions?
  def eliminatePossibilities(guess: Code, feedback: Feedback, gs: GameState): GameState =
    gs.copy(possibilities = eliminatePossibilities(guess, feedback, gs.possibilities))

  def eliminatePossibilities(guess: Code, feedback: Feedback, possibilities: Set[Code]): Set[Code] =
    possibilities.filter(possibility => {
      val feedbackForThisPossibility = HiddenCode(possibility).giveFeedBack(guess)
      feedback == feedbackForThisPossibility
    })

  // TODO: implement Knuth's technique here otherwise this is identical to the brute force method lol
  def getNextGuess(gs: GameState): GameState = {
    val unused              = (Code.allPossibleCodes -- gs.usedGuesses).toList
    val scores              = unused.map(guess => guess -> getScoreForGuess(guess, gs.possibilities))
    val maxScore            = scores.map { case (_, score) => score }.max
    val guessesWithMaxScore = scores.filter { case (_, score) => score == maxScore }.map(_._1)

    val nextGuess = chooseGuessFromGuessesInMaxScore(guessesWithMaxScore, gs.possibilities)

    gs.copy(currentGuess = nextGuess)
  }

  // ideally we would like to use something that is in the set of remaining possibilities
  // however this is not always possible hence this function!
  def chooseGuessFromGuessesInMaxScore(guessesWithMaxScore: List[Code], possibilities: Set[Code]): Code =
    guessesWithMaxScore.find(possibilities.contains).getOrElse(guessesWithMaxScore.head)

  // the score for each guess is the minimum number of possibilities that will be eliminated
  def getScoreForGuess(guess: Code, possibilities: Set[Code]): Int =
    Feedback.allPossibleFeedbacks.toList
      .map(getNumberThatWouldBeEliminated(guess, _, possibilities))
      .min

  def getNumberThatWouldBeEliminated(guess: Code, feedback: Feedback, possibilities: Set[Code]): Int =
    possibilities.size - eliminatePossibilities(guess, feedback, possibilities).size

  def incrementGuessCounter(gs: GameState): GameState =
    gs.copy(guessesMade = gs.guessesMade + 1)

}
