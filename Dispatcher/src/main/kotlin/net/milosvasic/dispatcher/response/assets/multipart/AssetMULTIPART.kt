package net.milosvasic.dispatcher.response.assets.multipart

import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.CONTENT_TYPE
import net.milosvasic.dispatcher.response.assets.Asset

class AssetMULTIPART(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    init {
        headers.add(HEADER.CONTENT_TYPE.value, CONTENT_TYPE.MULTI_PART.value)
    }

}