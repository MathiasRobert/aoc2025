import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class day4_2 {

    public static long getResult() throws IOException {
        File inputFile = new File("Callum/inputs/day4.txt");
        List<String> rawInputLines = Files.lines(inputFile.toPath()).toList();
        List<List<Integer>> inputLines = rawInputLines.stream().map(
                s -> s.chars().mapToObj(c -> c == '.' ? (Integer) 0 : (Integer) 1)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
        long output = 0;
        long previousOutput = -1;
        while (output != previousOutput) {
            previousOutput = output;
            for (int i = 0; i < inputLines.size(); i++) {
                for (int j = 0; j < inputLines.get(i).size(); j++) {
                    if (inputLines.get(i).get(j) == 1) {
                        int adjacentRolls = day4_1.getAdjacentRolls(inputLines, i, j);
                        if (adjacentRolls < 4){
                            output++;
                            inputLines.get(i).set(j, 0);
                        }
                    }
                }
            }
        }
        return output;
    }
}
