import java.io.IOException;
import java.util.*;

import static java.lang.Long.max;
import static java.lang.Long.min;

public class day9_2 {

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(9, false);

        List<Tile> tiles = lines.stream().map(l -> {
            List<Long> points = Arrays.stream(l.split(",")).map(Long::parseLong).toList();
            return new Tile(points.getFirst(), points.getLast());
        }).toList();

        List<VerticalLine> verticalLines = new ArrayList<>();
        List<HorizontalLine> horizontalLines = new ArrayList<>();

        for (int i = 0; i < tiles.size(); i++) {
            Tile tileA = tiles.get(i);
            Tile tileB = tiles.get((i + 1) % tiles.size());
            if (tileA.y == tileB.y) {
                horizontalLines.add(new HorizontalLine(tileA, tileB));
            }
            if (tileA.x == tileB.x) {
                verticalLines.add(new VerticalLine(tileA, tileB));
            }
        }

        long rectangle = 0;
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile a = tiles.get(i);
                Tile b = tiles.get(j);
                if (isEligible(a, b, verticalLines, horizontalLines)){
                    long size = (Math.abs(a.x - b.x) + 1) * (Math.abs(a.y - b.y) + 1);
                    rectangle = Math.max(size, rectangle);
                }
            }
        }
        return rectangle;
    }

    private static boolean isEligible(Tile a, Tile b, List<VerticalLine> verticalLines, List<HorizontalLine> horizontalLines) {
        VerticalLine leftVerticalLine = new VerticalLine(new Tile(min(a.x, b.x), a.y), new Tile(min(a.x, b.x), b.y));
        VerticalLine rightVerticalLine = new VerticalLine(new Tile(max(a.x, b.x), a.y), new Tile(max(a.x, b.x), b.y));
        HorizontalLine topHorizontalLine = new HorizontalLine(new Tile(a.x, min(a.y, b.y)), new Tile(b.x, min(a.y, b.y)));
        HorizontalLine bottomHorizontalLine = new HorizontalLine(new Tile(a.x, max(a.y, b.y)), new Tile(b.x, max(a.y, b.y)));

        List<VerticalLine> leftVerticalLines = getLeftVerticalLines(verticalLines, leftVerticalLine);
        List<VerticalLine> rightVerticalLines = getRightVerticalLines(verticalLines, rightVerticalLine);
        List<HorizontalLine> topHorizontalLines = getTopHorizontalLines(horizontalLines, topHorizontalLine);
        List<HorizontalLine> bottomHorizontalLines = getBottomHorizontalLines(horizontalLines, bottomHorizontalLine);
        if (leftVerticalLines.isEmpty() || rightVerticalLines.isEmpty() || topHorizontalLines.isEmpty() || bottomHorizontalLines.isEmpty()) {
            return false;
        }
        if (isNotVerticalWithinShape(leftVerticalLine, leftVerticalLines)) return false;
        if (isNotVerticalWithinShape(rightVerticalLine, rightVerticalLines)) return false;
        if (isNotHorizontalWithinShape(topHorizontalLine, topHorizontalLines)) return false;
        if (isNotHorizontalWithinShape(bottomHorizontalLine, bottomHorizontalLines)) return false;
        return true;
    }

    private static boolean isNotVerticalWithinShape(VerticalLine verticalLine, List<VerticalLine> verticalLines) {
        for (int i = 0; i < verticalLines.size() - 1; i++) {
            if (verticalLines.get(i).end.y < verticalLines.get(i+1).start.y) return true;
        }
        if (verticalLines.getFirst().start.y > verticalLine.start.y ||
                verticalLines.stream().max(Comparator.comparingLong(v -> v.end.y)).get().end.y < verticalLine.end.y)
            return true;
        return false;
    }

    private static boolean isNotHorizontalWithinShape(HorizontalLine horizontalLine, List<HorizontalLine> horizontalLines) {
        for (int i = 0; i < horizontalLines.size() - 1; i++) {
            if (horizontalLines.get(i).end.x < horizontalLines.get(i+1).start.x) return true;
        }
        if (horizontalLines.getFirst().start.x > horizontalLine.start.x ||
                horizontalLines.stream().max(Comparator.comparingLong(h -> h.end.x)).get().end.x < horizontalLine.end.x)
            return true;
        return false;
    }

    private static List<VerticalLine> getLeftVerticalLines(List<VerticalLine> verticalLines, VerticalLine leftVerticalLine) {
        if (leftVerticalLine.start.equals(leftVerticalLine.end)) {
            return List.of(leftVerticalLine);
        }
        return verticalLines.stream()
                .filter(v -> v.start.x <= leftVerticalLine.start.x && overlapsVertically(leftVerticalLine, v))
                .sorted(Comparator.comparingLong(v -> v.start.y)).toList();
    }

    private static List<VerticalLine> getRightVerticalLines(List<VerticalLine> verticalLines, VerticalLine rightVerticalLine) {
        if (rightVerticalLine.start.equals(rightVerticalLine.end)) {
            return List.of(rightVerticalLine);
        }
        return verticalLines.stream()
                .filter(v -> v.start.x >= rightVerticalLine.start.x && overlapsVertically(rightVerticalLine, v))
                .sorted(Comparator.comparingLong(v -> v.start.y)).toList();
    }

    private static List<HorizontalLine> getTopHorizontalLines(List<HorizontalLine> horizontalLines, HorizontalLine topHorizontalLine) {
        if (topHorizontalLine.start.equals(topHorizontalLine.end)) {
            return List.of(topHorizontalLine);
        }
        return horizontalLines.stream()
                .filter(h -> h.start.y <= topHorizontalLine.start.y && overlapsHorizontally(topHorizontalLine, h))
                .sorted(Comparator.comparingLong(v -> v.start.x)).toList();
    }

    private static List<HorizontalLine> getBottomHorizontalLines(List<HorizontalLine> horizontalLines, HorizontalLine bottomHorizontalLine) {
        if (bottomHorizontalLine.start.equals(bottomHorizontalLine.end)) {
            return List.of(bottomHorizontalLine);
        }
        return horizontalLines.stream()
                .filter(h -> h.start.y >= bottomHorizontalLine.start.y && overlapsHorizontally(bottomHorizontalLine, h))
                .sorted(Comparator.comparingLong(v -> v.start.x)).toList();
    }


    private static boolean overlapsVertically(VerticalLine verticalLine, VerticalLine v) {
        return v.start.y >= verticalLine.start.y && v.start.y <= verticalLine.end.y ||
                v.end.y >= verticalLine.start.y && v.end.y <= verticalLine.end.y ||
                v.start.y <= verticalLine.start.y && v.end.y >= verticalLine.end.y;
    }

    private static boolean overlapsHorizontally(HorizontalLine horizontalLine, HorizontalLine h) {
        return h.start.x >= horizontalLine.start.x && h.start.x <= horizontalLine.end.x ||
                h.end.x >= horizontalLine.start.x && h.end.x <= horizontalLine.end.x ||
                h.start.x <= horizontalLine.start.x && h.end.x >= horizontalLine.end.x;
    }

    private static class Tile {
        long x;
        long y;
        public Tile(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Tile obj) {
            return x == obj.x && y == obj.y;
        }
    }

    private static class VerticalLine {
        Tile start;
        Tile end;
        public VerticalLine(Tile tile1, Tile tile2) {
            assert(tile1.x == tile2.x);
            this.start = tile1.y < tile2.y ? tile1 : tile2;
            this.end = tile1.y < tile2.y ? tile2 : tile1;
        }
    }

    private static class HorizontalLine {
        Tile start;
        Tile end;
        public HorizontalLine(Tile tile1, Tile tile2) {
            assert(tile1.y == tile2.y);
            this.start = tile1.x < tile2.x ? tile1 : tile2;
            this.end = tile1.x < tile2.x ? tile2 : tile1;
        }
    }
}
