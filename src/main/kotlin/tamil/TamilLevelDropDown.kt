package tamil

import TamilLevel
import components.dropdown
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.setState
import styled.css
import styled.styledDiv

external interface TamilLevelDropDownProps : RProps {
    var displayValue: String
    var onLevelChangeClick: (TamilLevel) -> Unit
}

class TamilLevelDropDown : RComponent<TamilLevelDropDownProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("btn-group p-2 w-100")
            }
            dropdown {
                id = "TamilLevel"
                names = listOf(
                    listOf(
                        TamilLevel.LEVEL_I.displayValue,
                        TamilLevel.LEVEL_II.displayValue,
                        TamilLevel.LEVEL_III.displayValue
                    )
                )
                selectedName = props.displayValue
                onDropdownClick = { _, name ->
                    setState {
                        props.onLevelChangeClick(TamilLevel.fromDisplayValue(name))
                    }
                }
            }
        }
    }
}

fun RBuilder.tamilLevelDropDown(handler: TamilLevelDropDownProps.() -> Unit): ReactElement {
    return child(TamilLevelDropDown::class) {
        this.attrs(handler)
    }
}
