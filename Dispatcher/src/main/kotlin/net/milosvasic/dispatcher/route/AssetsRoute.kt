package net.milosvasic.dispatcher.route


class AssetsRoute private constructor() : Route() {

    class Builder {
        private val elements = mutableListOf<RouteElement>()

        fun addRouteElement(element: RouteElement): Builder {
            elements.add(element)
            return this
        }

        fun build(): AssetsRoute {
            val route = AssetsRoute()
            for (element in elements) {
                route.addRouteElement(element)
            }
            return route
        }
    }

}