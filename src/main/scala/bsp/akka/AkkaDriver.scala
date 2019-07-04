package bsp.akka

import akka.actor.ActorRef
import simulacrum._
import cats._
import cats.implicits._
import cats.data.NonEmptyList
import cats.effect.{Bracket, Resource}

trait AkkaDriver[F[_], G[_]] {
  def workers(program: G[Unit]): Resource[F, NonEmptyList[ActorRef]]
}

object AkkaDriver {
  private [akka] final class RunPartiallyApplied[F[_]] {
    def apply[G[_]](program: G[Unit])(implicit driver: AkkaDriver[F, G], bracket: Bracket[F, Throwable]): F[Unit] =
      driver.workers(program).use { workers => ().pure[F] }
  }

  def run[F[_]]: RunPartiallyApplied[F] = new RunPartiallyApplied[F]
}