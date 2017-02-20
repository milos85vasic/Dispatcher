package net.milosvasic.dispatcher.response

import net.milosvasic.dispatcher.headers.HEADER

class Response(content: String, code: Int = 200, contentType: CONTENT_TYPE = CONTENT_TYPE.HTML) : ResponseAbstract<String>(content, code) {

    init {
        headers.add(HEADER.CONTENT_TYPE.value, "${contentType.value}; charset=UTF-8")
    }

    override fun getBytes(): ByteArray? {
        return content.toByteArray()
    }

}