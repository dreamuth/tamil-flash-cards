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
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv

external interface SightWordsPageProps : RProps {
    var questionState: QuestionState
    var onLevelChangeClick: (EnglishLevel) -> Unit
    var onNextClick: () -> Unit
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
                classes = mutableListOf("d-flex bd-highlight")
            }
            if (props.questionState.sightWordsAudios.isNotEmpty()) {
                props.questionState.sightWordsAudios.forEach { soundUrl ->
                    styledDiv {
                        css {
                            classes = mutableListOf("p-2 flex-fill")
                        }
                        styledButton {
                            css {
                                classes = mutableListOf("btn btn-success w-100")
                                fontSize = 20.px
                            }
                            attrs {
                                onClickFunction = {
                                    Audio(soundUrl).play()
                                }
                            }
                            +"Audio"
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
