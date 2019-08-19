package example

import example.modules.{About, Home, Layout}
import example.services.AppCircuit
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom.document
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object ScalaJSExample {

  sealed trait Loc
  case object HomeLoc extends Loc
  case object AboutLoc extends Loc

  @JSImport("bootstrap", JSImport.Default)
  @js.native
  object Bootstrap extends js.Object

  def main(args: Array[String]): Unit = {
    val target = document.getElementById("main")

    Bootstrap

    val homeWrapper = AppCircuit.connect(identity(_))

    val routerConfig = RouterConfigDsl[Loc].buildConfig { dsl =>
      import dsl._

      (emptyRule
        | staticRoute(root, HomeLoc) ~> render(homeWrapper(Home(_)))
        | staticRoute("#about", AboutLoc) ~> render(About())
        )
        .notFound(redirectToPage(HomeLoc)(Redirect.Replace))
        .setTitle(p => s"PAGE = $p | Example App")
    }.renderWith(Layout.apply)

    val router = Router(BaseUrl.until_#, routerConfig)
    router().renderIntoDOM(target)
  }
}
