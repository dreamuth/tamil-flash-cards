package components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH3

external interface KeyValuePairProps : RProps {
    var key: String
    var value: String
}

class KeyValuePair : RComponent<KeyValuePairProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("row m-1")
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledH3 {
                    css {
                        classes = mutableListOf("card-text")
                    }
                    +props.key
                }
            }
            styledDiv {
                css {
                    classes = mutableListOf("col p-1")
                }
                styledH3 {
                    css {
                        classes = mutableListOf("card-text")
                    }
                    +props.value
                }
            }
        }
    }
}

fun RBuilder.keyValuePair(handler: KeyValuePairProps.() -> Unit): ReactElement {
    return child(KeyValuePair::class) {
        this.attrs(handler)
    }
}
