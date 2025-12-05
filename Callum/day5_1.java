import java.io.IOException;
import java.util.*;

public class day5_1 {

    public static Long getResult() throws IOException {
        List<String> fileLines = util.getFileLines(5, false);
        List<Long> lowerBounds = new ArrayList<>();
        List<Long> upperBounds = new ArrayList<>();
        List<String> idLines = fileLines.subList(0, fileLines.indexOf(""));
        setBounds(idLines, lowerBounds, upperBounds);
        List<Long> ids = fileLines.subList(fileLines.indexOf("") + 1, fileLines.size()).stream().map(Long::parseLong).toList();
        long output = 0;
        for (Long id : ids) {
            if (getPositionOfGreatestLowerThan(lowerBounds, id) >= 0 && upperBounds.get(getPositionOfGreatestLowerThan(lowerBounds, id)) >= id) {
                output++;
            }
        }
        return output;
    }

    public static void setBounds(List<String> idLines, List<Long> lowerBounds, List<Long> upperBounds) {
        for (String line : idLines) {
            List<Long> boundaries = Arrays.stream(line.split("-")).map(Long::parseLong).toList();
            if (lowerBounds.isEmpty()) {
                lowerBounds.add(boundaries.get(0));
                upperBounds.add(boundaries.get(1));
            } else {
                int lowerPosition = getPositionOfGreatestLowerThan(lowerBounds, boundaries.get(0));
                int upperPosition = getPositionOfSmallestGreaterThan(upperBounds, boundaries.get(1));
                if (lowerPosition != upperPosition) {
                    if (lowerPosition < 0 || boundaries.get(0) > upperBounds.get(lowerPosition)) {
                        if (lowerBounds.size() == upperPosition || boundaries.get(1) < lowerBounds.get(upperPosition))
                            setNewBounds(lowerBounds, upperBounds, upperPosition, lowerPosition, boundaries);
                        else {
                            extendUpperBound(lowerBounds, upperBounds, upperPosition, lowerPosition, boundaries);
                        }
                    } else {
                        if (boundaries.get(1) < lowerBounds.get(upperPosition))
                            extendLowerBound(lowerBounds, upperBounds, upperPosition, lowerPosition, boundaries);
                        else {
                            mergeBounds(lowerBounds, upperBounds, upperPosition, lowerPosition, boundaries);
                        }
                    }
                } else if (lowerPosition == lowerBounds.size()) {
                    lowerBounds.add(boundaries.get(0));
                    upperBounds.add(boundaries.get(1));
                }
            }
        }
    }

    private static void mergeBounds(List<Long> lowerBounds, List<Long> upperBounds, int upperPosition, int lowerPosition, List<Long> boundaries) {
        List<Long> lowerBoundsEnd = new ArrayList<>(lowerBounds.subList(upperPosition + 1, lowerBounds.size()));
        lowerBounds.subList(lowerPosition + 1, lowerBounds.size()).clear();
        lowerBounds.addAll(lowerBoundsEnd);
        List<Long> upperBoundsEnd = new ArrayList<>(upperBounds.subList(upperPosition, upperBounds.size()));
        upperBounds.subList(lowerPosition, upperBounds.size()).clear();
        upperBounds.addAll(upperBoundsEnd);
    }

    private static void extendLowerBound(List<Long> lowerBounds, List<Long> upperBounds, int upperPosition, int lowerPosition, List<Long> boundaries) {
        List<Long> lowerBoundsEnd = new ArrayList<>(lowerBounds.subList(upperPosition, lowerBounds.size()));
        lowerBounds.subList(lowerPosition + 1, lowerBounds.size()).clear();
        lowerBounds.addAll(lowerBoundsEnd);
        List<Long> upperBoundsEnd = new ArrayList<>(upperBounds.subList(upperPosition, upperBounds.size()));
        upperBounds.subList(lowerPosition, upperBounds.size()).clear();
        upperBounds.add(boundaries.get(1));
        upperBounds.addAll(upperBoundsEnd);
    }

    private static void setNewBounds(List<Long> lowerBounds, List<Long> upperBounds, int upperPosition, int lowerPosition, List<Long> boundaries) {
        List<Long> lowerBoundsEnd = new ArrayList<>(lowerBounds.subList(upperPosition, lowerBounds.size()));
        lowerBounds.subList(lowerPosition + 1, lowerBounds.size()).clear();
        lowerBounds.add(boundaries.get(0));
        lowerBounds.addAll(lowerBoundsEnd);
        List<Long> upperBoundsEnd = new ArrayList<>(upperBounds.subList(upperPosition, upperBounds.size()));
        upperBounds.subList(lowerPosition + 1, upperBounds.size()).clear();
        upperBounds.add(boundaries.get(1));
        upperBounds.addAll(upperBoundsEnd);
    }

    private static void extendUpperBound(List<Long> lowerBounds, List<Long> upperBounds, int upperPosition, int lowerPosition, List<Long> boundaries) {
        List<Long> lowerBoundsEnd = new ArrayList<>(lowerBounds.subList(upperPosition + 1, lowerBounds.size()));
        lowerBounds.subList(lowerPosition + 1, lowerBounds.size()).clear();
        lowerBounds.add(boundaries.get(0));
        lowerBounds.addAll(lowerBoundsEnd);
        List<Long> upperBoundsEnd = new ArrayList<>(upperBounds.subList(upperPosition, upperBounds.size()));
        upperBounds.subList(lowerPosition + 1, upperBounds.size()).clear();
        upperBounds.addAll(upperBoundsEnd);
    }

    private static int getPositionOfSmallestGreaterThan(List<Long> list, Long value) {
        if (list.isEmpty()) return -1;

        int position = Collections.binarySearch(list, value);

        if (position >= 0) {
            return position;
        } else {
            return -position - 1;
        }
    }

    public static int getPositionOfGreatestLowerThan(List<Long> list, Long value) {
        if (list.isEmpty()) return -1;

        int position = Collections.binarySearch(list, value);

        if (position >= 0) {
            return position;
        } else {
            return -position - 2;
        }
    }

}
