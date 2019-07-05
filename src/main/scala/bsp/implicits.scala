package bsp

import cats._
import cats.data._

object implicits {
  implicit def driverForWorkerT[F[_]]: Driver[WorkerT[F, ?]] = new Driver[WorkerT[F, ?]] {
    /**
      * Информировать драйвер об окончании работы программы.
      */
    override def cancelled: WorkerT[F, Unit] = ???

    override def completed: WorkerT[F, Unit] = ???

    /**
      * Отправить драйверу информацию об ошибке.
      */
    override def error(e: Throwable): WorkerT[F, Unit] = ???
  }

  implicit def runnableForWorkerT[G[_] : Runnable]: RunnableT[WorkerT, G] = new RunnableT[WorkerT, G] {
    override def runAsync[A](f: WorkerT[G, A], cb: Either[Throwable, A] => Unit): WorkerT[Id, Unit] = Kleisli[Id, WorkerEnv, Unit] { env =>
      Runnable[G].runAsync(f(env), cb)
    }
  }
}
