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
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledImg

external interface SightWordsPageProps : RProps {
    var questionState: QuestionState
    var onLevelChangeClick: (EnglishLevel) -> Unit
    var onBackClick: () -> Unit
    var onNextClick: () -> Unit
    var onAudioClick: (String) -> Unit
}

class SightWordsPage : RComponent<SightWordsPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("btn-group p-2 w-100")
            }
            dropdown {
                id = "EnglishSightWordsLevel"
                names = listOf(
                    listOf(
                        EnglishLevel.LEVEL_I.displayValue,
                        EnglishLevel.LEVEL_II.displayValue,
                        EnglishLevel.LEVEL_III.displayValue,
                        EnglishLevel.LEVEL_IV.displayValue,
                        EnglishLevel.LEVEL_V.displayValue,
                        EnglishLevel.LEVEL_VI.displayValue,
                    )
                )
                selectedName = props.questionState.selectedEnglishLevel.displayValue
                onDropdownClick = { _, name ->
                    setState {
                        props.onLevelChangeClick(EnglishLevel.fromDisplayValue(name))
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
                            props.onNextClick()
                        }
                    }
                    +props.questionState.sightWordsState.getCurrent()
                }
            }
        }
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

fun RBuilder.sightWordsPage(handler: SightWordsPageProps.() -> Unit): ReactElement {
    return child(SightWordsPage::class) {
        this.attrs(handler)
    }
}
