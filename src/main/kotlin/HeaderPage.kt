import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import styled.css
import styled.styledDiv
import styled.styledH6

external interface HeaderPageProps : RProps {
    var title: String
}

class HeaderPage : RComponent<HeaderPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("w-100")
            }
            styledDiv {
                css {
                    classes = mutableListOf("row m-0 justify-content-center")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("alert alert-primary text-center mb-0 p-2 w-100 rounded-0 ")
                    }
                    styledH6 {
                        +props.title
                    }
                }
            }
        }
    }
}

fun RBuilder.headerPage(handler: HeaderPageProps.() -> Unit): ReactElement {
    return child(HeaderPage::class) {
        this.attrs(handler)
    }
}
