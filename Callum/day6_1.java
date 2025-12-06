import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class day6_1 {
    public static String ADD = "+";
    public static String MULTIPLY = "*";

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(6, false);
        List<List<String>> splitLines = lines.stream().map(
                l -> Arrays.stream(l.split(" +")).filter(j -> !j.isEmpty()).toList()).toList();
        long output = 0;
        for (int i = 0; i < splitLines.getFirst().size(); i++) {
            String operand = splitLines.getLast().get(i);
            output += getResult(splitLines, i, operand);
        }
        return output;
    }

    private static long getResult(List<List<String>> splitLines, int i, String operand) {
        List<String> operation = new ArrayList<>();
        for (List<String> s : splitLines) {
            if (!s.get(i).contains(operand)) {
                operation.add(s.get(i));
                operation.add(operand);
            }
        }
        operation.removeLast();
        long result = Long.parseLong(operation.getFirst());
        for (int j = 1; j < operation.size(); j = j + 2) {
            if (operation.get(j).equals(ADD)) {
                result = result + Long.parseLong(operation.get(j + 1));
            }
            if (operation.get(j).equals(MULTIPLY)) {
                result = result * Long.parseLong(operation.get(j + 1));
            }
        }
        return result;
    }
}
