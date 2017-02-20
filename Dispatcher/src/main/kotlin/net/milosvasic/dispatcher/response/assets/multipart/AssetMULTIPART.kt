package net.milosvasic.dispatcher.response.assets.multipart

import com.sun.net.httpserver.Headers
import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.assets.Asset

class AssetMULTIPART(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    override fun getHeaders(): Headers {
        val headers = super.getHeaders()
        headers.add(HEADER.CONTENT_TYPE.value, "multipart/form-data")
        return headers
    }

}