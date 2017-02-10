package net.milosvasic.dispatcher.request

import com.sun.net.httpserver.HttpExchange


class RequestPath(exchange: HttpExchange) {

    val value: String

    init {
        var path = exchange.requestURI.path
        if (path.length > 1 && path.endsWith("/")) {
            path = path.substring(0, path.lastIndex)
        }
        value = path
    }

}