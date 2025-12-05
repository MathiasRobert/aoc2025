import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class util {
    public static String FILE_PATH = "Callum/inputs/day";
    public static String TEST = "_test";
    public static String FILE_EXTENTION = ".txt";

    public static File getFile(int dayNumber, boolean test) {
        String path = FILE_PATH + dayNumber + (test ? TEST : "") + FILE_EXTENTION;
        return new File(path);
    }

    public static List<String> getFileLines(int dayNumber, boolean test) throws IOException {
        return Files.lines(getFile(dayNumber, test).toPath()).toList();
    }
}
