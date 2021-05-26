import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH6
import styled.styledSpan

external interface StatusPageProps: RProps {
    var questionState: QuestionState
}

class StatusPage : RComponent<StatusPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("position-absolute bottom-0 end-0 w-100")
            }
            styledDiv {
                css {
                    classes = mutableListOf("row m-0")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("alert alert-primary text-center mb-0 p-1 w-100 rounded-0 position-absolute bottom-0")
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("d-flex align-middle")
                        }
                        styledH6 {
                            css {
                                classes = mutableListOf("me-auto mb-0 ps-3")
                            }
                            val time = props.questionState.timerState.time
                            +"${time / 60 % 60} : ${time % 60}"
                        }
                        styledH6 {
                            css {
                                classes = mutableListOf("pe-3")
                            }
                            +"Points: "
                            styledSpan {
                                css {
                                    classes = mutableListOf("")
                                }
                                val points = props.questionState.timerState.count
                                +"$points"
                            }
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
