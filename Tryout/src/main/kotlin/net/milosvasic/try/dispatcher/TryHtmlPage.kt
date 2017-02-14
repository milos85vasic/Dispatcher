package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.RootRouteElement
import net.milosvasic.dispatcher.route.Route
import net.milosvasic.dispatcher.route.RouteElement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main(args: Array<String>) {

    val dispatcher = Dispatcher("Dispatcher_Tryout", 2507)

    val rootRoute = Route.Builder()
            .addRouteElement(RootRouteElement())
            .build()

    val homepage = object : ResponseFactory {
        override fun getResponse(params: HashMap<RouteElement, String>): Response {
            val input = javaClass.classLoader.getResourceAsStream("Html/index.html")
            val bufferedReader = BufferedReader(InputStreamReader(input, "UTF-8"))
            var line = bufferedReader.readLine()
            val builder = StringBuilder()
            while (line != null) {
                builder
                        .append(line)
                        .append("\n")
                line = bufferedReader.readLine()
            }
            val content = builder.toString()
            return Response(content)
        }
    }

    dispatcher.registerRoute(rootRoute, homepage)
    dispatcher.start()

}