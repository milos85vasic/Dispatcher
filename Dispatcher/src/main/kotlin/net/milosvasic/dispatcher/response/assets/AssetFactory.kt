package net.milosvasic.dispatcher.response.assets


import java.util.*


interface AssetFactory {

    fun getContent(params: HashMap<String, String>): Asset

}