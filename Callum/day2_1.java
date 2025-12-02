import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class day2_1 {

    public static long getFakeEntriesSum() throws IOException {
        List<String> inputs = Files.lines(new File("Callum/inputs/day2_1.txt").toPath()).toList();
        String[] ranges = inputs.getFirst().split(",");
        long output = 0;
        for (String range : ranges) {
            List<String> startAndEnd = Arrays.asList(range.split("-"));
            long start = Long.parseLong(startAndEnd.getFirst());
            long end = Long.parseLong(startAndEnd.getLast());
            for (long i = start; i <= end; i++) {
                String stringifiedI = Long.toString(i);
                int iLength = stringifiedI.length();
                if (iLength % 2 == 0) {
                    if (stringifiedI.substring(0, iLength / 2).equals(stringifiedI.substring(iLength / 2, iLength)))
                        output += i;
                }
            }
        }
        return output;
    }
}
