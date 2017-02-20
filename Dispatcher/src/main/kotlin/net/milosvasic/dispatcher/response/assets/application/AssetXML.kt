package net.milosvasic.dispatcher.response.assets.application

import com.sun.net.httpserver.Headers
import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.assets.Asset

class AssetXML(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    override fun getHeaders(): Headers {
        val headers = super.getHeaders()
        headers.add(HEADER.CONTENT_TYPE.value, "application/xml")
        return headers
    }

}