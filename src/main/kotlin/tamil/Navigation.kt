package tamil

import QuestionState
import TamilLevel
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

external interface NavigationProps : RProps {
    var questionState: QuestionState
    var onPreviousClick: () -> Unit
    var onNextClick: () -> Unit
}

class Navigation : RComponent<NavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-0")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("d-flex")
                    }
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-success m-1 flex-fill")
                            fontSize = 30.px
                            height = 80.px
                        }
                        attrs {
                            disabled = !props.questionState.tamilState.hasPrevious()
                            onClickFunction = {
                                props.onPreviousClick()
                            }
                        }
                        +"Back"
                    }
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-success m-1 flex-fill")
                            fontSize = 30.px
                            height = 80.px
                        }
                        val disabledState = when (props.questionState.selectedTamilLevel) {
                            TamilLevel.LEVEL_I -> !props.questionState.tamilState.hasNext() || !props.questionState.showAnswer
                            TamilLevel.LEVEL_II -> !props.questionState.tamilState.hasNext()
                        }
                        attrs {
                            disabled = disabledState
                            onClickFunction = {
                                props.onNextClick()
                            }
                        }
                        +"Next"
                    }
                }
            }
        }
    }
}

fun RBuilder.navigation(handler: NavigationProps.() -> Unit): ReactElement {
    return child(Navigation::class) {
        this.attrs(handler)
    }
}
