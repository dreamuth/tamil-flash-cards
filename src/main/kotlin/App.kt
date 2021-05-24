import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
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
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledH6
import styled.styledSpan

suspend fun fetchSource(): MutableMap<LetterKey, String> {
    val sourceUrl = "/private/tamilLetters.txt"
    val sourceData = window.fetch(sourceUrl).await().text().await()
    val tamilLetters = readSource(sourceData)
    println("version: 2021-05-23.1")
    return tamilLetters
}

external interface AppState : RState {
    var loaded: Boolean
    var tamilLetters: MutableMap<LetterKey, String>
    var questionState: QuestionState
}

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val receivedLetters = fetchSource()
            setState {
                tamilLetters = receivedLetters
                questionState = QuestionState(receivedLetters, LetterState(receivedLetters), TimerState(isLive = true), false)
                loaded = true
                window.setInterval(timerHandler(), 1000)
            }
        }
    }

    private fun timerHandler(): () -> Unit = {
        if (state.questionState.timerState.isLive
            && !state.questionState.timerState.isPaused) {
            setState {
                questionState.timerState.time++
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("container-fluid m-0 p-0")
                height = 100.pct
            }
            styledDiv {
                css {
                    classes = mutableListOf("alert alert-primary text-center mb-0 rounded-0")
                }
                +"தமிழ் பயிற்சி"
            }
            styledDiv {
                css {
                    classes = mutableListOf("container-fluid m-0 p-0")
                }
                if (state.loaded) {
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
                                    +state.questionState.letterState.getCurrent().mei
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
                                    +state.questionState.letterState.getCurrent().uyir
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
                                        setState {
                                            questionState.showAnswer = !questionState.showAnswer
                                        }
                                    }
                                }
                                if (state.questionState.showAnswer) {
                                    +state.questionState.letterState.getAnswer()
                                } else {
                                    +"Show"
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
                                    classes = mutableListOf("btn btn-primary w-100")
                                    fontSize = 40.px
                                }
                                attrs {
                                    onClickFunction = {
                                        setState {
                                            questionState.showAnswer = false
                                            questionState.letterState.goPrevious()
                                        }
                                    }
                                }
                                +"Previous"
                            }
                        }
                        styledDiv {
                            css {
                                classes = mutableListOf("col p-1")
                            }
                            styledButton {
                                css {
                                    classes = mutableListOf("btn btn-primary w-100")
                                    fontSize = 40.px
                                }
                                attrs {
                                    onClickFunction = {
                                        setState {
                                            questionState.showAnswer = false
                                            questionState.timerState.count = questionState.letterState.goNext()
                                        }
                                    }
                                }
                                +"Next"
                            }
                        }
                    }
                }
            }
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
                                    classes = mutableListOf("me-auto mb-0")
                                }
                                val time = if (state.loaded) state.questionState.timerState.time else 0
                                +"${time / 60 % 60} : ${time % 60}"
                            }
                            styledH6 {
                                css {
                                    classes = mutableListOf("")
                                }
                                +"Points: "
                                styledSpan {
                                    css {
                                        classes = mutableListOf("")
                                    }
                                    val points = if (state.loaded) state.questionState.timerState.count else 0
                                    +"$points"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
