package tamil

import QuestionState
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv

external interface LevelIAnswerProps : RProps {
    var questionState: QuestionState
}

class LevelIAnswer : RComponent<LevelIAnswerProps, RState>() {
    override fun RBuilder.render() {
        val question = props.questionState.tamilState.getQuestion()

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
                            height = 92.px
                        }
                        styledDiv {
                            css {
                                fontSize = 30.px
                            }
                            +"${question.mei} + ${question.uyir} = ${question.uyirMei}"
                        }
                        styledDiv {
                            css {
                                fontSize = 15.px
                            }
                            +"You got ${props.questionState.tamilState.lastPoints} points"
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.levelIAnswer(handler: LevelIAnswerProps.() -> Unit): ReactElement {
    return child(LevelIAnswer::class) {
        this.attrs(handler)
    }
}
