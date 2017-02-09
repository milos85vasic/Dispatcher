package net.milosvasic.`try`.dispatcher

import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.routing.DynamicRouteElement
import net.milosvasic.dispatcher.routing.Route
import net.milosvasic.dispatcher.routing.StaticRouteElement
import net.milosvasic.logger.ConsoleLogger

private class TryDispatcher

fun main(args: Array<String>) {

    val LOG_TAG = TryDispatcher::class
    val logger = ConsoleLogger()

    // TODO: We should implement handling / entry point
    val root = Route.Builder().addRouteElement(StaticRouteElement("")).build()

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

    val dispatcher = Dispatcher()
    dispatcher.addRoute(routeUserRepos)
    dispatcher.addRoute(routeAllRepos)
    dispatcher.addRoute(routeAllUsers)
    dispatcher.addRoute(root)

    try {
        dispatcher.start(2507)
    } catch (e: Exception) {
        logger.e(LOG_TAG, "Error: " + e)
    }

}
