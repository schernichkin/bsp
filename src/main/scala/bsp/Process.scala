package bsp

import cats.effect.Resource
import simulacrum.typeclass

@typeclass trait Process[F[_]] {
  def workers: Resource[F, Seq[Worker[F]]]
}