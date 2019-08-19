package example.modules

import diode.react.ModelProxy
import example.components.BlueButton
import example.services.IncreaseClicks
import example.shared.HelloShared
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import example.services.RootModel
import example.services.TryGetRandom
import diode.react.ReactPot._

object Home {

  case class Props(proxy: ModelProxy[RootModel])

  class Backend($: BackendScope[Props, Unit]) {
    def tick(): Callback = $.props.flatMap(_.proxy.dispatchCB(IncreaseClicks))
    def getRandom(): Callback = $.props.flatMap(_.proxy.dispatchCB(TryGetRandom()))

    def render(props: Props) =
      React.Fragment(
        <.div(^.cls := "text-center",
          "Hello: " + HelloShared.TEST_STR
        ),
        <.div(^.cls := "row mt-2",
          <.div(^.cls := "col text-right", BlueButton(BlueButton.Props("click me!", tick()))),
          <.div(^.cls := "col", " clicks: " + props.proxy().clicks.count)
        ),
        <.div(^.cls := "row mt-2",
          <.div(^.cls := "col text-right", BlueButton(BlueButton.Props("get random from server!", getRandom()))),
          <.div(^.cls := "col", " random: ",
            props.proxy().randomNumber.renderPending(_ =>
                <.div(^.cls := "spinner-border text-primary", ^.role := "status",
                  <.span(^.cls := "sr-only", "Loading...")
                )
            ),
            props.proxy().randomNumber.renderReady(random => random.i),
            props.proxy().randomNumber.renderEmpty("nothing here yet"),
            props.proxy().randomNumber.renderFailed(msg => " error: " + msg.toString),
          )
        )
      )
  }

  val component = ScalaComponent.builder[Props]("Home")
    .renderBackend[Backend]
    .build

  def apply(proxy: ModelProxy[RootModel]) = component(Props(proxy))
}
