# Dispatcher
Dispatcher is simple http serving request/response library. 

Main purpose of this library is to be integrated into existing applications and give them possibility to respond on http requests.
Applications that integrate this library may respond with real http response or use request as trigger for other actions. 

Library is currently in the phase of development.

## What is supported?

- Library supports http GET as a mechanism to request something or to trigger an action.
- Static and dynamic routes
- Routes for assets (static and dynamic)

## How to use it?
- Import:
```
import net.milosvasic.dispatcher.Dispatcher
import net.milosvasic.dispatcher.response.Response
import net.milosvasic.dispatcher.response.ResponseAction
import net.milosvasic.dispatcher.response.ResponseFactory
import net.milosvasic.dispatcher.route.*
```
- Instantiate:
```
val dispatcher = Dispatcher("ServerName", 8080)
```
- Register routes:
```
dispatcher.registerRoute(route1, responseFactory)
dispatcher.registerRoute(route2, responseFactory)
dispatcher.registerRoute(route3, responseFactory)
dispatcher.registerRoute(route4, responseFactory)
dispatcher.registerRoute(route4, responseAction) // Bind route to action
dispatcher.registerRoute(route5, rsponseAction2) // This route only triggers action
```
NOTE: Route may be also registered after we started server!
- Unregister route (if we want):
```
dispatcher.unregisterRoute(someRoute) // Will be unregistered response factory and action both
dispatcher.unregisterRoute(someRoute, someAction) // Will be unregistered action associated to route
dispatcher.unregisterRoute(someRoute, someResponseFactory) // Will be unregistered response (factory) associated to route
```
- Start / Stop server:
```
try {
    dispatcher.start()
} catch (e: Exception) {
    // Handle exception
}
```
or
```
try {
    dispatcher.stop()
} catch (e: Exception) {
    // Handle exception
}
```
- Define route with response factory:
```
val accountsParams = "accounts"
val userParam = "user"
val attributesParam = "attributes"
val operationParam = "operation"

/* 
    We define route (as example):
    /accounts/{user}/attributes/{operation}
*/
val routeAccounts = Route.Builder()
        .addRouteElement(StaticRouteElement(accountsParams))
        .addRouteElement(DynamicRouteElement(userParam))
        .addRouteElement(StaticRouteElement(attributesParam))
        .addRouteElement(DynamicRouteElement(operationParam))
        .build()

// Response factory is responsible for providing response for the route
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

// We register our route
dispatcher.registerRoute(routeAccounts, factoryAccounts)
```
- Define route with response action:
```
// We define action we whish to trigger:
val action = object : ResponseAction {
    override fun onAction() {
        logger.v(LOG_TAG, "Action taken!")
    }
}

// Then we define our route:
val routeAllUsers = Route.Builder()
    .addRouteElement(StaticRouteElement("users"))
    .build()
    
// And register action to be triggered by the route:
dispatcher.registerRoute(routeAllUsers, action)
```
- Define assets routes:
```
// Dynanamic part of the route (for example: filename.gif)
val assetsFolder = DynamicRouteElement("Asset")

// Deining route: /Assets/{Asset}
val assets = AssetsRoute.Builder()
        .addRouteElement(StaticRouteElement("Assets"))
        .addRouteElement(assetsFolder)
        .build()

// Deining route: /Assets/Js/{Asset}
val assetsJs = AssetsRoute.Builder()
        .addRouteElement(StaticRouteElement("Assets"))
        .addRouteElement(StaticRouteElement("Js"))
        .addRouteElement(assetsFolder)
        .build()
        
// Assets static route for favicon
val faviconStaticRoute = StaticRouteElement("favicon.ico")
val favicon = AssetsRoute.Builder()
        .addRouteElement(faviconStaticRoute)
        .build()

// Define factories to get bytes for the assets:
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

val faviconResponse = object : AssetFactory {
    override fun getContent(params: HashMap<RouteElement, String>): Asset {
        val assetName = faviconStaticRoute.name
        val input = javaClass.classLoader.getResourceAsStream(assetName)
        return Asset(getBytes(input), 200)
    }
}

// Register assets routes
dispatcher.registerRoute(assets, assetsResponse)
dispatcher.registerRoute(assetsJs, assetsJsResponse)
dispatcher.registerRoute(favicon, faviconResponse)
```
## Logging
Dispatcher uses [Logger](https://github.com/milos85vasic/Logger) library for logging.
By default it logs to console output and filesystem both.
You can set your own logger:
```
dispatcher.logger = SomeLoggerImplementation()
```
