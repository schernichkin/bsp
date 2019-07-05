package bsp.engine.akka

/**
  * Сообщения, обрабатываемые драйвером.
  */
sealed trait DriverMessage

object Canceled extends  DriverMessage

final case class Completed[A](a: A) extends DriverMessage

final case class Error(e: Throwable) extends DriverMessage