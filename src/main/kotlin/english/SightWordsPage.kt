package english

import EnglishLevel
import QuestionState
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement

external interface SightWordsPageProps : RProps {
    var questionState: QuestionState
    var onLevelChangeClick: (EnglishLevel) -> Unit
    var onBackClick: () -> Unit
    var onNextClick: () -> Unit
    var onAudioClick: () -> Unit
    var onReloadClick: () -> Unit
    var onNextLevelClick: () -> Unit
}

class SightWordsPage : RComponent<SightWordsPageProps, RState>() {
    override fun RBuilder.render() {
        englishLevelDropDown {
            displayValue = props.questionState.selectedEnglishLevel.displayValue
            onLevelChangeClick = props.onLevelChangeClick
        }
        if (props.questionState.englishState.isCompleted()) {
            resultsPage {
                questionState = props.questionState
                onReloadClick = props.onReloadClick
                onNextLevelClick = props.onNextLevelClick
            }
        } else {
            question {
                displayValue = props.questionState.englishState.getQuestion()
                audio = props.questionState.sightWordsAudio
                onAudioClick = props.onAudioClick
            }
            navAndAudio {
                questionState = props.questionState
                onBackClick = props.onBackClick
                onNextClick = props.onNextClick
            }
        }
    }
}

fun RBuilder.sightWordsPage(handler: SightWordsPageProps.() -> Unit): ReactElement {
    return child(SightWordsPage::class) {
        this.attrs(handler)
    }
}
