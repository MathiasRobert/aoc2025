import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class day2_2 {
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
                for (int j = 1; j <= iLength/2; j++) {
                    if (iLength % j == 0) {
                        List<String> iChunks = Arrays.asList(stringifiedI.split("(?<=\\G.{" + j + "})"));
                        if (iChunks.stream().allMatch(c -> c.equals(iChunks.getFirst()))) {
                            output += i;
                            break;
                        }
                    }
                }
            }
        }
        return output;
    }
}
