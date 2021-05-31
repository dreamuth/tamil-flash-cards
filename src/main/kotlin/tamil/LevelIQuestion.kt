package tamil

import QuestionState
import kotlinx.css.fontSize
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
import styled.styledDiv
import styled.styledImg

external interface LevelIQuestionProps : RProps {
    var questionState: QuestionState
    var showHint: Boolean
    var onHintClick: () -> Unit
}

class LevelIQuestion : RComponent<LevelIQuestionProps, RState>() {
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
                            fontSize = 40.px
                        }
                        +question.mei
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
                        if (props.showHint) {
                            +question.help
                        } else {
                            styledImg {
                                css {
                                    width = 50.px
                                }
                                attrs.src = "svg/lightbulb.svg"
                            }
                        }
                        attrs {
                            onClickFunction = { props.onHintClick() }
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
                        +question.uyir
                    }
                }
            }
        }
    }
}

fun RBuilder.levelIQuestion(handler: LevelIQuestionProps.() -> Unit): ReactElement {
    return child(LevelIQuestion::class) {
        this.attrs(handler)
    }
}
