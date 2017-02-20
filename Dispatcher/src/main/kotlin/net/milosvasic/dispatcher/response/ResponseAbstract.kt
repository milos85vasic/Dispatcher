package net.milosvasic.dispatcher.response

import com.sun.net.httpserver.Headers
import net.milosvasic.dispatcher.DispatcherVersion
import net.milosvasic.dispatcher.content.Labels
import net.milosvasic.dispatcher.headers.HEADER


abstract class ResponseAbstract<out T>(val content: T, val code: Int) {

    private val version = DispatcherVersion()

    abstract fun getBytes(): ByteArray?

    open fun getHeaders(): Headers {
        val headers = Headers()
        headers.add("Server", "${Labels.DISPATCHER} ${version.VERSION.replace("_", " ")}")
        headers.add(HEADER.CACHE_CONTROL.value, "max-age=3600")
        return headers
    }

}