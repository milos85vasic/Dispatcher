package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.route.Route

interface ResponseFactory {

    fun getResponse(): Response

}