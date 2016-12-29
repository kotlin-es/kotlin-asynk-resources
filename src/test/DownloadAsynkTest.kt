
import org.junit.Assert
import org.junit.Assert.fail
import java.util.concurrent.CompletableFuture

/**
 * Created by vicboma on 28/12/16.
 */
class DownloadAsynkTest {

    @org.junit.Test
    fun testSingleton() {
        val obj1 = DownloadAsynk
        val obj2 = DownloadAsynk

        Assert.assertTrue( obj1 == obj2 )
        Assert.assertTrue( obj1.toString().equals(obj2.toString()))

    }

    @org.junit.Test
    fun testSubmit() {
        val listCompletable = DownloadAsynk.submit(
           Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/AeroStar(J)%5B!%5D.zip","./src/Aero-Star.zip"),
           Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/Alien3(J)%5B!%5D.zip", "Alien-3.zip"),
           Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/SuperMarioLand2-6GoldenCoins(UE)(V1.2)%5B!%5D.zip","Super-Mario-Land-2-6-Golden-Coins.zip")
        )


        val runAsync = CompletableFuture.runAsync {
            for(_it in listCompletable){
                try {
                    val file = _it.get()
                    val res = file.exists()
                    System.out.println("Assert " + res.toString())
                    Assert.assertTrue(res)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    fail()
                }
            }
        }

        System.out.println("End Test")

        runAsync.get()
    }

    @org.junit.Test
        fun testSubmit2() {
                val completable = DownloadAsynk.submit(
                        Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/Alien3(J)%5B!%5D.zip", "./src/test/Alien-3.zip")
                )


                val runAsync = CompletableFuture.runAsync {
                            try {
                                    val file = completable.get()
                                    val res = file.exists()
                                    System.out.println("Assert " + res.toString())
                                    Assert.assertTrue(res)
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                    fail()
                                }
                    }


                System.out.println("End Test")

                runAsync.get()
            }

}