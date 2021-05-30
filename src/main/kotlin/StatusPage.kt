import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH6

external interface StatusPageProps : RProps {
    var left: String
    var right: String
}

class StatusPage : RComponent<StatusPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("w-100")
            }
            styledDiv {
                css {
                    classes = mutableListOf("alert alert-primary text-left mb-0 p-2 w-100 rounded-0")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("row")
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                        styledH6 {
                            +props.left
                        }
                    }
                    styledDiv {
                        css {
                            classes = mutableListOf("col")
                        }
                        styledH6 {
                            css {
                                classes = mutableListOf("text-end")
                            }
                            +props.right
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.statusPage(handler: StatusPageProps.() -> Unit): ReactElement {
    return child(StatusPage::class) {
        this.attrs(handler)
    }
}
