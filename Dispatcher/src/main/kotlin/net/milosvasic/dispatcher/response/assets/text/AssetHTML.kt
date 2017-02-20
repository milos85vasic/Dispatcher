package net.milosvasic.dispatcher.response.assets.text

import com.sun.net.httpserver.Headers
import net.milosvasic.dispatcher.headers.HEADER
import net.milosvasic.dispatcher.response.assets.Asset

class AssetHTML(content: ByteArray?, code: Int = 200) : Asset(content, code) {

    override fun getHeaders(): Headers {
        val headers = super.getHeaders()
        headers.add(HEADER.CONTENT_TYPE.value, "text/html")
        return headers
    }

}