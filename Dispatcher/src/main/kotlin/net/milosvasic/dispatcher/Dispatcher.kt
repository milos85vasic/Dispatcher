package net.milosvasic.dispatcher


import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import net.milosvasic.dispatcher.content.Labels
import net.milosvasic.dispatcher.content.Messages
import net.milosvasic.dispatcher.executors.TaskExecutor
import net.milosvasic.dispatcher.logging.DispatcherLogger
import net.milosvasic.dispatcher.request.REQUEST_METHOD
import net.milosvasic.dispatcher.request.RequestPath
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseAbstract
import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.response.assets.AssetFactory
import net.milosvasic.dispatcher.route.AssetsRoute
import net.milosvasic.dispatcher.route.DynamicRouteElement
import net.milosvasic.dispatcher.route.Route
import net.milosvasic.dispatcher.route.RouteElement
import net.milosvasic.dispatcher.route.exception.RouteUnregisterException
import net.milosvasic.logger.Logger
import java.io.ByteArrayInputStream
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern


class Dispatcher(instanceName: String, port: Int) : DispatcherAbstract(instanceName, port) {

    private val LOG_TAG = Dispatcher::class
    private val running = AtomicBoolean(false)
    var logger: Logger = DispatcherLogger(this)
    private val executor = TaskExecutor.instance(10)
    private val actionRoutes = ConcurrentHashMap<Route, ResponseAction>()
    private val assetRoutes = ConcurrentHashMap<AssetsRoute, AssetFactory>()
    private val responseRoutes = ConcurrentHashMap<Route, ResponseFactory>()
    private val server: HttpServer = HttpServer.create(InetSocketAddress(port), 0)

    private val hook = Thread(Runnable {
        try {
            stop()
        } catch (e: Exception) {
            // Ignore
        }
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
            logger.c(LOG_TAG, Messages.DISPATCHER_RUNNING)
        } else {
            throw IllegalStateException(Messages.DISPATCHER_ALREADY_RUNNING)
        }
    }

    override fun stop() {
        if (running.get()) {
            server.stop(0)
            running.set(false)
            logger.c(LOG_TAG, Messages.DISPATCHER_TERMINATED)
        } else {
            throw IllegalStateException(Messages.DISPATCHER_NOT_RUNNING)
        }
    }

    override fun isRunning(): Boolean {
        return running.get()
    }

    override fun registerRoute(route: Route, responseFactory: ResponseFactory): Boolean {
        responseRoutes.put(route, responseFactory)
        val result = responseRoutes.keys.contains(route)
        val message = "${Labels.ROUTE} [ $route ][ ${Labels.REGISTER.toUpperCase()} ][ $result ][ ${Labels.RESPONSE_FACTORY} ]"
        if (result) {
            logger.i(LOG_TAG, message)
        } else {
            logger.w(LOG_TAG, message)
        }
        return result
    }

    override fun registerRoute(route: Route, responseAction: ResponseAction): Boolean {
        actionRoutes.put(route, responseAction)
        val result = actionRoutes.keys.contains(route)
        val message = "${Labels.ROUTE} [ $route ][ ${Labels.REGISTER.toUpperCase()} ][ $result ][ ${Labels.RESPONSE_ACTION} ]"
        if (result) {
            logger.i(LOG_TAG, message)
        } else {
            logger.w(LOG_TAG, message)
        }
        return result
    }

    override fun registerRoute(route: AssetsRoute, assetFactory: AssetFactory): Boolean {
        assetRoutes.put(route, assetFactory)
        val result = assetRoutes.keys.contains(route)
        val message = "${Labels.ROUTE} [ $route ][ ${Labels.REGISTER.toUpperCase()} ][ $result ][ ${Labels.RESPONSE_ASSET} ]"
        if (result) {
            logger.i(LOG_TAG, message)
        } else {
            logger.w(LOG_TAG, message)
        }
        return result
    }

    override fun unregisterRoute(route: Route): Boolean {
        var success = false
        if (actionRoutes.keys.contains(route)) {
            success = actionRoutes.remove(route) != null
            if (!success) {
                throw RouteUnregisterException()
            }
        }
        if (responseRoutes.keys.contains(route)) {
            success = responseRoutes.remove(route) != null
            if (!success) {
                throw RouteUnregisterException()
            }
        }
        logger.i(LOG_TAG, "${Labels.ROUTE} [ $route ][ ${Labels.UNREGISTER.toUpperCase()} ][ $success ]")
        return success
    }

    override fun unregisterRoute(route: Route, responseFactory: ResponseFactory): Boolean {
        var success = false
        if (responseRoutes.keys.contains(route)) {
            success = responseRoutes.remove(route, responseFactory)
            if (!success) {
                throw RouteUnregisterException()
            }
        }
        val message = "${Labels.ROUTE}  [ $route ][ ${Labels.UNREGISTER.toUpperCase()} ][ $success ][ ${Labels.RESPONSE_FACTORY} ]"
        logger.i(LOG_TAG, message)
        return success
    }

    override fun unregisterRoute(route: Route, responseAction: ResponseAction): Boolean {
        var success = false
        if (actionRoutes.keys.contains(route)) {
            success = actionRoutes.remove(route, responseAction)
            if (!success) {
                throw RouteUnregisterException()
            }
        }
        val message = "${Labels.ROUTE} [ $route ][ ${Labels.UNREGISTER.toUpperCase()} ][ $success ][ ${Labels.RESPONSE_ACTION} ]"
        logger.i(LOG_TAG, message)
        return success
    }

