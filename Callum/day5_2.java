import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class day5_2 {

    public static Long getResult() throws IOException {
        List<String> fileLines = util.getFileLines(5, false);
        List<Long> lowerBounds = new ArrayList<>();
        List<Long> upperBounds = new ArrayList<>();
        List<String> idLines = fileLines.subList(0, fileLines.indexOf(""));
        day5_1.setBounds(idLines, lowerBounds, upperBounds);
        long output = 0;
        for (int i = 0; i < lowerBounds.size(); i++) {
            output +=  upperBounds.get(i) - lowerBounds.get(i) + 1;
        }
        return output;
    }
}
