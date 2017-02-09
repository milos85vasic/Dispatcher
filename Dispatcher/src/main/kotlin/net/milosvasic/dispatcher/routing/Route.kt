package net.milosvasic.dispatcher.routing


class Route private constructor() {

    private val elements = mutableListOf<RouteElement>()

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