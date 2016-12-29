
import org.junit.Assert
import org.junit.Assert.fail
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Created by vicboma on 28/12/16.
 */
class CustomExecuteTest {

    val threadPoll = CustomExecutor<Integer>()

    @org.junit.Test
    fun testPrototype() {
        val obj1 = CustomExecutor<Integer>()
        val obj2 = threadPoll

        Assert.assertFalse( obj1 == obj2 )
        Assert.assertFalse( obj1.toString().equals(obj2.toString()))

    }

    private fun add(i:Int) : Integer = (i + 1) as Integer

    @org.junit.Test
    fun testAdd() {

        val listCompletable = ArrayList<Pair<Integer,CompletableFuture<Integer>>>()

        for (i in 0 .. 1000000) {
            listCompletable.add(
                    Pair((i + 1) as Integer ,threadPoll.add { add(i) })
            )
        }

        val runAsync = CompletableFuture.runAsync {
            for(_it in listCompletable){
                try {
                    val expected  = _it.first
                    val res = _it.second.get()
                   // System.out.println("Assert " +expected+" == "+res)
                    Assert.assertTrue(expected==res)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    fail()
                }
            }
        }

        System.out.println("End Test")

        runAsync.get()
    }
}