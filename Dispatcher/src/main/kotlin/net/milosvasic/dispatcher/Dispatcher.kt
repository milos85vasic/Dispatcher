package net.milosvasic.dispatcher


import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import net.milosvasic.dispatcher.content.Messages
import net.milosvasic.dispatcher.executors.TaskExecutor
import net.milosvasic.dispatcher.request.REQUEST_METHOD
import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.Route
import net.milosvasic.logger.ConsoleLogger
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean


class Dispatcher(port: Int) : DispatcherAbstract(port) {

    private val logger = ConsoleLogger()
    private val LOG_TAG = Dispatcher::class
    private val running = AtomicBoolean(false)
    private val executor = TaskExecutor.instance(10)
    private val actionRoutes = ConcurrentHashMap<Route, ResponseAction>()
    private val responseRoutes = ConcurrentHashMap<Route, ResponseFactory>()
    private val server: HttpServer = HttpServer.create(InetSocketAddress(port), 0)

    private val hook = Thread(Runnable {
        try {
            stop()
        } catch (e: Exception) {
            // Ignore
        }
        logger.d(LOG_TAG, Messages.DISPATCHER_TERMINATED)
    })

    init {
        server.executor = executor
        server.createContext("/", { exchange -> handleExchange(exchange) })
        Runtime.getRuntime().addShutdownHook(hook)
    }

    override fun start() {
        if (!running.get()) {
            server.start()
            running.set(true)
            logger.v(LOG_TAG, Messages.DISPATCHER_RUNNING)
        } else {
            throw IllegalStateException(Messages.DISPATCHER_ALREADY_RUNNING)
        }
    }

    override fun stop() {
        if (running.get()) {
            server.stop(0)
            running.set(false)
        } else {
            throw IllegalStateException(Messages.DISPATCHER_NOT_RUNNING)
        }
    }

    override fun registerRoute(route: Route, responseFactory: ResponseFactory) {
        responseRoutes.put(route, responseFactory)
    }

    override fun registerRoute(route: Route, responseAction: ResponseAction) {
        actionRoutes.put(route, responseAction)
    }

    private fun handleExchange(exchange: HttpExchange) {
        when (exchange.requestMethod) {
            REQUEST_METHOD.GET -> {
                val code: Int
                val response: String
                val route = getRoute(exchange)
                if (route != null) {
                    code = 200
                    response = getResponse(route)
                    actionRoutes[route]?.onAction()
                } else {
                    code = 404
                    response = Messages.ERROR_404
                }
                exchange.sendResponseHeaders(code, response.length.toLong())
                sendResponse(exchange, response)
            }
            else -> {
                val response = "${Messages.METHOD_NOT_SUPPORTED} Method [ ${exchange.requestMethod} ]"
                exchange.sendResponseHeaders(501, response.length.toLong())
                sendResponse(exchange, response)
            }
        }
    }

    private fun getRoute(exchange: HttpExchange): Route? {
        return null // TODO: Handle this
    }

    private fun getResponse(route: Route): String {
        // TODO: Handle this
        return Messages.OK
    }

    private fun sendResponse(exchange: HttpExchange, response: String) {
        val os = exchange.responseBody
        os?.write(response.toByteArray())
        os?.close()
    }

}