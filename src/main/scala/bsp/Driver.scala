package bsp

class Driver[F[_], G[_]] {
  //TODO: нужно создать список WorkerInterface, вызвать на всех run, дождаться результата.
  // каждый worker должен получить DriverInterface для общения с драйвером.

  def run(program: G[Unit]): F[Unit] = {
    ???
  }
}