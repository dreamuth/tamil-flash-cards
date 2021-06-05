package english

import QuestionState
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Audio
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledButton
import styled.styledDiv

external interface NavAndAudioProps : RProps {
    var questionState: QuestionState
    var onBackClick: () -> Unit
    var onNextClick: () -> Unit
    var audio: Audio?
    var onAudioClick: () -> Unit
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
                            disabled = !props.questionState.englishState.hasPrevious()
                            onClickFunction = {
                                props.onBackClick()
                            }
                        }
                        +"Back"
                    }
                    styledButton {
                        css {
                            classes = mutableListOf("btn btn-success m-1 flex-fill")
                            fontSize = 30.px
                            height = 60.px
                        }
                        attrs {
                            disabled = props.questionState.englishState.isCompleted()
                            onClickFunction = {
                                props.audio?.let {
                                    props.onAudioClick()
                                }
                                props.onNextClick()
                            }
                        }
                        +"Next"
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
