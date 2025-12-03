import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class day3_2 {

    public static long getJoltage() throws IOException {
        File inputFile = new File("Callum/inputs/day3_1.txt");
        List<String> lines = Files.lines(inputFile.toPath()).toList();
        long result = 0;

        for (String line : lines) {
            String number = "";
            int previousIndex = -1;
            for (int i = 11; i >= 0; i--) {
                String substring = line.substring(previousIndex + 1, line.length()-i);
                String digit = findBiggestDigit(substring);
                number += digit;
                previousIndex = substring.indexOf(digit) + previousIndex + 1;
            }
            result += Long.parseLong(number);
        }
        return result;
    }

    private static String findBiggestDigit(String substring) {
        return String.valueOf((char) substring.chars().max().getAsInt());
    }
}
