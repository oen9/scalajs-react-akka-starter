package example.modules

import example.ScalaJSExample.{AboutLoc, HomeLoc, Loc}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^._

object Layout {

  case class MenuItem(idx: Int, label: String, location: Loc)

  case class Props(router: RouterCtl[Loc], resolution: Resolution[Loc])

  val menuItems = Seq(
    MenuItem(0, "Home", HomeLoc),
    MenuItem(1, "About", AboutLoc)
  )
  def nav(props: Props) =
    <.div(^.cls := "navbar navbar-expand-md navbar-dark bg-dark",
      props.router.link(HomeLoc)(
        ^.cls := "navbar-brand",
        <.img(^.src := "front-res/img/logo-mini.png"),
        " react-akka"
      ),
      <.button(^.cls := "navbar-toggler", ^.tpe := "button", VdomAttr("data-toggle") := "collapse", VdomAttr("data-target") := "#navbarNav", ^.aria.controls := "navbarNav", ^.aria.expanded := "false", ^.aria.label := "Toggle navigation",
        <.span(^.cls := "navbar-toggler-icon")
      ),
      <.div(^.cls := "collapse navbar-collapse", ^.id := "navbarNav",
        <.ul(^.cls := "navbar-nav mr-auto",
          menuItems.map(item =>
            <.li(^.key := item.idx, ^.cls := "nav-item", (^.cls := "active").when(props.resolution.page == item.location),
              props.router.link(item.location)(^.cls := "nav-link", item.label)
            )
          ).toVdomArray
        )
      )
    )

  def contentBody(props: Props) = props.resolution.render()

  def footer(props: Props) =
    <.div(^.cls := "footer bg-dark text-white d-flex justify-content-center mt-auto py-3",
      "© 2019 oen"
    )

  val component = ScalaComponent.builder[Props]("Layout")
    .render_P(props => {
      React.Fragment(
        nav(props),
        <.div(^.cls := "container",
          <.div(^.cls := "main-content mt-5", ^.role := "main", contentBody(props))
        ),
        footer(props)
      )
    })
    .build

  def apply(ctl: RouterCtl[Loc], resolution: Resolution[Loc]) = component(Props(ctl, resolution))
}
