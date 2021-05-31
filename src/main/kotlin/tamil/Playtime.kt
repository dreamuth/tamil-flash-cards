package tamil

import QuestionState
import TimerValues
import components.buttonGroup
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledButton
import styled.styledDiv

external interface PlaytimeProps : RProps {
    var questionState: QuestionState
    var onSelectedTimerValueChange: (TimerValues) -> Unit
    var onStart: () -> Unit
}

class Playtime : RComponent<PlaytimeProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-1 ")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("card  text-center")
                        width = 100.pct
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                        }
                        buttonGroup {
                            allValues = TimerValues.values().map { it.displayValue }
                            selected = props.questionState.selectedTimerValue.displayValue
                            onButtonClick = {
                                val timerValue = TimerValues.fromDisplayValue(it)
                                props.onSelectedTimerValueChange(timerValue)
                            }
                        }
                        styledDiv {
                            css {
                                classes = mutableListOf("btn-group p-2 w-100")
                            }
                            styledButton {
                                css {
                                    classes = mutableListOf("btn btn-success flex-fill w-100")
                                    fontSize = 30.px
                                    height = 50.px
                                }
                                attrs {
                                    onClickFunction = {
                                        props.onStart()
                                    }
                                }
                                +"Start"
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.playtime(handler: PlaytimeProps.() -> Unit): ReactElement {
    return child(Playtime::class) {
        this.attrs(handler)
    }
}
