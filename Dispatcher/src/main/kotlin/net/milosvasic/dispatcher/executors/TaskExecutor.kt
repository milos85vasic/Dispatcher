package net.milosvasic.dispatcher.executors

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor private constructor(corePoolSize: Int, maximumPoolSize: Int, queue: BlockingQueue<Runnable>) : ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0, TimeUnit.MILLISECONDS, queue) {

    companion object {
        fun instance(capacity: Int): TaskExecutor {
            return TaskExecutor(capacity, capacity * 2, LinkedBlockingDeque<Runnable>())
        }
    }

}