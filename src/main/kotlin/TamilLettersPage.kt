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

external interface TamilLettersPageProps: RProps {
    var questionState: QuestionState
    var onShowAnswerClick: () -> Unit
    var onPreviousClick: () -> Unit
    var onNextClick: () -> Unit
}

class TamilLettersPage : RComponent<TamilLettersPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-1")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("card bg-warning text-center")
                        width = 100.pct
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                            fontSize = 40.px
                        }
                        +props.questionState.letterState.getCurrent().mei
                    }
                }
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("card bg-warning text-center")
                        width = 100.pct
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                            fontSize = 40.px
                        }
                        +props.questionState.letterState.getCurrent().uyir
                    }
                }
            }
        }
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
                            if (props.questionState.showAnswer) {
                                props.onNextClick()
                            } else {
                                props.onShowAnswerClick()
                            }
                        }
                    }
                    if (props.questionState.showAnswer) {
                        +props.questionState.letterState.getAnswer()
                    } else {
                        +""
                    }
                }
            }
        }
    }
}

fun RBuilder.tamilLettersPage(handler: TamilLettersPageProps.() -> Unit): ReactElement {
    return child(TamilLettersPage::class) {
        this.attrs(handler)
    }
}
