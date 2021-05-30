package tamil

import QuestionState
import TamilLevel
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement

external interface TamilLettersPageProps: RProps {
    var questionState: QuestionState
    var onShowAnswerClick: () -> Unit
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
            }
        } else {
            tamilLevelII {
                questionState = props.questionState
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
