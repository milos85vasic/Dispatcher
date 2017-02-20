package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.route.RouteElement
import java.util.*

interface ResponseFactory {

    fun getResponse(params: HashMap<String, String>): Response

}