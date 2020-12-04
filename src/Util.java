import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    public static List<String> readWebpage(String url) throws IOException
    {
        var url2 = new URL(url);
        List<String> result = new ArrayList<>();
        HttpsURLConnection conn = (HttpsURLConnection)url2.openConnection();
        InputStream is = conn.getInputStream();
        try (var br = new BufferedReader(new InputStreamReader(is))) {

            String line;

            var sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                result.add(line);
            }
        }
        return result;
    }

    public static List<String> readFile(String filename) throws FileNotFoundException
    {
        File myObj = new File(filename);
        System.out.println("Reading file: " + myObj.getAbsolutePath());
        List<String> result = new ArrayList<>();
        var myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            result.add(data);
        }
        return result;
    }
}
