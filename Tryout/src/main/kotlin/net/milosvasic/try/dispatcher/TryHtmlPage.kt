package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseAsset
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.*
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

fun main(args: Array<String>) {

    fun getBytes(input: InputStream): ByteArray {
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(1024)
        var nRead = input.read(data, 0, data.size)
        while (nRead != -1) {
            buffer.write(data, 0, nRead)
            nRead = input.read(data, 0, data.size)
        }
        buffer.flush()
        return buffer.toByteArray()
    }

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

    val assetsFolder = StaticRouteElement("Assets")
    val assets = AssetsRoute.Builder()
            .addRouteElement(assetsFolder)
            .addRouteElement(DynamicRouteElement("Asset"))
            .build()

    val faviconStaticRoute = StaticRouteElement("favicon.ico")
    val favicon = AssetsRoute.Builder()
            .addRouteElement(faviconStaticRoute)
            .build()

    dispatcher.registerRoute(rootRoute, homepage)
    dispatcher.registerRoute(assets, object : ResponseAsset {
        override fun getContent(params: HashMap<RouteElement, String>): ByteArray {
            val assetName = params[assetsFolder]
            val input = javaClass.classLoader.getResourceAsStream("Assets/$assetName")
            return getBytes(input)
        }
    })
    dispatcher.registerRoute(favicon, object : ResponseAsset {
        override fun getContent(params: HashMap<RouteElement, String>): ByteArray {
            val assetName = params[faviconStaticRoute]
            val input = javaClass.classLoader.getResourceAsStream(assetName)
            return getBytes(input)
        }
    })

    dispatcher.start()


}