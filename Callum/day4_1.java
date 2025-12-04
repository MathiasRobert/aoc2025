import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class day4_1 {

    public static long getResult() throws IOException {
        File inputFile = new File("Callum/inputs/day4.txt");
        List<String> rawInputLines = Files.lines(inputFile.toPath()).toList();
        List<List<Integer>> inputLines = rawInputLines.stream().map(
                s -> s.chars().mapToObj(c -> c == '.' ? (Integer) 0 : (Integer) 1).toList()).toList();
        long output = 0;
        for (int i = 0; i < inputLines.size(); i++) {
            for (int j = 0; j < inputLines.get(i).size(); j++) {
                if (inputLines.get(i).get(j) == 1) {
                    int adjacentRolls = getAdjacentRolls(inputLines, i, j);
                    if (adjacentRolls < 4)
                        output++;
                }
            }
        }
        return output;
    }

    public static int getAdjacentRolls(List<List<Integer>> inputLines, int i, int j) {
        int adjacentRolls = -inputLines.get(i).get(j);
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                try {
                    adjacentRolls += inputLines.get(i + k).get(j + l);
                } catch (Exception ignored) {}
            }
        }
        return adjacentRolls;
    }
}
