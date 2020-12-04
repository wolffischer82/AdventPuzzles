import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class Day2 {

    public static void main (String []args) throws IOException
    {
        List<String> input = Util.readFile("src/Day2Input");
        int match = 0;
        int match2 = 0;
        for (String in : input)
        {
            String []parts = in.split(" ");
            String []numbers = parts[0].split("-");
            int min = Integer.parseInt(numbers[0]);
            int max = Integer.parseInt(numbers[1]);
            String find = parts[1].substring(0, 1);
            int occurrences = 0;
            // Part 1
            for (int i = 0; i < parts[2].length(); i++)
            {
                String nextChar = parts[2].substring(i, i+1);
                if (nextChar.equals(find))
                {
                    occurrences++;
                }
            }
            if (occurrences >= min && occurrences <= max)
            {
                match++;
            }

            // Part 2
            min--;
            max--;
            String substr1 = parts[2].substring(min, min+1);
            String substr2 = parts[2].substring(max, max+1);
            if ((substr1.equals(find) && !substr2.equals(find))
                || (!substr1.equals(find) && substr2.equals(find)))
            {
                match2++;
            }
        }
        System.out.println("There were " + match + " correct passwordstrings!");
        System.out.println("V2: There were " + match2 + " correct passwordstrings!");
    }
}
