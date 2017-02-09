package net.milosvasic.dispatcher

import net.milosvasic.dispatcher.route.Routing


interface DispatcherAbstract : Routing {

    fun start(port: Int)

    fun stop()

}