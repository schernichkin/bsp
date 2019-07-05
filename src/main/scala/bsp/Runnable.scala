package bsp

import simulacrum.typeclass

@typeclass trait Runnable[F[_]] {
  def runAsync[A](f: F[A], cb: Either[Throwable, A] => Unit): Unit
}
