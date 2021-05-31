package components

import kotlinx.css.fontSize
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledButton
import styled.styledDiv

external interface ButtonGroupProps : RProps {
    var selected: String
    var allValues: List<String>
    var onButtonClick: (String) -> Unit
}

class ButtonGroup : RComponent<ButtonGroupProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("btn-group p-2 w-100")
            }
            for (buttonName in props.allValues) {
                val isActive = if (props.selected == buttonName) "active" else ""
                styledButton {
                    css {
                        classes = mutableListOf("btn btn-outline-primary $isActive")
                        fontSize = 20.px
                    }
                    attrs {
                        onClickFunction = {
                            props.onButtonClick(buttonName)
                        }
                    }
                    +buttonName
                }
            }
        }
    }
}

fun RBuilder.buttonGroup(handler: ButtonGroupProps.() -> Unit): ReactElement {
    return child(ButtonGroup::class) {
        this.attrs(handler)
    }
}
