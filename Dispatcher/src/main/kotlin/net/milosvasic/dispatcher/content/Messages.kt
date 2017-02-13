package net.milosvasic.dispatcher.content


internal object Messages {

    val OK = "OK"
    val ERROR_404 = "Not found."
    val METHOD_NOT_SUPPORTED = "Method not supported."
    val DISPATCHER_TERMINATED = "Dispatcher is terminated."
    val ACTION_ROUTES_SHADOWING = "Action routes shadowing."
    val RESPONSE_ROUTES_SHADOWING = "Response routes shadowing."
    val DISPATCHER_RUNNING = "${Labels.DISPATCHER} is running."
    val DISPATCHER_NOT_RUNNING = "${Labels.DISPATCHER} is not running."
    val DISPATCHER_ALREADY_RUNNING = "${Labels.DISPATCHER} is already running."

}