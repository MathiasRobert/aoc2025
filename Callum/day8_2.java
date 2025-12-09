import java.io.IOException;
import java.util.*;

public class day8_2 {
    private static boolean TEST = false;
    List<JunctionBox> junctionBoxes = new ArrayList<>();
    List<Network> networks = new ArrayList<>();

    public static long getResult() throws IOException {
        day8_2 result = new day8_2();
        List<String> lines = util.getFileLines(8, TEST);
        return result.getResult(lines);
    }

    private long getResult(List<String> lines) {
        lines.forEach(l -> {
            long[] coordinates = Arrays.stream(l.split(",")).mapToLong(Long::parseLong).toArray();
            new JunctionBox(coordinates[0], coordinates[1], coordinates[2], this);
        });
        long last1 = 0;
        long last2 = 0;
        while (networks.size() > 1) {
            JunctionBox closestJB = junctionBoxes.stream()
                    .min(Comparator.comparingDouble(j -> j.getXYZDistance(j.getClosestJunctionBox()))).get();
            last1 = closestJB.x;
            last2 = closestJB.getClosestJunctionBox().x;
            closestJB.connectClosestJunctionBox();
        }
        return last1 * last2;
    }

    public class JunctionBox {
        public long x;
        public long y;
        public long z;

        private JunctionBox closestJunctionBox;
        private final List<JunctionBox> connectedJunctionBoxes = new ArrayList<>();
        public Network network;
        private final day8_2 day82;

        public JunctionBox(long x, long y, long z, day8_2 day82) {
            this.x = x;
            this.y = y;
            this.z = z;

            network = new Network(this, day82);
            day82.junctionBoxes.add(this);
            this.day82 = day82;
        }

        private double getXYZDistance(JunctionBox other) {
            return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z));
        }

        public JunctionBox getClosestJunctionBox() {
            if (this.closestJunctionBox == null) {
                JunctionBox closestJunctionBox = null;
                for (JunctionBox junctionBox : day82.junctionBoxes) {
                    if (junctionBox != this && !this.connectedJunctionBoxes.contains(junctionBox)) {
                        if (closestJunctionBox == null || getXYZDistance(junctionBox) < getXYZDistance(closestJunctionBox)) {
                            closestJunctionBox = junctionBox;
                        }
                    }
                }
                this.closestJunctionBox = closestJunctionBox;
            }
            return closestJunctionBox;
        }

        public void connectClosestJunctionBox() {
            if (closestJunctionBox != null) {
                if (this.network!=this.closestJunctionBox.network) {
                    day82.networks.remove(this.closestJunctionBox.network);
                    this.network.junctionBoxes.addAll(this.closestJunctionBox.network.junctionBoxes);
                    this.closestJunctionBox.network.junctionBoxes.forEach(j -> j.network = this.network);
                }

                this.connectedJunctionBoxes.add(this.closestJunctionBox);
                this.closestJunctionBox.connectedJunctionBoxes.add(this);

                this.closestJunctionBox.closestJunctionBox = null;
                this.closestJunctionBox = null;
            } else {
                throw new IllegalStateException("closestJunctionBox is null");
            }
        }
    }

    public class Network {
        public Set<JunctionBox> junctionBoxes;

        public Network(JunctionBox junctionBox, day8_2 day82) {
            Set<JunctionBox> junctionBoxSet = new HashSet<>();
            junctionBoxSet.add(junctionBox);
            this.junctionBoxes = junctionBoxSet;
            day82.networks.add(this);
        }
    }
}
