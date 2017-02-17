package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.route.RouteElement
import java.util.*


interface ResponseAsset {

    fun getContent(params: HashMap<RouteElement, String>): ByteArray

}