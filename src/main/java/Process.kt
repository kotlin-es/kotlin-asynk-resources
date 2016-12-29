import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Created by vicboma on 29/12/16.
 */
class Process {

    companion object {

        private val BUFFER = 1024

        fun  execute(pair: Pair<String, String>): File {
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



}