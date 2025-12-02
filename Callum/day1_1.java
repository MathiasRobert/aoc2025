import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class day1_1 {

    public static Integer getRealCode() throws IOException {
        File inputFile = new File("inputs/day1_1.txt");
        List<String> rawInputs = Files.lines(inputFile.toPath()).toList();
        List<Integer> inputs = rawInputs.stream().map(s -> {
                    s = s.replace("L", "-");
                    s = s.replace("R", "");
                    return Integer.parseInt(s);
                }).toList();
        int code = 0;
        int currentPosition = 50;
        for (Integer input : inputs) {
            currentPosition = (currentPosition + input) % 100;
            if (currentPosition < 0) currentPosition = 100 + currentPosition;
            if (currentPosition == 0) code += 1;
        }
        return code;
    }
}
