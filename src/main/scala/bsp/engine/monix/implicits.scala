package bsp.engine.monix

import bsp._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future

object implicits {
  implicit val runnableForTask: Runnable[Task] = new Runnable[Task] {
    override def runAsync[A](f: Task[A], cb: Either[Throwable, A] => Unit): Unit = {
      f.runAsync(cb)
    }
  }

  implicit val fromFutureForTask: FromFuture[Task] = new FromFuture[Task] {
    override def fromFuture[A](f: Future[A]): Task[A] = Task.fromFuture(f)
  }
}
