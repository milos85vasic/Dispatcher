package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.DynamicRouteElement
import net.milosvasic.dispatcher.route.RootRouteElement
import net.milosvasic.dispatcher.route.Route
import net.milosvasic.dispatcher.route.StaticRouteElement
import net.milosvasic.logger.ConsoleLogger
import java.util.*

private class TryDispatcher

fun main(args: Array<String>) {

    val LOG_TAG = TryDispatcher::class
    val logger = ConsoleLogger()

    val root = Route.Builder().addRouteElement(RootRouteElement()).build()

    val factory = object : ResponseFactory {
        override fun getResponse(): Response {
            return Response(">>> ${Date()}")
        }
    }

    val action = object : ResponseAction {
        override fun onAction() {
            println("Action taken!")
        }
    }

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

    val dispatcher = Dispatcher(2507)
    dispatcher.registerRoute(root, factory)
    dispatcher.registerRoute(routeUserRepos, factory)
    dispatcher.registerRoute(routeAllRepos, factory)
    dispatcher.registerRoute(routeAllUsers, factory)
    dispatcher.registerRoute(routeAllUsers, action) // We registered action for user route too!

    try {
        dispatcher.start()
    } catch (e: Exception) {
        logger.e(LOG_TAG, "Error: " + e)
    }

}
