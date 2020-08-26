package mastermind.typeclasses

import cats.Show

object MapTypeclasses {

  implicit def mapShow[K: Ordering, V]: Show[Map[K, V]] =
    (t: Map[K, V]) =>
      t.toList
        .sortBy { case (k, _) => k }
        .map { case (k, v) => s"$k -> $v" }
        .mkString("Map(\n  ", ",\n  ", "\n)")

}
