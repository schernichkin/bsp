package bsp.engine.akka

import akka.actor._
import akka.util.Timeout
import bsp.FromFuture
import cats._
import cats.effect._

import scala.concurrent.ExecutionContext

case class LocalProcess[F[_] : Applicative : LiftIO : FromFuture](
                                                                   workerCount: Int = Runtime.getRuntime.availableProcessors()
                                                                 )
                                                                 (
                                                                   implicit actorSystem: ActorSystem,
                                                                   timeout: Timeout,
                                                                   executionContext: ExecutionContext
                                                                 ) extends AkkaProcess[F] {
  override protected def acquireWorkers(): Seq[ActorRef] = Seq.fill(workerCount) {
    actorSystem.actorOf(Props(classOf[WorkerActor]))
  }
}