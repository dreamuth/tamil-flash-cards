package tamil

import QuestionState
import TamilLevel
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.key
import styled.css
import styled.styledButton
import styled.styledDiv

external interface TamilLettersPageProps: RProps {
    var questionState: QuestionState
    var onShowAnswerClick: (Int) -> Unit
    var onPreviousClick: () -> Unit
    var onNextClick: () -> Unit
    var onLevelChangeClick: (TamilLevel) -> Unit
}

class TamilLettersPage : RComponent<TamilLettersPageProps, RState>() {
    override fun RBuilder.render() {
        tamilLevelDropDown {
            displayValue = props.questionState.selectedTamilLevel.displayValue
            onLevelChangeClick = props.onLevelChangeClick
        }
        if (props.questionState.selectedTamilLevel == TamilLevel.LEVEL_I) {
            tamilLevelI {
                questionState = props.questionState
                onShowAnswerClick = props.onShowAnswerClick
                onNextClick = props.onNextClick
                key = props.questionState.letterState.getAnswer()
            }
        } else {
            tamilLevelII {
                questionState = props.questionState
                onNextClick = props.onNextClick
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
                            disabled = props.questionState.timerState.count == 0
                            onClickFunction = {
                                props.onPreviousClick()
                            }
                        }
                        +"முன்பு"
                    }
                }
            }
        }
    }
}

fun RBuilder.tamilLettersPage(handler: TamilLettersPageProps.() -> Unit): ReactElement {
    return child(TamilLettersPage::class) {
        this.attrs(handler)
    }
}
