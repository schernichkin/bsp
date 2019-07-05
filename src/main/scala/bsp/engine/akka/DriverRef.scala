package bsp.engine.akka

import akka.actor._
import bsp._
import cats.effect._

case class DriverRef[F[_] : LiftIO](driverRef: ActorRef) extends Driver[F] {
  /**
    * Информировать драйвер об окончании работы программы.
    */
  override def cancelled: F[Unit] = LiftIO[F].liftIO {
    IO {
      driverRef ! Canceled
    }
  }

  override def completed: F[Unit] = LiftIO[F].liftIO {
    IO {
      driverRef ! Completed
    }
  }

  /**
    * Отправить драйверу информацию об ошибке.
    */
  override def error(e: Throwable): F[Unit] = LiftIO[F].liftIO {
    IO {
      driverRef ! Error(e)
    }
  }
}
