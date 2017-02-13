package net.milosvasic.dispatcher

import net.milosvasic.dispatcher.route.Routing

abstract class DispatcherAbstract(val instanceName: String, val port: Int) : Routing, Naming {

    abstract fun start()

    abstract fun stop()

}