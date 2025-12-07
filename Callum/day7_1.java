import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class day7_1 {
    public static char SPLITTER = '^';

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(7, false).stream()
                .filter(l -> !l.chars().allMatch(c -> c == '.')).toList();
        Set<Integer> beams = Set.of(lines.getFirst().indexOf('S'));
        long splits = 0;
        for (String line : lines.subList(1, lines.size())) {
            Set<Integer> newBeams = new HashSet<>();
            for (Integer beam : beams) {
                if (line.charAt(beam) == SPLITTER) {
                    splits++;
                    newBeams.add(beam-1);
                    newBeams.add(beam+1);
                }
                else {
                    newBeams.add(beam);
                }
            }
            beams = newBeams;
        }
        return splits;
    }
}
