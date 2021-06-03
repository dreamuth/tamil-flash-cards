package tamil.sound

import QuestionState
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.px
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledButton
import styled.styledDiv

external interface TamilLevel3Props : RProps {
    var questionState: QuestionState
    var onNextClick: () -> Unit
}

class TamilLevel3 : RComponent<TamilLevel3Props, RState>() {
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
                        classes = mutableListOf("btn btn-warning w-100")
                        fontSize = 80.px
                        height = 250.px
                    }
                    +props.questionState.tamilState.getAnswer()
                }
            }
        }
    }
}

fun RBuilder.tamilLevel3(handler: TamilLevel3Props.() -> Unit): ReactElement {
    return child(TamilLevel3::class) {
        this.attrs(handler)
    }
}
