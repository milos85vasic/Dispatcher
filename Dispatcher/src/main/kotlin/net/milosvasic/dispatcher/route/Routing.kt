package net.milosvasic.dispatcher.route

import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseAsset
import net.milosvasic.dispatcher.response.ResponseFactory


internal interface Routing {

    fun registerRoute(route: Route, responseFactory: ResponseFactory): Boolean

    fun registerRoute(route: Route, responseAction: ResponseAction): Boolean

    fun registerRoute(route: AssetsRoute, responseAsset: ResponseAsset): Boolean

    fun unregisterRoute(route: Route): Boolean

    fun unregisterRoute(route: Route, responseAction: ResponseAction): Boolean

    fun unregisterRoute(route: Route, responseFactory: ResponseFactory): Boolean

    fun unregisterRoute(route: AssetsRoute, responseAsset: ResponseAsset): Boolean

}