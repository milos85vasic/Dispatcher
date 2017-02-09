package net.milosvasic.dispatcher

import com.sun.net.httpserver.HttpServer
import net.milosvasic.dispatcher.content.Labels
import net.milosvasic.dispatcher.content.Messages
import net.milosvasic.dispatcher.executors.TaskExecutor
import net.milosvasic.dispatcher.routing.Route
import net.milosvasic.logger.ConsoleLogger
import java.net.InetSocketAddress
import java.util.*

class Dispatcher : DispatcherAbstract {
    private val logger = ConsoleLogger()

    private var server: HttpServer? = null
    private val LOG_TAG = Dispatcher::class
    private val executor = TaskExecutor.instance(10)
    private val routes = Collections.synchronizedSet(HashSet<Route>())

    override fun start(port: Int) {
        server = HttpServer.create(InetSocketAddress(port), 0)
        server?.executor = executor
        if (server != null) {
            Runtime.getRuntime().addShutdownHook(Thread(Runnable {
                stop()
                logger.d(LOG_TAG, Messages.DISPATCHER_TERMINATED)
                System.exit(0)
            }))
            Thread(Runnable {
                Thread.currentThread().name = Labels.DISPATCHER_STARTING_THREAD
                server?.start()
                logger.v(LOG_TAG, Messages.DISPATCHER_RUNNING)
            }).start()
        }
    }

    override fun stop() {
        server?.stop(0)
    }

    override fun addRoute(route: Route) {
        routes.add(route)
    }

}