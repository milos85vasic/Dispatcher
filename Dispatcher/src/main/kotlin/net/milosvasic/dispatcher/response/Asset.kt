package net.milosvasic.dispatcher.response


class Asset(content: ByteArray?, code: Int = 200) : ResponseAbstract<ByteArray?>(content, code) {

    override fun getBytes(): ByteArray? {
        return content
    }

}