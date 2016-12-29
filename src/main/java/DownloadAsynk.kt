
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture


/**
 * Created by vicboma on 26/12/16.
 */
object DownloadAsynk {

    val thread by lazy {
        CustomExecutor<File, Pair<String,String>>({
            CompletableFuture.supplyAsync({ Process.execute(it) })
        })
    }

    init {

    }

    fun submit(pair: Pair<String,String>): CompletableFuture<File> {
        val completableFuture = CompletableFuture<File>()
        var pair = Pair(completableFuture,pair)
        thread.add(pair)
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

}

