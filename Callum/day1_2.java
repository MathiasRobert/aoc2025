import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class day1_2 {

    public static Integer getRealCode() throws IOException {
        List<Integer> inputs = getInputs();
        int code = 0;
        int currentPosition = 50;
        for (int move : inputs) {
            if (move != 0) {
                int newPosition = currentPosition + move;
                code += countNewPassesOfZero(currentPosition, newPosition);
                currentPosition = adjustNewPosition(newPosition);
            }
        }
        return code;
    }

    private static int adjustNewPosition(int newPosition) {
        newPosition = newPosition % 100;
        if (newPosition < 0)
            newPosition = 100 + newPosition;
        return newPosition;
    }

    private static int countNewPassesOfZero(int currentPosition, int newPosition) {
        if (newPosition == 0) return 1;
        else {
            int rotations = Math.abs(newPosition / 100);
            if (newPosition < 0)
                rotations += currentPosition == 0 ? 0 : 1;
            return rotations;
        }
    }

    private static List<Integer> getInputs() throws IOException {
        File inputFile = new File("inputs/day1_1.txt");
        List<String> rawInputs = Files.lines(inputFile.toPath()).toList();
        return rawInputs.stream().map(s -> {
            s = s.replace("L", "-");
            s = s.replace("R", "");
            return Integer.parseInt(s);
        }).collect(Collectors.toList());
    }
}
