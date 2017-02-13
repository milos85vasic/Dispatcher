package net.milosvasic.dispatcher.route

import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory


interface Routing {

    fun registerRoute(route: Route, responseFactory: ResponseFactory): Boolean

    fun registerRoute(route: Route, responseAction: ResponseAction): Boolean

    fun unregisterRoute(route: Route): Boolean

    fun unregisterRoute(route: Route, responseAction: ResponseAction): Boolean

    fun unregisterRoute(route: Route, responseFactory: ResponseFactory): Boolean

}