    override fun unregisterRoute(route: AssetsRoute, assetFactory: AssetFactory): Boolean {
        var success = false
        if (assetRoutes.keys.contains(route)) {
            success = assetRoutes.remove(route, assetFactory)
            if (!success) {
                throw RouteUnregisterException()
            }
        }
        val message = "${Labels.ROUTE}  [ $route ][ ${Labels.UNREGISTER.toUpperCase()} ][ $success ][ ${Labels.RESPONSE_ASSET} ]"
        logger.i(LOG_TAG, message)
        return success
    }

    override fun getName(): String {
        return instanceName
    }

    private fun handleExchange(exchange: HttpExchange) {
        logger.v(LOG_TAG, ">>> [ ${exchange.requestMethod} ] ${exchange.requestURI}")
        when (exchange.requestMethod) {
            REQUEST_METHOD.GET -> {
                val route = getRoute(exchange)
                if (route != null) {
                    val params = getParams(route, RequestPath(exchange))
                    val routeResponse = responseRoutes[route]
                    val assetResponse = assetRoutes[route]
                    if (routeResponse != null) {
                        val response = routeResponse.getResponse(params)
                        try {
                            sendResponse(exchange, response)
                        } catch (e: Exception) {
                            logger.e(LOG_TAG, "${Labels.ERROR}: $e")
                        }
                    } else if (assetResponse != null) {
                        val response = assetResponse.getContent(params)
                        try {
                            sendResponse(exchange, response)
                        } catch (e: Exception) {
                            logger.e(LOG_TAG, "${Labels.ERROR}: $e")
                        }
                    } else {
                        confirmation(exchange)
                    }
                    actionRoutes[route]?.onAction()
                } else {
                    error_404(exchange)
                }
                val code = exchange.responseCode
                logger.v(LOG_TAG, "<<< [ $code ] ${exchange.requestURI}")
            }
            else -> {
                val message = "${Messages.METHOD_NOT_SUPPORTED} Method [ ${exchange.requestMethod} ]"
                val response = Response(message, 501)
                sendResponse(exchange, response)
            }
        }
    }

    private fun getRoute(exchange: HttpExchange): Route? {
        val routesSet = LinkedHashSet<Route>()
        if (!responseRoutes.isEmpty() || !actionRoutes.isEmpty() || !assetRoutes.isEmpty()) {
            val path = RequestPath(exchange)

            val responseRoutesSet = responseRoutes.keys
                    .filter { matchRoute(it, path.value) }
                    .toSet()

            val actionRoutesSet = actionRoutes.keys
                    .filter { matchRoute(it, path.value) }
                    .toSet()

            val assetsRoutesSet = assetRoutes.keys
                    .filter { matchRoute(it, path.value) }
                    .toSet()

            routesSet.addAll(actionRoutesSet)
            routesSet.addAll(responseRoutesSet)
            routesSet.addAll(assetsRoutesSet)

            if (actionRoutesSet.size > 1) logger.w(LOG_TAG, Messages.ACTION_ROUTES_SHADOWING)
            if (assetsRoutesSet.size > 1) logger.w(LOG_TAG, Messages.ASSETS_ROUTES_SHADOWING)
            if (responseRoutesSet.size > 1) logger.w(LOG_TAG, Messages.RESPONSE_ROUTES_SHADOWING)
        }
        if (!routesSet.isEmpty()) {
            return routesSet.first()
        }
        return null
    }

    private fun getParams(route: Route, path: RequestPath): HashMap<RouteElement, String> {
        val params = HashMap<RouteElement, String>()
        try {
            val regex = route.getRegex()
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(path.value)
            if (matcher.matches()) {
                route.getElements().forEachIndexed {
                    index, element ->
                    if (element is DynamicRouteElement) {
                        params.put(element, matcher.group(index + 1))
                    }
                }
            }
        } catch (e: Exception) {
            logger.e(LOG_TAG, e.toString())
        }
        return params
    }

    private fun matchRoute(route: Route, path: String?): Boolean {
        try {
            val regex = route.getRegex()
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(path)
            return matcher.matches()
        } catch (e: Exception) {
            logger.e(LOG_TAG, e.toString())
        }
        return false
    }

    private fun sendResponse(exchange: HttpExchange, response: ResponseAbstract<*>) {
        val bytes = response.getBytes()
        if (bytes == null || bytes.isEmpty()) {
            error_404(exchange)
        } else {
            for (key in response.headers.keys) {
                exchange.responseHeaders[key] = response.headers[key]
            }
            exchange.sendResponseHeaders(response.code, 0)
            val output = exchange.responseBody
            val input = ByteArrayInputStream(bytes)
            input.copyTo(output)
            input.close()
            output.close()
        }
    }

    private fun error_404(exchange: HttpExchange) {
        exchange.sendResponseHeaders(404, 0)
        val output = exchange.responseBody
        val input = ByteArrayInputStream(Messages.ERROR_404.toByteArray())
        input.copyTo(output)
        input.close()
        output.close()
    }

    private fun confirmation(exchange: HttpExchange) {
        exchange.sendResponseHeaders(200, 0)
        val output = exchange.responseBody
        val input = ByteArrayInputStream(Messages.OK.toByteArray())
        input.copyTo(output)
        input.close()
        output.close()
    }

}