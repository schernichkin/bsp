package bsp

import cats.Id

trait RunnableT[F[_[_], _], G[_]] {
  def runAsync[A](f: F[G, A], cb: Either[Throwable, A] => Unit): F[Id, Unit]
}