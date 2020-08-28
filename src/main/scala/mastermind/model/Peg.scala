package mastermind.model

import cats.implicits._

sealed trait Peg

object Peg {

  final case object White  extends Peg
  final case object Black  extends Peg
  final case object Red    extends Peg
  final case object Yellow extends Peg
  final case object Blue   extends Peg
  final case object Green  extends Peg

  def fromString(s: String): Either[String, Peg] =
    s match {
      case "White"  => White.asRight
      case "Black"  => Black.asRight
      case "Red"    => Red.asRight
      case "Yellow" => Yellow.asRight
      case "Blue"   => Blue.asRight
      case "Green"  => Green.asRight
      case _        => s"sorry $s is not a valid peg".asLeft
    }
}
