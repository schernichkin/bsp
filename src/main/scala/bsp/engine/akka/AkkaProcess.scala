package bsp.engine.akka

import akka.actor.{ActorRef, PoisonPill}
import akka.util.Timeout
import bsp._
import cats._
import cats.effect.{LiftIO, Resource}

import scala.concurrent.ExecutionContext

abstract class AkkaProcess[F[_] : Applicative : LiftIO : FromFuture](
                                                                      implicit timeout: Timeout, executionContext: ExecutionContext
                                                                    ) extends bsp.Process[F] {
  protected def acquireWorkers(): Seq[ActorRef]

  protected def releaseWorkers(workers: Seq[ActorRef]): Unit = workers.foreach(_ ! PoisonPill)

  override def workers: Resource[F, Seq[Worker[F]]] = Resource.make {
    io(acquireWorkers())
  } { ws =>
    io(releaseWorkers(ws))
  } map {
    _.map(WorkerRef[F])
  }
}
