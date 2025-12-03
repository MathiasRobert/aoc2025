import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class day3_1 {

    public static long getJoltage() throws IOException {
        File inputFile = new File("Callum/inputs/day3_1.txt");
        List<String> lines = Files.lines(inputFile.toPath()).toList();
        long result = 0;

        for (String line : lines) {
            String firstDigit = findBiggestDigit(line.substring(0, line.length()-1));
            String secondDigit = findBiggestDigit(line.substring(line.indexOf(firstDigit)+1));
            String number = firstDigit + secondDigit;
            result += Long.parseLong(number);
        }
        return result;
    }

    private static String findBiggestDigit(String substring) {
        return String.valueOf((char) substring.chars().max().getAsInt());
    }
}
