package net.milosvasic.dispatcher

import net.milosvasic.dispatcher.routing.Routing


interface DispatcherAbstract : Routing {

    fun start(port: Int)

    fun stop()

}