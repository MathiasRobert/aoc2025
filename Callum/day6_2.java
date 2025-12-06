import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class day6_2 {
    public static Character ADD = '+';
    public static Character MULTIPLY = '*';
    public static List<Character> operands = List.of(ADD, MULTIPLY);

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(6, false);
        List<List<Character>> characterArray = lines.stream().map(l -> l.chars().mapToObj(c -> (char) c).toList()).toList();
        List<List<String>> operations = getOperations(characterArray);
        return getOutput(operations);
    }

    private static List<List<String>> getOperations(List<List<Character>> characterArray) {
        List<List<String>> operations = new ArrayList<>();
        operations.add(new ArrayList<>());
        int currentOperation = 0;
        for (int i = characterArray.getFirst().size() - 1; i >= 0; i--) {
            String number = "";
            for (int j = 0; j < characterArray.size() - 1; j++) {
                if (!characterArray.get(j).get(i).equals(' ')) {
                    number += characterArray.get(j).get(i);
                }
            }
            if (!number.isEmpty())
                operations.get(currentOperation).add(number);
            if (operands.contains(characterArray.getLast().get(i))) {
                operations.get(currentOperation).add(String.valueOf(characterArray.getLast().get(i)));
                currentOperation++;
                operations.add(new ArrayList<>());
            }
        }
        operations.removeLast();
        return operations;
    }

    private static long getOutput(List<List<String>> operations) {
        long output = 0;
        for (List<String> operation : operations) {
            long result = Long.parseLong(operation.getFirst());
            for (int i = 1; i < operation.size() - 1; i++) {
                if (operation.getLast().equals(String.valueOf(ADD))) {
                    result += Long.parseLong(operation.get(i));
                }
                if (operation.getLast().equals(String.valueOf(MULTIPLY))) {
                    result *= Long.parseLong(operation.get(i));
                }
            }
            output += result;
        }
        return output;
    }
}
