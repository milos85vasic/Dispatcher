package net.milosvasic.dispatcher.route


open class Route protected constructor() {

    private val elements = mutableListOf<RouteElement>()

    fun getRegex(): String {
        val builder = StringBuilder()
        for (element in elements) {
            when (element) {
                is RootRouteElement -> {
                    builder.append("(/)")
                }
                is StaticRouteElement -> {
                    builder.append("/(${element.name})")
                }
                is DynamicRouteElement -> {
                    builder.append("/(.+?)")
                }
            }
        }
        return builder.toString()
    }

    fun getElements(): List<RouteElement> {
        return elements
    }

    protected fun addRouteElement(element: RouteElement) {
        elements.add(element)
    }

    class Builder {
        private val elements = mutableListOf<RouteElement>()

        fun addRouteElement(element: RouteElement): Builder {
            elements.add(element)
            return this
        }

        fun build(): Route {
            val route = Route()
            for (element in elements) {
                route.addRouteElement(element)
            }
            return route
        }
    }

    override fun toString(): String {
        return printRoute()
    }

    private fun printRoute(): String {
        val builder = StringBuilder()
        for (element in elements) {
            when (element) {
                is RootRouteElement -> {
                    builder.append("/")
                }
                is StaticRouteElement -> {
                    builder.append("/${element.name}")
                }
                is DynamicRouteElement -> {
                    builder.append("/{${element.name}}")
                }
            }
        }
        return builder.toString()
    }
}