package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.route.Route

abstract class ResponseFactory(rout: Route) {

    abstract fun getResponse(): Response

}