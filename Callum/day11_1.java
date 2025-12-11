import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class day11_1 {
    public static final String OUT = "out";
    public static final String YOU = "you";

    public static long getResult() throws IOException {
        List<String> lines = new ArrayList<>(util.getFileLines(11, false));
        Servers servers = new Servers();
        int prevSize = 0;
        while (servers.servers.size() != prevSize && !lines.isEmpty()) {
            List<String> linesToRemove = new ArrayList<>();
            for (String line : lines) {
                if (servers.addServer(line)) {
                    linesToRemove.add(line);
                }
            }
            for (String lineToRemove : linesToRemove) {
                lines.remove(lineToRemove);
            }
        }
        if (servers.findByName(YOU)) {
            return servers.getByName(YOU).pathsCount;
        }
        throw new IllegalStateException("no solution found");
    }

    private static class Server {
        List<Server> outputs = new ArrayList<>();
        long pathsCount;
        String name;

        public Server(String name, long pathsCount) {
            this.name = name;
            this.pathsCount = pathsCount;
        }

        public Server(List<Server> outputs, String name) {
            this.outputs = outputs;
            this.name = name;
            pathsCount = outputs.stream().mapToLong(s -> s.pathsCount).sum();
        }
    }

    private static class Servers {
        List<Server> servers = new ArrayList<>();

        public Servers() {
            Server out = new Server(OUT, 1);
            servers.add(out);
        }

        public boolean addServer(String serverInput) {
            String[] serverValues = serverInput.split(": ");
            if (!findByName(serverValues[0])) {
                String[] outputs = serverValues[1].split(" ");
                if (Arrays.stream(outputs).allMatch(this::findByName)) {
                    servers.add(new Server(getByNames(outputs), serverValues[0]));
                    return true;
                }
            }
            return false;
        }

        public boolean findByName(String name) {
            return servers.stream().anyMatch(s -> s.name.equals(name));
        }

        public Server getByName(String name) {
            return servers.stream().filter(s -> s.name.equals(name)).findFirst().get();
        }

        public List<Server> getByNames(String[] names) {
            return servers.stream().filter(s -> Arrays.stream(names).anyMatch(n -> n.equals(s.name))).toList();
        }
    }
}
