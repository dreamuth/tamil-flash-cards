package english

import EnglishLevel
import dropdown
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.setState
import styled.css
import styled.styledDiv

external interface LevelDropDownProps : RProps {
    var displayValue: String
    var onLevelChangeClick: (EnglishLevel) -> Unit
}

class LevelDropDown : RComponent<LevelDropDownProps, RState>() {
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
                selectedName = props.displayValue
                onDropdownClick = { _, name ->
                    setState {
                        println(name)
                        props.onLevelChangeClick(EnglishLevel.fromDisplayValue(name))
                    }
                }
            }
        }
    }
}

fun RBuilder.levelDropDown(handler: LevelDropDownProps.() -> Unit): ReactElement {
    return child(LevelDropDown::class) {
        this.attrs(handler)
    }
}
