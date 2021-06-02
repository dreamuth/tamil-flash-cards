package tamil

import QuestionState
import kotlinx.css.height
import kotlinx.css.minWidth
import kotlinx.css.px
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

external interface TamilLevelIProps : RProps {
    var questionState: QuestionState
    var onShowAnswerClick: (Int) -> Unit
    var onNextClick: () -> Unit
}

external interface TamilLevelIState : RState {
    var checkState: MutableMap<String, Boolean>
    var showHint: Boolean
}

class TamilLevelI(props: TamilLevelIProps) : RComponent<TamilLevelIProps, TamilLevelIState>(props) {
    override fun TamilLevelIState.init(props: TamilLevelIProps) {
        val uyirMeiLetters = props.questionState.tamilState.getUyirMeiForUyir()
        checkState = uyirMeiLetters.associateWith { true }.toMutableMap()
        showHint = false
    }

    override fun RBuilder.render() {
        val tamilState = props.questionState.tamilState
        val showAnswer = props.questionState.showAnswer

        if (showAnswer) {
            levelIAnswer {
                questionState = props.questionState
            }
        } else {
            levelIQuestion {
                questionState = props.questionState
                showHint = state.showHint
                onHintClick = {
                    if (!state.showHint) {
                        setState {
                            showHint = !showHint
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                classes = mutableListOf("d-flex flex-wrap")
            }
            for (entry in state.checkState.entries) {
                styledButton {
                    val style = when {
                        showAnswer && entry.key == tamilState.getAnswer() -> "success"
                        entry.value -> "primary"
                        else -> "danger"
                    }
                    css {
                        classes = mutableListOf("btn btn-$style m-2")
                        height = 80.px
                        minWidth = 80.px
                    }
                    attrs {
                        onClickFunction = {
                            if (!showAnswer && entry.value) {
                                if (tamilState.getAnswer() == entry.key) {
                                    val failedCount = state.checkState.values.filter { !it }.count()
                                    val newPoints = when {
                                        failedCount >= 3 -> 0
                                        state.showHint && failedCount >= 1 -> 0
                                        state.showHint -> 1 - failedCount
                                        else -> 3 - failedCount
                                    }
                                    props.onShowAnswerClick(newPoints)
                                } else {
                                    setState {
                                        checkState[entry.key] = false
                                    }
                                }
                            }
                        }
                    }
                    +entry.key
                }
            }
        }
    }
}

fun RBuilder.tamilLevelI(handler: TamilLevelIProps.() -> Unit): ReactElement {
    return child(TamilLevelI::class) {
        this.attrs(handler)
    }
}
