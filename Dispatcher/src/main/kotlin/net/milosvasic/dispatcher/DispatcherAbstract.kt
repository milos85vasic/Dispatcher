package net.milosvasic.dispatcher

import net.milosvasic.dispatcher.route.Routing


abstract class DispatcherAbstract(val port: Int) : Routing {

    abstract fun start()

    abstract fun stop()

}