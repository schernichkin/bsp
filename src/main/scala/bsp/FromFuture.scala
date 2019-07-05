package bsp

import simulacrum.typeclass

import scala.concurrent.Future

@typeclass trait FromFuture[F[_]] {
  def fromFuture[A](future: Future[A]): F[A]
}
