
import org.junit.Assert
import org.junit.Assert.fail
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Created by vicboma on 28/12/16.
 */
class DownloadAsynkTest {

    @org.junit.Test
    fun testSubmit() {

    }

    @org.junit.Test
    fun testSubmit1() {
        val listCompletable = DownloadAsynk.create().submit(
           object : HashMap<String, String>() {
                       init {
                           put("https://github.com/kotlin-es/kotlin-asynk-resources/blob/master/src/main/resource/AeroStar(J)%5B!%5D.zip", "Aero-Star.zip")
                           put("https://github.com/kotlin-es/kotlin-asynk-resources/blob/master/src/main/resource/Alien3(J)%5B!%5D.zip", "Alien-3.zip")
                           put("https://github.com/kotlin-es/kotlin-asynk-resources/blob/master/src/main/resource/SuperMarioLand2-6GoldenCoins(UE)(V1.2)%5B!%5D.zip","Super-Mario-Land-2-6-Golden-Coins.zip")
                       }
           }
        )


        val runAsync = CompletableFuture.runAsync {
            for(_it in listCompletable){
                try {
                    val file = _it.get()
                    val res = file.exists()
                    System.out.println("Assert " + res.toString())
                    Assert.assertTrue(res)
                } catch (e: Exception) {
                    e.printStackTrace()
                    fail()
                }
            }



        }

        System.out.println("End Test")

        runAsync.get()
    }

}