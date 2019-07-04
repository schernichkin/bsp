package bsp

trait WorkerInterface[F[_], G[_]] {
  def run[A](program: G[A]): F[A]
}
