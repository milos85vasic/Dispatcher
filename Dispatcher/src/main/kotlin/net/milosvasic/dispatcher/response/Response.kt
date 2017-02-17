package net.milosvasic.dispatcher.response

class Response(content: String, code: Int = 200) : ResponseAbstract<String>(content, code) {

    override fun getBytes(): ByteArray? {
        return content.toByteArray()
    }

}