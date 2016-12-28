
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Created by vicboma on 26/12/16.
 */
class DownloadAsynk internal constructor() {

    companion object {
        private val MILLISECONDS_THREAD_SLEEP  = 100
        private val MILLISECOND_THREAD_SLEEP_TASK_RESOLVED = 25

        private var queue: ConcurrentLinkedQueue<Map<CompletableFuture<File>, Map.Entry<String,String>>>? = null

        fun create() = DownloadAsynk()
    }

    init {
        queue = ConcurrentLinkedQueue()
        processData()
    }

    private fun processData() {
        val thread = Thread {

            while (!Thread.currentThread().isInterrupted) {

                val _priorityQueue = queue

                if (_priorityQueue!!.isEmpty()) {
                    try {
                        Thread.sleep(MILLISECONDS_THREAD_SLEEP.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                } else {
                    val size = _priorityQueue.size
                    for (i in 0..size - 1) {
                        var poll = _priorityQueue.poll()
                        for(entry in poll.entries){
                            val res = processSingleAttachment(entry.value)?.get()
                            entry.key.complete(res)
                        }
                    }

                    try {
                        Thread.sleep(MILLISECOND_THREAD_SLEEP_TASK_RESOLVED.toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
        }

        thread.isDaemon = true
        thread.start()

    }

    fun submit(elem: Map.Entry<String,String>): CompletableFuture<File> {
        val completableFuture = CompletableFuture<File>()
        val pair = object : HashMap<CompletableFuture<File>, Map.Entry<String,String>>() {
            init {
                put(completableFuture, elem)
            }
        }
        queue?.add(pair)
        return completableFuture
    }

    fun submit(map: Map<String, String>): List<CompletableFuture<File>> {
        val listCompletable = ArrayList<CompletableFuture<File>>()
        for(it in map.entries){
            val res = submit(it)
            listCompletable.add(res)
        }

        return listCompletable
    }


    private fun processSingleAttachment(map: Map.Entry<String,String> ) =  CompletableFuture.supplyAsync { Util.execute(map) }

}