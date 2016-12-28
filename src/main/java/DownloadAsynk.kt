
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.function.Supplier


/**
 * Created by vicboma on 26/12/16.
 */
object DownloadAsynk {

    private val BUFFER = 1024
    private val MILLISECONDS_THREAD_SLEEP  = 100
    private val MILLISECOND_THREAD_SLEEP_TASK_RESOLVED = 25
    private var queue: ConcurrentLinkedQueue<Pair<CompletableFuture<File>,Pair<String, String>>>? = null

    val cachedLazyPool by lazy { Executors.newCachedThreadPool() }

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
                        val res = processSingleAttachment(poll.second)?.get()
                        poll.first.complete(res)
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

    fun submit(pair: Pair<String,String>): CompletableFuture<File> {
        val completableFuture = CompletableFuture<File>()
        var pair = Pair(completableFuture,pair)
        queue?.add(pair)
        return completableFuture
    }

    fun submit(vararg pairs : Pair<String, String>): List<CompletableFuture<File>> {
        val listCompletable = ArrayList<CompletableFuture<File>>()
        for(it in pairs){
            val res = submit(it)
            listCompletable.add(res)
        }

        return listCompletable
    }


    private fun processSingleAttachment(map: Pair<String,String> ) =  CompletableFuture.supplyAsync(Supplier { execute(map) } ,cachedLazyPool)


    fun execute(pair: Pair<String, String>): File {
        System.out.println("Downloading Resource " + pair.first)
        val `in` = BufferedInputStream(URL(pair.first).openStream())
        val fos = FileOutputStream(pair.second)
        val bout = BufferedOutputStream(fos)

        val data = ByteArray(BUFFER)
        while( readFile(`in`,data, bout) >= 0);

        bout.close()
        `in`.close()

        System.out.println("Download Resource " + pair.second)

        return File(pair.second)
    }

    private fun readFile(`in`: BufferedInputStream, data :ByteArray, bout : BufferedOutputStream) : Int {
        val res = `in`.read(data, 0, BUFFER)
        when {
            res.isRange() -> bout.write(data, 0, res)
            else -> { }
        }

        return res;
    }

    private fun Int.isRange() = this >= 0

}

