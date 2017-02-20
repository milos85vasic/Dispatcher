package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.assets.Asset
import net.milosvasic.dispatcher.response.assets.AssetFactory
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.response.assets.image.AssetICON
import net.milosvasic.dispatcher.route.*
// import net.milosvasic.logger.ConsoleLogger
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

// private class TryHtmlPage

fun main(args: Array<String>) {

//    val logger = ConsoleLogger()
//    val TAG = TryHtmlPage::class

    fun getBytes(input: InputStream?): ByteArray? {
        return input?.readBytes()
    }

    fun getResponse(input: InputStream?): String {
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
        return content
    }

    val dispatcher = Dispatcher("Dispatcher_Tryout", 2507)

    val rootRoute = Route.Builder()
            .addRouteElement(RootRouteElement())
            .build()

    val homepage = object : ResponseFactory {
        override fun getResponse(params: HashMap<RouteElement, String>): Response {
            val input = javaClass.classLoader.getResourceAsStream("Html/index.html")
            val content = getResponse(input)
            return Response(content)
        }
    }

    val assetsFolder = DynamicRouteElement("Asset")
    val assetsJs = AssetsRoute.Builder()
            .addRouteElement(StaticRouteElement("Assets"))
            .addRouteElement(StaticRouteElement("Js"))
            .addRouteElement(assetsFolder)
            .build()

    val assets = AssetsRoute.Builder()
            .addRouteElement(StaticRouteElement("Assets"))
            .addRouteElement(assetsFolder)
            .build()

    val assetsJsResponse = object : AssetFactory {
        override fun getContent(params: HashMap<RouteElement, String>): Asset {
            val assetName = params[assetsFolder]
            val input = javaClass.classLoader.getResourceAsStream("Assets/Js/$assetName")
            return Asset(getBytes(input), 200)
        }
    }

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
        override fun getContent(params: HashMap<RouteElement, String>): AssetICON {
            val assetName = faviconStaticRoute.name
            val input = javaClass.classLoader.getResourceAsStream(assetName)
            return AssetICON(getBytes(input))
        }
    }

    dispatcher.registerRoute(rootRoute, homepage)
    dispatcher.registerRoute(assets, assetsResponse)
    dispatcher.registerRoute(assetsJs, assetsJsResponse)
    dispatcher.registerRoute(favicon, faviconResponse)
    dispatcher.start()

}