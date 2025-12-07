import java.io.IOException;
import java.util.*;

public class day7_2 {
    public static char SPLITTER = '^';

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(7, false).stream()
                .filter(l -> !l.chars().allMatch(c -> c == '.')).toList();
        int lineLength = lines.getFirst().length();
        List<Long> beams = new ArrayList<>(Collections.nCopies(lineLength, 0L));
        beams.set(lines.getFirst().indexOf('S'), 1L);
        for (String line : lines.subList(1, lines.size())) {
            for (int i = 0; i < beams.size(); i++) {
                if (line.charAt(i) == SPLITTER) {
                    beams.set(i-1, beams.get(i-1) + beams.get(i));
                    beams.set(i+1, beams.get(i+1) + beams.get(i));
                    beams.set(i, 0L);
                }
            }
        }
        return beams.stream().mapToLong(l -> l).sum();
    }
}
