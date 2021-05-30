import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH6

external interface StatusPageProps : RProps {
    var timerState: TimerState
}

class StatusPage : RComponent<StatusPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("w-100")
            }
            styledDiv {
                css {
                    classes = mutableListOf("alert alert-primary text-left mb-0 p-2 w-100 rounded-0")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("row")
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                        val time = props.timerState.time
                        val displayValue = "Time: ${time / 60 % 60} : ${time % 60}"
                        styledH6 {
                            +displayValue
                        }
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                        val count = props.timerState.count
                        val total = props.timerState.total
                        val displayValue = "Points: $count/$total"
                        styledH6 {
                            css {
                                classes = mutableListOf("text-end")
                            }
                            +displayValue
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.statusPage(handler: StatusPageProps.() -> Unit): ReactElement {
    return child(StatusPage::class) {
        this.attrs(handler)
    }
}
