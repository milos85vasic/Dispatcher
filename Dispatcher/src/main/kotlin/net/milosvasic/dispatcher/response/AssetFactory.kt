package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.route.RouteElement
import java.util.*


interface AssetFactory {

    fun getContent(params: HashMap<RouteElement, String>): Asset

}