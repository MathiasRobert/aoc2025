import java.io.IOException;
import java.util.*;

public class day8_1 {
    private static boolean TEST = false;
    List<JunctionBox> junctionBoxes = new ArrayList<>();
    List<Network> networks = new ArrayList<>();

    public static long getResult() throws IOException {
        day8_1 result = new day8_1();
        List<String> lines = util.getFileLines(8, TEST);
        return result.getResult(lines);
    }

    private long getResult(List<String> lines) {
        lines.forEach(l -> {
            int[] coordinates = Arrays.stream(l.split(",")).mapToInt(Integer::parseInt).toArray();
            new JunctionBox(coordinates[0], coordinates[1], coordinates[2], this);
        });
        for (int i = 0; i < (TEST?10:1000); i++) {
            JunctionBox closestJB = junctionBoxes.stream()
                    .min(Comparator.comparingDouble(j -> j.getXYZDistance(j.getClosestJunctionBox()))).get();
            closestJB.connectClosestJunctionBox();
        }
        List<Network> sortedNetworks = networks.stream().sorted(Comparator.comparingInt(n -> n.junctionBoxes.size())).toList();
        return sortedNetworks.subList(sortedNetworks.size()-3, sortedNetworks.size()).stream().mapToLong(n -> n.junctionBoxes.size()).reduce(1, (a, b) -> a * b);
    }

    public class JunctionBox {
        public int x;
        public int y;
        public int z;

        private JunctionBox closestJunctionBox;
        private final List<JunctionBox> connectedJunctionBoxes = new ArrayList<>();
        public Network network;
        private final day8_1 day81;

        public JunctionBox(int x, int y, int z, day8_1 day81) {
            this.x = x;
            this.y = y;
            this.z = z;

            network = new Network(this, day81);
            day81.junctionBoxes.add(this);
            this.day81 = day81;
        }

        private double getXYZDistance(JunctionBox other) {
            return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z));
        }

        public JunctionBox getClosestJunctionBox() {
            if (this.closestJunctionBox != null) {
                return this.closestJunctionBox;
            }
            JunctionBox closestJunctionBox = null;
            for (JunctionBox junctionBox : day81.junctionBoxes) {
                if (junctionBox!=this && !this.connectedJunctionBoxes.contains(junctionBox)) {
                    if (closestJunctionBox == null || getXYZDistance(junctionBox) < getXYZDistance(closestJunctionBox)) {
                        closestJunctionBox = junctionBox;
                    }
                }
            }
            this.closestJunctionBox = closestJunctionBox;
            return closestJunctionBox;
        }

        public void connectClosestJunctionBox() {
            if (closestJunctionBox != null) {
                if (this.network!=this.closestJunctionBox.network) {
                    day81.networks.remove(this.closestJunctionBox.network);
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

        public Network(JunctionBox junctionBox, day8_1 day81) {
            Set<JunctionBox> junctionBoxSet = new HashSet<>();
            junctionBoxSet.add(junctionBox);
            this.junctionBoxes = junctionBoxSet;
            day81.networks.add(this);
        }
    }
}
