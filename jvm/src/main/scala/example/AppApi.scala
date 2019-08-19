package example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.actor.ActorSystem
import akka.event.Logging
import scala.util.Random
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

trait AppApi {
  implicit def system: ActorSystem
  def assetsPath: String

  val log =  Logging(system.eventStream, "api")

  val routes: Route = cors() {
    getStatic ~
    jsonExample
  }

  def getStatic: Route = get {
    pathSingleSlash {
      getFromResource("index.html")
    } ~
    (path("favicon.ico") & get) {
      getFromResource("favicon.ico")
    } ~
    pathPrefix("front-res") {
      getFromResourceDirectory("front-res")
    } ~
    pathPrefix("assets") {
      getFromDirectory(assetsPath)
    }
  }

  def jsonExample = {
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.extras.auto._
    import example.shared.Dto._
    val data = Foo(123): Event

    pathPrefix("json") {
      get {
        path("random") {
          complete(Foo(Random.nextInt(100)): Event)
        } ~
        complete(data)
      } ~
      post {  // httpie: http localhost:8080/json s=hello eventType=Bar
        entity(as[Event]) { event =>
          log.info(event.toString)
          complete("ok")
        }
      }
    }
  }
}

class AppApiImpl(
  val assetsPath: String,
  val system: ActorSystem
) extends AppApi

object AppApi {
  def apply(assetsPath: String, system: ActorSystem): AppApi = new AppApiImpl(assetsPath, system)
}
