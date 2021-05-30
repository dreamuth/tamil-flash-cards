package english

import kotlinx.css.fontSize
import kotlinx.css.height
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

external interface QuestionProps : RProps {
    var displayValue: String
    var onNextClick: () -> Unit
}

class Question : RComponent<QuestionProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-1")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledButton {
                    css {
                        classes = mutableListOf("btn btn-success w-100")
                        fontSize = 80.px
                        height = 250.px
                    }
                    attrs {
                        onClickFunction = {
                            props.onNextClick()
                        }
                    }
                    +props.displayValue
                }
            }
        }
    }
}

fun RBuilder.question(handler: QuestionProps.() -> Unit): ReactElement {
    return child(Question::class) {
        this.attrs(handler)
    }
}
