package bsp.engine.akka

/**
  * Сообщения, обрабатываемые воркером.
  */
sealed trait WorkerMessage

final case class Run[A](task: WorkerTask[A]) extends WorkerMessage