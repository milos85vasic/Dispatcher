package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Asset
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.AssetFactory
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

    val assetsFolder = DynamicRouteElement("Asset")
    val assets = AssetsRoute.Builder()
            .addRouteElement(StaticRouteElement("Assets"))
            .addRouteElement(StaticRouteElement("Js"))
            .addRouteElement(assetsFolder)
            .build()

    val assetsResponse = object : AssetFactory {
        override fun getContent(params: HashMap<RouteElement, String>): Asset {
            val assetName = params[assetsFolder]
            val input = javaClass.classLoader.getResourceAsStream("Assets/$assetName")
            return Asset(getBytes(input), 200)
        }
    }

    val faviconStaticRoute = StaticRouteElement("favicon.ico")
    val favicon = AssetsRoute.Builder()
            .addRouteElement(faviconStaticRoute)
            .build()

    val faviconResponse = object : AssetFactory {
        override fun getContent(params: HashMap<RouteElement, String>): Asset {
            val assetName = faviconStaticRoute.name
            val input = javaClass.classLoader.getResourceAsStream(assetName)
            return Asset(getBytes(input), 200)
        }
    }

    dispatcher.registerRoute(rootRoute, homepage)
    dispatcher.registerRoute(assets, assetsResponse)
    dispatcher.registerRoute(favicon, faviconResponse)
    dispatcher.start()

}