package bsp.akka

import akka.actor._
import cats._
import cats.data._
import cats.effect._

case class LocalDriver[F[_] : LiftIO : Functor, G[_], Worker](workerCount: Int)
                                                             (implicit actorSystem: ActorSystem) extends AkkaDriver[F, G] {
  if (workerCount < 1) throw new IllegalArgumentException("Worker count should be grater than zero")

  private def createWorkerActor(program: G[Unit])(workerId: Int): ActorRef =
    actorSystem.actorOf(Props(classOf[Worker], workerId, program))

  private def acquireWorkers(program: G[Unit]): F[NonEmptyList[ActorRef]] = LiftIO[F].liftIO {
    IO {
      NonEmptyList.fromListUnsafe(List.tabulate(workerCount)(createWorkerActor(program)))
    }
  }

  private def releaseWorkers(workers: NonEmptyList[ActorRef]): F[Unit] = LiftIO[F].liftIO {
    IO {
      workers.toList.foreach(_ ! PoisonPill)
    }
  }

  override def workers(program: G[Unit]): Resource[F, NonEmptyList[ActorRef]] =
    Resource.make(acquireWorkers(program))(releaseWorkers)
}
