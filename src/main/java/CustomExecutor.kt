
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
/**
 * Created by vicboma on 29/12/16.
 */
class CustomExecutor<K>{

    private val MILLISECONDS_THREAD_SLEEP  = 75
    private val MILLISECOND_THREAD_SLEEP_TASK_RESOLVED = 25

    private var queue: ConcurrentLinkedQueue<Pair<CompletableFuture<K>,() -> K >>? = null

    val thread by lazy {

        Thread {

            while (!Thread.currentThread().isInterrupted) {

                val _priorityQueue = queue

                if (_priorityQueue!!.isEmpty()) {
                    try {
                        Thread.sleep(MILLISECONDS_THREAD_SLEEP.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    var poll : Pair<CompletableFuture<K>,() -> K > ? = null
                    try {
                        CompletableFuture.runAsync {
                            val size = _priorityQueue.size
                            for (i in 0..size - 1) {
                                poll = _priorityQueue.poll()
                                val res = poll!!.second.invoke()
                                poll!!.first.complete(res)
                            }
                        }
                    } catch(e: java.lang.Exception){
                        _priorityQueue.add(poll)
                    }
                    finally {

                        try {
                            Thread.sleep(MILLISECOND_THREAD_SLEEP_TASK_RESOLVED.toLong())
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }


    init {
        queue = ConcurrentLinkedQueue()
        thread.apply{
            isDaemon = true
            start()
        }
    }

    fun add(processSingle : () -> K)  : CompletableFuture<K> {
        val completableFuture = CompletableFuture<K>()
        var _pair = Pair(completableFuture,processSingle)
        queue?.add(_pair)
        return completableFuture
    }
}
