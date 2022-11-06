import csstype.px
import csstype.rgb
import react.FC
import react.Props
import emotion.react.css
import kotlinx.browser.document
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.input
import react.useState

external interface WelcomeProps : Props {
    var name: String
}

val Welcome = FC<WelcomeProps> { props ->
    var name by useState(props.name)
    div {
        id = "name-holder-div"
        css {
            padding = 5.px
            backgroundColor = rgb(8, 97, 22)
            color = rgb(56, 246, 137)
        }
        +"Hello, ${name.reversed()}"
    }
    input {
        css {
            marginTop = 5.px
            marginBottom = 5.px
            fontSize = 14.px
        }
        type = InputType.text
        value = name
        onChange = { event ->
            name = event.target.value
        }
    }
    div {
        img {
            src = "https://placekitten.com/408/287"
        }
    }

    div {
        id = "change-text-button"
        button {
            onClick = {
                name = name.reversed()
            }
            +"Change name"

        }
    }
}
