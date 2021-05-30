package english

import EnglishLevel
import TimerState
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

external interface ResultsPageProps : RProps {
    var englishLevel: EnglishLevel
    var timerState: TimerState
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
                val time = props.timerState.time
                val displayValue = "${time / 60 % 60} : ${time % 60}"
                styledDiv {
                    css {
                        classes = mutableListOf("card bg-success text-white")
                        height = 250.px
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("card-body")
                        }
                        keyValuePair {
                            key = "Level:"
                            value = props.englishLevel.name.removePrefix("LEVEL_")
                        }
                        keyValuePair {
                            key = "Total question:"
                            value = "${props.timerState.total}"
                        }
                        keyValuePair {
                            key = "Duration:"
                            value = displayValue
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
                if (props.englishLevel != EnglishLevel.LEVEL_VI) {
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-success m-1 flex-fill")
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
