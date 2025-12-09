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
            Tile a = tiles.get(i);
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile b = tiles.get(j);
                if (getSize(a, b) > rectangle && isEligible(a, b, verticalLines, horizontalLines)) {
                    rectangle = getSize(a, b);
                }
            }
        }
        return rectangle;
    }

    private static long getSize(Tile a, Tile b) {
        return (Math.abs(a.x - b.x) + 1) * (Math.abs(a.y - b.y) + 1);
    }

    private static boolean isEligible(Tile a, Tile b, List<VerticalLine> verticalLines, List<HorizontalLine> horizontalLines) {
        VerticalLine leftVerticalLine = new VerticalLine(new Tile(min(a.x, b.x), a.y), new Tile(min(a.x, b.x), b.y));
        VerticalLine rightVerticalLine = new VerticalLine(new Tile(max(a.x, b.x), a.y), new Tile(max(a.x, b.x), b.y));
        HorizontalLine topHorizontalLine = new HorizontalLine(new Tile(a.x, min(a.y, b.y)), new Tile(b.x, min(a.y, b.y)));
        HorizontalLine bottomHorizontalLine = new HorizontalLine(new Tile(a.x, max(a.y, b.y)), new Tile(b.x, max(a.y, b.y)));

        List<VerticalLine> leftVerticalLines = getVerticalLines(verticalLines, leftVerticalLine, true);
        List<VerticalLine> rightVerticalLines = getVerticalLines(verticalLines, rightVerticalLine, false);
        List<HorizontalLine> topHorizontalLines = getHorizontalLines(horizontalLines, topHorizontalLine, true);
        List<HorizontalLine> bottomHorizontalLines = getHorizontalLines(horizontalLines, bottomHorizontalLine, false);

        return leftVerticalLine.isCovered(leftVerticalLines) &&
                rightVerticalLine.isCovered(rightVerticalLines) &&
                topHorizontalLine.isCovered(topHorizontalLines) &&
                bottomHorizontalLine.isCovered(bottomHorizontalLines);
    }

    private static List<VerticalLine> getVerticalLines(List<VerticalLine> verticalLines, VerticalLine verticalLine,
                                                       boolean left) {
        if (verticalLine.start.equals(verticalLine.end)) {
            return List.of(verticalLine);
        }
        return verticalLines.stream()
                .filter(v -> (left ? v.start.x <= verticalLine.start.x : v.start.x >= verticalLine.start.x) &&
                        verticalLine.overlaps(v))
                .sorted(Comparator.comparingLong(v -> v.start.y)).toList();
    }

    private static List<HorizontalLine> getHorizontalLines(List<HorizontalLine> horizontalLines,
                                                           HorizontalLine horizontalLine, boolean top) {
        if (horizontalLine.start.equals(horizontalLine.end)) {
            return List.of(horizontalLine);
        }
        return horizontalLines.stream()
                .filter(h -> (top ? h.start.y <= horizontalLine.start.y : h.start.y >= horizontalLine.start.y) &&
                        horizontalLine.overlaps(h))
                .sorted(Comparator.comparingLong(v -> v.start.x)).toList();
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
            assert (tile1.x == tile2.x);
            this.start = tile1.y < tile2.y ? tile1 : tile2;
            this.end = tile1.y < tile2.y ? tile2 : tile1;
        }

        public boolean isCovered(List<VerticalLine> shape) {
            if (shape.isEmpty()) return false;
            for (int i = 0; i < shape.size() - 1; i++) {
                if (shape.get(i).end.y < shape.get(i + 1).start.y) return false;
            }
            return shape.getFirst().start.y <= this.start.y &&
                    shape.stream().max(Comparator.comparingLong(v -> v.end.y)).get().end.y >= this.end.y;
        }

        public boolean overlaps(VerticalLine v) {
            return v.start.y >= start.y && v.start.y <= end.y ||
                    v.end.y >= start.y && v.end.y <= end.y ||
                    v.start.y <= start.y && v.end.y >= end.y;
        }
    }

    private static class HorizontalLine {
        Tile start;
        Tile end;

        public HorizontalLine(Tile tile1, Tile tile2) {
            assert (tile1.y == tile2.y);
            start = tile1.x < tile2.x ? tile1 : tile2;
            end = tile1.x < tile2.x ? tile2 : tile1;
        }

        public boolean isCovered(List<HorizontalLine> shape) {
            if (shape.isEmpty()) return false;
            for (int i = 0; i < shape.size() - 1; i++) {
                if (shape.get(i).end.x < shape.get(i + 1).start.x) return false;
            }
            return shape.getFirst().start.x <= start.x &&
                    shape.stream().max(Comparator.comparingLong(h -> h.end.x)).get().end.x >= end.x;
        }

        public boolean overlaps(HorizontalLine h) {
            return h.start.x >= start.x && h.start.x <= end.x ||
                    h.end.x >= start.x && h.end.x <= end.x ||
                    h.start.x <= start.x && h.end.x >= end.x;
        }
    }
}
