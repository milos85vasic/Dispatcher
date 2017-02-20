package net.milosvasic.dispatcher.response.assets

import net.milosvasic.dispatcher.route.RouteElement
import java.util.*


interface AssetFactory {

    fun getContent(params: HashMap<RouteElement, String>): Asset

}