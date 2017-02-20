package net.milosvasic.dispatcher.response.assets

import net.milosvasic.dispatcher.response.ResponseAbstract


open class Asset(content: ByteArray?, code: Int = 200) : ResponseAbstract<ByteArray?>(content, code) {

    final override fun getBytes(): ByteArray? {
        return content
    }

}