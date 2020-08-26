package mastermind.model

sealed trait Peg

object Peg {

  final case object White  extends Peg
  final case object Black  extends Peg
  final case object Red    extends Peg
  final case object Yellow extends Peg
  final case object Blue   extends Peg
  final case object Green  extends Peg

}
