package tamil

import QuestionState
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
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledImg

external interface TamilLevelIProps : RProps {
    var questionState: QuestionState
    var onShowAnswerClick: (Int) -> Unit
    var onNextClick: () -> Unit
}

external interface TamilLevelIState : RState {
    var checkState: MutableMap<String, Boolean>
    var showHelp: Boolean
}

class TamilLevelI(props: TamilLevelIProps) : RComponent<TamilLevelIProps, TamilLevelIState>(props) {
    override fun TamilLevelIState.init(props: TamilLevelIProps) {
        val mei = props.questionState.letterState.getCurrent().mei
        val uyirMeiLetters = props.questionState.letterState.meiLettersMap[mei]!!
        checkState = uyirMeiLetters.associateWith { true }.toMutableMap()
        showHelp = false
    }

    override fun RBuilder.render() {
        val letterState = props.questionState.letterState
        val current = letterState.getCurrent()
        val showAnswer = props.questionState.showAnswer

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
                        classes = mutableListOf("card bg-warning text-center")
                        width = 100.pct
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                            fontSize = 40.px
                        }
                        +current.mei
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
                        if (state.showHelp) {
                            +letterState.help[current.uyir]!!
                        } else {
                            styledImg {
                                css {
                                    width = 50.px
                                }
                                attrs.src = "svg/lightbulb.svg"
                            }
                        }
                        attrs {
                            onClickFunction = {
                                setState {
                                    showHelp = !showHelp
                                }
                            }
                        }
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
                        +current.uyir
                    }
                }
            }
        }
        styledDiv {
            css {
                classes = mutableListOf("d-flex flex-wrap")
            }
            for (entry in state.checkState.entries) {
                styledButton {
                    val style = when {
                        showAnswer && entry.key == letterState.getAnswer() -> "success"
                        entry.value -> "primary"
                        else -> "danger"
                    }
                    css {
                        classes = mutableListOf("btn btn-$style m-2")
                    }
                    attrs {
                        disabled = if (showAnswer) true else !entry.value
                        onClickFunction = {
                            if (letterState.getAnswer() == entry.key) {
                                val failedCount = state.checkState.values.filter { !it }.count()
                                val points = when {
                                    failedCount >= 3 -> 0
                                    state.showHelp && failedCount >= 1 -> 0
                                    state.showHelp -> 1 - failedCount
                                    else -> 3 - failedCount
                                }
                                props.onShowAnswerClick(points)
                            } else {
                                setState {
                                    checkState[entry.key] = false
                                }
                            }
                        }
                    }
                    +entry.key
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
                        height = 150.px
                    }
                    attrs {
                        disabled = !showAnswer
                        onClickFunction = {
                            if (showAnswer) {
                                props.onNextClick()
                            }
                        }
                        if (showAnswer) +letterState.getAnswer() else +"?"
                    }
                }
            }
        }
    }
}

fun RBuilder.tamilLevelI(handler: TamilLevelIProps.() -> Unit): ReactElement {
    return child(TamilLevelI::class) {
        this.attrs(handler)
    }
}
