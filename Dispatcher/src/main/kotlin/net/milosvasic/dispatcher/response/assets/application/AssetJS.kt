package net.milosvasic.dispatcher.response.assets.application

import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.CONTENT_TYPE
import net.milosvasic.dispatcher.response.assets.Asset

class AssetJS(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    init {
        headers.add(HEADER.CONTENT_TYPE.value, CONTENT_TYPE.JAVASCRIPT.value)
    }

}
