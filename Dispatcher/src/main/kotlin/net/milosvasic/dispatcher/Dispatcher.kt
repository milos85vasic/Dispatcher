package net.milosvasic.dispatcher

import com.sun.net.httpserver.HttpServer
import net.milosvasic.dispatcher.content.Labels
import net.milosvasic.dispatcher.content.Messages
import net.milosvasic.dispatcher.executors.TaskExecutor
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.Route
import net.milosvasic.logger.ConsoleLogger
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Dispatcher : DispatcherAbstract {
    private val logger = ConsoleLogger()

    private var server: HttpServer? = null
    private val LOG_TAG = Dispatcher::class
    private val executor = TaskExecutor.instance(10)
    private val routes = ConcurrentHashMap<Route, ResponseFactory>()

    override fun start(port: Int) {
        if (server == null) {
            server = HttpServer.create(InetSocketAddress(port), 0)
            server?.executor = executor

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
        } else {
            throw IllegalStateException(Messages.DISPATCHER_ALREADY_RUNNING)
        }
    }

    override fun stop() {
        server?.stop(0)
    }

    override fun registerRoute(route: Route, responseFactory: ResponseFactory) {
        routes.put(route, responseFactory)
    }

}