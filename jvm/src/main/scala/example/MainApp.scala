package example

import cats.effect._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.util.Success
import scala.util.Failure
import akka.event.Logging
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.Http

object MainApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = for { // I just like cats and fp
    _ <- createServer[IO]()
  } yield ExitCode.Success

  def createServer[F[_] : Effect]() = Effect[F].delay {
    val config = ConfigFactory.load()

    val host = config.getString("http.host")
    val port = config.getInt("http.port")
    val assets = config.getString("assets")

    implicit val system = ActorSystem("app", config)
    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    val api = AppApi(assets, system)
    val bindingFuture = Http().bindAndHandle(api.routes, host, port)

    val log =  Logging(system.eventStream, "app-service")

    bindingFuture.onComplete {
      case Success(serverBinding) =>
        log.info("Bound to {}", serverBinding.localAddress)

      case  Failure(t) =>
        log.error(t, "Failed to bind to {}:{}!", host, port)
        system.terminate()
    }
  }
}
