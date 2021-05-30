package english

import QuestionState
import kotlinx.css.fontSize
import kotlinx.css.height
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
import styled.styledImg

external interface NavAndAudioProps : RProps {
    var questionState: QuestionState
    var onBackClick: () -> Unit
    var onAudioClick: (String) -> Unit
}

class NavAndAudio : RComponent<NavAndAudioProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-0")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("d-flex")
                    }
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-success m-1 flex-fill")
                            fontSize = 30.px
                            height = 60.px
                        }
                        attrs {
                            disabled = props.questionState.timerState.count == 0
                            onClickFunction = {
                                props.onBackClick()
                            }
                        }
                        +"Back"
                    }
                    if (props.questionState.sightWordsAudios.isNotEmpty()) {
                        props.questionState.sightWordsAudios.forEach { soundInfo ->
                            styledButton {
                                css {
                                    classes = mutableListOf("btn btn-success m-1 flex-fill")
                                    fontSize = 20.px
                                    height = 60.px
                                }
                                attrs {
                                    onClickFunction = {
                                        props.onAudioClick(soundInfo.key)
                                    }
                                }
                                styledImg {
                                    css {
                                        width = 40.px
                                    }
                                    attrs.src = "svg/audio.svg"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.navAndAudio(handler: NavAndAudioProps.() -> Unit): ReactElement {
    return child(NavAndAudio::class) {
        this.attrs(handler)
    }
}
