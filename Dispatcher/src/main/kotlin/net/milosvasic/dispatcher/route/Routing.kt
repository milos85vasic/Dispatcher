package net.milosvasic.dispatcher.route

import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory


interface Routing {

    fun registerRoute(route: Route, responseFactory: ResponseFactory)

    fun registerRoute(route: Route, responseAction: ResponseAction)

}