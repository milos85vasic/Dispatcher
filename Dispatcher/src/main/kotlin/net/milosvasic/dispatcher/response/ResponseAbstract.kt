package net.milosvasic.dispatcher.response


abstract class ResponseAbstract<out T>(val content: T, val code: Int) {

    abstract fun getBytes(): ByteArray?

}