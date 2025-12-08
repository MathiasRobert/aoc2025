import java.io.IOException;
import java.util.*;

public class day8_2 {
    List<day8_2.JunctionBox> junctionBoxes = new ArrayList<>();
    List<day8_2.Network> networks = new ArrayList<>();

    public static long getResult() throws IOException {
        day8_2 result = new day8_2();
        List<String> lines = util.getFileLines(8, true);
        return result.getResult(lines);
    }

    private long getResult(List<String> lines) {
        lines.forEach(l -> {
            int[] coordinates = Arrays.stream(l.split(",")).mapToInt(Integer::parseInt).toArray();
            new day8_2.JunctionBox(coordinates[0], coordinates[1], coordinates[2], this);
        });
        junctionBoxes.forEach(j -> j.setClosestJunctionBox(junctionBoxes));
        List<day8_2.Network> max3 = networks.stream().sorted(Comparator.comparingInt(n -> n.junctionBoxes.size())).toList().subList(0, 3);
        return max3.stream().mapToLong(n -> n.junctionBoxes.size()).reduce(1, (a, b) -> a * b);
    }

    public class JunctionBox {
        public int x;
        public int y;
        public int z;

        private day8_2.JunctionBox closestJunctionBox;
        public day8_2.Network network;
        private final day8_2 day82;

        public JunctionBox(int x, int y, int z, day8_2 day82) {
            this.x = x;
            this.y = y;
            this.z = z;

            network = new day8_2.Network(this, day82);
            day82.junctionBoxes.add(this);
            this.day82 = day82;
        }

        private double getXYZDistance(day8_2.JunctionBox other) {
            return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z));
        }

        public void setClosestJunctionBox(List<day8_2.JunctionBox> junctionBoxes) {
            day8_2.JunctionBox closestJunctionBox = null;
            for (day8_2.JunctionBox junctionBox : junctionBoxes) {
                if (junctionBox!=this) {
                    if (closestJunctionBox == null || getXYZDistance(junctionBox) < getXYZDistance(closestJunctionBox)) {
                        closestJunctionBox = junctionBox;
                    }
                }
            }
            if (closestJunctionBox != null && this.network != closestJunctionBox.network) {
                day82.networks.remove(closestJunctionBox.network);
                this.network.junctionBoxes.addAll(closestJunctionBox.network.junctionBoxes);
                closestJunctionBox.network.junctionBoxes.forEach(j -> j.network = this.network);
            }
        }
    }

    public class Network {
        public Set<day8_2.JunctionBox> junctionBoxes;

        public Network(day8_2.JunctionBox junctionBox, day8_2 day82) {
            Set<day8_2.JunctionBox> junctionBoxSet = new HashSet<>();
            junctionBoxSet.add(junctionBox);
            this.junctionBoxes = junctionBoxSet;
            day82.networks.add(this);
        }
    }
}
