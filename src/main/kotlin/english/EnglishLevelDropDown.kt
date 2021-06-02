package english

import EnglishLevel
import components.dropdown
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.setState
import styled.css
import styled.styledDiv

external interface EnglishLevelDropDownProps : RProps {
    var displayValue: String
    var onLevelChangeClick: (EnglishLevel) -> Unit
}

class EnglishLevelDropDown : RComponent<EnglishLevelDropDownProps, RState>() {
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
                        EnglishLevel.LEVEL_VII.displayValue,
                        EnglishLevel.LEVEL_VIII.displayValue,
                    )
                )
                selectedName = props.displayValue
                onDropdownClick = { _, name ->
                    setState {
                        props.onLevelChangeClick(EnglishLevel.fromDisplayValue(name))
                    }
                }
            }
        }
    }
}

fun RBuilder.englishLevelDropDown(handler: EnglishLevelDropDownProps.() -> Unit): ReactElement {
    return child(EnglishLevelDropDown::class) {
        this.attrs(handler)
    }
}
