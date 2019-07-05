import cats.data._
import cats.effect._

package object bsp {
  //TODO: partial application

  type WorkerT[F[_], A] = ReaderT[F, WorkerEnv, A]

  def io[F[_] : LiftIO, A](a: => A): F[A] = LiftIO[F].liftIO(IO(a))
}
