package english

import EnglishLevel
import QuestionState
import components.keyValuePair
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
import styled.styledH2

external interface ResultsPageProps : RProps {
    var questionState: QuestionState
    var onReloadClick: () -> Unit
    var onNextLevelClick: () -> Unit
}

class ResultsPage : RComponent<ResultsPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-1")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                val time = props.questionState.timerState.time
                val displayValue = "${time / 60 % 60} : ${time % 60}"
                styledDiv {
                    css {
                        classes = mutableListOf("card bg-primary text-white")
                        height = 250.px
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-header")
                        }
                        styledH2 {
                            css {
                                classes = mutableListOf("text-center")
                            }
                            +"congratulations!!!"
                        }
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                        }
                        keyValuePair {
                            label = "Level:"
                            value = props.questionState.selectedEnglishLevel.name.removePrefix("LEVEL_")
                        }
                        keyValuePair {
                            label = "Total question:"
                            value = "${props.questionState.englishState.getMaxPoints()}"
                        }
                        if (props.questionState.showTimer) {
                            keyValuePair {
                                label = "Duration:"
                                value = displayValue
                            }
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                classes = mutableListOf("row m-0")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1 d-flex")
                }
                styledButton {
                    css {
                        classes = mutableListOf("btn btn-success m-1 flex-fill")
                        fontSize = 30.px
                        height = 60.px
                    }
                    attrs {
                        onClickFunction = {
                            props.onReloadClick()
                        }
                    }
                    +"Play Again"
                }
                if (props.questionState.selectedEnglishLevel != EnglishLevel.LEVEL_VIII) {
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-info m-1 flex-fill")
                            fontSize = 30.px
                            height = 60.px
                        }
                        attrs {
                            onClickFunction = {
                                props.onNextLevelClick()
                            }
                        }
                        +"Next Level"
                    }
                }
            }
        }
    }
}

fun RBuilder.resultsPage(handler: ResultsPageProps.() -> Unit): ReactElement {
    return child(ResultsPage::class) {
        this.attrs(handler)
    }
}
