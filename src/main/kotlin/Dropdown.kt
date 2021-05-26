import kotlinx.css.LinearDimension
import kotlinx.css.width
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.role
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledSpan

external interface DropdownProps : RProps {
    var id: String
    var names: List<List<String>>
    var selectedName: String
    var badge: Int?
    var width: LinearDimension?
    var onDropdownClick: (Int, String) -> Unit
}

class Dropdown : RComponent<DropdownProps, RState>() {
    override fun RBuilder.render() {
        styledButton {
            css {
                classes = mutableListOf("btn btn-primary dropdown-toggle")
                props.width?.let { width = it }
            }
            attrs {
                id = props.id
                role = "button"
                attributes["data-bs-toggle"] = "dropdown"
                attributes["aria-expanded"] = "false"
            }
            +props.selectedName
            props.badge?.let {
                styledSpan {
                    css {
                        classes = mutableListOf("badge badge-light")
                    }
                    +"$it"
                }
            }
        }
        styledDiv {
            css {
                classes = mutableListOf("dropdown-menu dropdown-menu-right")
            }
            for ((listIndex, sectionList) in props.names.withIndex()) {
                for ((nameIndex, name) in sectionList.withIndex()) {
                    styledButton {
                        css {
                            classes = mutableListOf("dropdown-item")
                        }
                        +name
                        attrs {
                            onClickFunction = {
                                println("On click...")
                                props.onDropdownClick(nameIndex, name)
                            }
                        }
                    }
                }
                if (listIndex + 1 < props.names.size) {
                    styledDiv {
                        css {
                            classes = mutableListOf("dropdown-divider")
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.dropdown(handler: DropdownProps.() -> Unit): ReactElement {
    return child(Dropdown::class) {
        this.attrs(handler)
    }
}
