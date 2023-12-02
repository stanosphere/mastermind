package mastermind.algorithms

import cats.data.State
import cats.implicits._
import mastermind.HiddenCode
import mastermind.model.Peg._
import mastermind.model.{Code, CodeBreakResult, Feedback}

case object BruteForce extends Algorithm {

  case class GameState(possibilities: Set[Code], currentGuess: Code, guessesMade: Int)

  def breakCode(hiddenCode: HiddenCode): CodeBreakResult = {
    val firstGuess   = Code(White, White, Black, Black)
    val initialState = GameState(Code.allPossibleCodes, firstGuess, 0)

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
    gs.copy(possibilities = gs.possibilities.filter(possibility => {
      val feedbackForThisPossibility = HiddenCode(possibility).giveFeedBack(guess)
      feedback == feedbackForThisPossibility
    }))

  def getNextGuess(gs: GameState): GameState =
    gs.copy(currentGuess = gs.possibilities.head)

  def incrementGuessCounter(gs: GameState): GameState =
    gs.copy(guessesMade = gs.guessesMade + 1)

}
