package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.*
import net.milosvasic.logger.ConsoleLogger
import java.util.*

private class TryDispatcher

fun main(args: Array<String>) {

    val LOG_TAG = TryDispatcher::class
    val logger = ConsoleLogger()

    val root = Route.Builder().addRouteElement(RootRouteElement()).build()

    val factory = object : ResponseFactory {
        override fun getResponse(params: HashMap<RouteElement, String>): Response {
            return Response("Executed ${Date()} [ ${params.size} ]")
        }
    }

    val action = object : ResponseAction {
        override fun onAction() {
            println("Action taken!")
        }
    }

    val routeCatalogs = Route.Builder()
            .addRouteElement(StaticRouteElement("catalogs"))
            .addRouteElement(DynamicRouteElement("catalog"))
            .build()

    val routeUserRepos = Route.Builder()
            .addRouteElement(StaticRouteElement("users"))
            .addRouteElement(DynamicRouteElement("username"))
            .addRouteElement(StaticRouteElement("repositories"))
            .build()

    val routeAllRepos = Route.Builder()
            .addRouteElement(StaticRouteElement("repositories"))
            .build()

    val routeAllUsers = Route.Builder()
            .addRouteElement(StaticRouteElement("users"))
            .build()

    val dispatcher = Dispatcher("Dispatcher_Tryout", 2507)
    dispatcher.registerRoute(routeUserRepos, factory)
    dispatcher.registerRoute(routeAllRepos, factory)
    dispatcher.registerRoute(routeAllUsers, factory)
    dispatcher.registerRoute(routeAllUsers, action) // We registered action for user route too!
    dispatcher.registerRoute(routeCatalogs, factory)

    try {
        dispatcher.start()
    } catch (e: Exception) {
        logger.e(LOG_TAG, "Error: " + e)
    }

    // Register route after we started dispatcher
    val routeStop = Route.Builder().addRouteElement(StaticRouteElement("stop")).build()
    val stop = object : ResponseAction {
        override fun onAction() {
            try {
                dispatcher.stop()
            } catch (e: Exception) {
                logger.e(LOG_TAG, "Error: " + e)
            }
        }
    }
    dispatcher.registerRoute(routeStop, object : ResponseFactory {
        override fun getResponse(params: HashMap<RouteElement, String>): Response {
            return Response("<h1>Dispatcher stopped</h1>")
        }
    })
    dispatcher.registerRoute(routeStop, stop)

    val accountsParams = "accounts"
    val userParam = "user"
    val attributesParam = "attributes"
    val operationParam = "operation"
    val routeAccounts = Route.Builder()
            .addRouteElement(StaticRouteElement(accountsParams))
            .addRouteElement(DynamicRouteElement(userParam))
            .addRouteElement(StaticRouteElement(attributesParam))
            .addRouteElement(DynamicRouteElement(operationParam))
            .build()

    val factoryAccounts = object : ResponseFactory {
        override fun getResponse(params: HashMap<RouteElement, String>): Response {
            val builder = StringBuilder("<h1>We have parameters:</h1>")
            builder.append("<ul>")
            params.forEach {
                element, value ->
                builder.append("<li>${element.name} -> $value</li>")
            }
            builder.append("</ul>")
            return Response(builder.toString())
        }
    }

    dispatcher.registerRoute(routeAccounts, factoryAccounts)

    Thread.sleep(10000)
    dispatcher.unregisterRoute(routeCatalogs)

}
