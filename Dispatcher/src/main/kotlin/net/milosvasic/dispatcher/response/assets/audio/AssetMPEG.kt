package net.milosvasic.dispatcher.response.assets.audio

import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.CONTENT_TYPE
import net.milosvasic.dispatcher.response.assets.Asset

class AssetMPEG(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    init {
        headers.add(HEADER.CONTENT_TYPE.value, CONTENT_TYPE.MPEG.value)
    }

}