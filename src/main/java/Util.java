import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by vicboma on 28/12/16.
 */
public class Util {

    private static final int BUFFER = 1024;

    public static File execute(Map.Entry<String,String> map) throws Exception {
        System.out.println("Downloading Resource "+map.getKey());
        final BufferedInputStream in = new BufferedInputStream(new URL(map.getKey()).openStream());
        final FileOutputStream fos = new FileOutputStream(map.getValue());
        final BufferedOutputStream bout = new BufferedOutputStream(fos);

        byte data[] = new byte[BUFFER];
        int read;
        while ((read = in.read(data, 0, BUFFER)) >= 0) {
            bout.write(data, 0, read);
        }
        bout.close();
        in.close();

        System.out.println("Download Resource "+map.getValue());

        return new File(map.getValue());
    }
}
