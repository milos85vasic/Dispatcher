package net.milosvasic.dispatcher.logging


import net.milosvasic.dispatcher.Naming
import net.milosvasic.dispatcher.content.Labels
import net.milosvasic.logger.ConsoleLogger
import net.milosvasic.logger.FilesystemLogger
import net.milosvasic.logger.Logger
import java.io.File
import kotlin.reflect.KClass

internal class DispatcherLogger(val naming: Naming) : Logger {

    private val logger = ConsoleLogger()
    private val loggerFs = FilesystemLogger(getHome())

    override fun c(tag: KClass<*>, message: String) {
        logger.c(tag, message)
        loggerFs.c(tag, message)
    }

    override fun d(tag: KClass<*>, message: String) {
        logger.d(tag, message)
        loggerFs.d(tag, message)
    }

    override fun e(tag: KClass<*>, message: String) {
        logger.e(tag, message)
        loggerFs.e(tag, message)
    }

    override fun i(tag: KClass<*>, message: String) {
        logger.i(tag, message)
        loggerFs.i(tag, message)
    }

    override fun n(tag: KClass<*>, message: String) {
        logger.n(tag, message)
        loggerFs.n(tag, message)
    }

    override fun v(tag: KClass<*>, message: String) {
        logger.v(tag, message)
        loggerFs.v(tag, message)
    }

    override fun w(tag: KClass<*>, message: String) {
        logger.w(tag, message)
        loggerFs.w(tag, message)
    }

    fun getHome(): File {
        val home = System.getProperty("user.home")
        val builder = StringBuilder()
                .append(home)
                .append(File.separator)
                .append(naming.getName())
                .append(File.separator)
                .append(Labels.LOGS)
        val root = File(builder.toString())
        if (!root.exists()) {
            root.mkdirs()
        }
        return root
    }

}

