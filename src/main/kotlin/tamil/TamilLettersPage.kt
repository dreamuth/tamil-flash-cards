package tamil

import QuestionState
import TamilLevel
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.key
import tamil.identify.tamilLevel1
import tamil.identify.tamilLevel2
import tamil.sound.tamilLevel3

external interface TamilLettersPageProps : RProps {
    var questionState: QuestionState
    var onShowAnswerClick: (Int) -> Unit
    var onPreviousClick: () -> Unit
    var onNextClick: () -> Unit
    var onLevelChangeClick: (TamilLevel) -> Unit
    var onReloadClick: () -> Unit
    var onNextLevelClick: () -> Unit
}

class TamilLettersPage : RComponent<TamilLettersPageProps, RState>() {
    override fun RBuilder.render() {
        tamilLevelDropDown {
            displayValue = props.questionState.selectedTamilLevel.displayValue
            onLevelChangeClick = props.onLevelChangeClick
        }
        if (props.questionState.timerState.time == 0L) {
            resultsPage {
                questionState = props.questionState
                onReloadClick = props.onReloadClick
                onNextLevelClick = props.onNextLevelClick
            }
        } else {
            when (props.questionState.selectedTamilLevel) {
                TamilLevel.LEVEL_I -> {
                    tamilLevel1 {
                        questionState = props.questionState
                        onShowAnswerClick = props.onShowAnswerClick
                        key = props.questionState.tamilState.getAnswer()
                    }
                }
                TamilLevel.LEVEL_II -> {
                    tamilLevel2 {
                        questionState = props.questionState
                        onShowAnswerClick = props.onShowAnswerClick
                        key = props.questionState.tamilState.getAnswer()
                    }
                }
                TamilLevel.LEVEL_III -> {
                    tamilLevel3 {
                        questionState = props.questionState
                        onNextClick = props.onNextClick
                    }
                }
            }
            navigation {
                questionState = props.questionState
                onPreviousClick = props.onPreviousClick
                onNextClick = props.onNextClick
            }
        }
    }
}

fun RBuilder.tamilLettersPage(handler: TamilLettersPageProps.() -> Unit): ReactElement {
    return child(TamilLettersPage::class) {
        this.attrs(handler)
    }
}
