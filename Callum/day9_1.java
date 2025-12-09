import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class day9_1 {

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(9, false);

        List<List<Long>> tiles = lines.stream().map(l -> Arrays.stream(l.split(",")).map(Long::parseLong).toList()).toList();

        long rectangle = 0;
        for (List<Long> tile : tiles) {
            for (List<Long> otherTile : tiles) {
                long size = Math.abs(tile.getFirst() - otherTile.getFirst() + 1) * Math.abs(tile.getLast() - otherTile.getLast() + 1);
                rectangle = Math.max(size, rectangle);
            }
        }
        return rectangle;
    }
    
    private class Tile {
        public long x;
        public long y;
        
        public long biggestRectangle;
        public Tile oppositeCorner;
        
        public Tile(long x, long y) {
            this.x = x;
            this.y = y;
        }
        
        private long calculateRectangleSize(Tile other) {
            long width = Math.abs(x - other.x);
            long height = Math.abs(y - other.y);
            return width * height;
        }
    }
}
