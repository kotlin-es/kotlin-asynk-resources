import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Created by vicboma on 26/12/16.
 */
object DownloadAsynk {

    val threadPoll = CustomExecutor<File>()

    init {

    }

    fun submit(pair: Pair<String,String>): CompletableFuture<File> = threadPoll.add { Process.execute(pair)     }



    fun submit(vararg pairs : Pair<String, String>): List<CompletableFuture<File>> {
        val listCompletable = ArrayList<CompletableFuture<File>>()

        for(it in pairs){
            val res = submit(it)
            listCompletable.add(res)
        }

        return listCompletable
    }


    //suspend fun <T> coroutine(f:  suspend () -> T): T = suspendCoroutine<T> { f.startCoroutine(it) }

}

