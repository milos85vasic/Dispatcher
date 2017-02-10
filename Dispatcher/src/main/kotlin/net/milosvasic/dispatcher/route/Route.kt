package net.milosvasic.dispatcher.route


class Route private constructor() {

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
                    builder.append("/(\\w+)")
                }
            }
        }
        return builder.toString()
    }

    private fun addRouteElement(element: RouteElement) {
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

}