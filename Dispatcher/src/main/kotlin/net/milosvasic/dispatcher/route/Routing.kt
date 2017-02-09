package net.milosvasic.dispatcher.route

import net.milosvasic.dispatcher.response.ResponseFactory


interface Routing {

    fun registerRoute(route: Route, responseFactory: ResponseFactory)

}