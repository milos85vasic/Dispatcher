package net.milosvasic.dispatcher.route.exception

import net.milosvasic.dispatcher.content.Messages

internal class NoBytesAvailableException(message: String = Messages.NO_BYTES_AVAILABLE_TO_SEND) : Exception(message)