import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class day11_2 {
    public static final String OUT = "out";
    public static final String SVR = "svr";
    public static final String DAC = "dac";
    public static final String FFT = "fft";
    public static final String COLON = ": ";
    public static final String SPACE = " ";

    public static long getResult() throws IOException {
        List<String> lines = new ArrayList<>(util.getFileLines(11, false));
        Servers servers = new Servers();
        int prevSize = 0;
        while (servers.servers.size() != prevSize && !lines.isEmpty()) {
            prevSize = servers.servers.size();
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
        if (servers.findByName(SVR)) {
            return servers.getByName(SVR).pathsCount;
        }
        throw new IllegalStateException("no solution found");
    }

    private static class Server {
        List<Server> outputs = new ArrayList<>();
        long pathsCount;
        String name;
        boolean pathsAreValid;
        boolean pathsMaybeValid;

        public Server(String name, long pathsCount) {
            this.name = name;
            this.pathsCount = pathsCount;
        }

        public Server(List<Server> outputs, String name, boolean dacFound, boolean fftFound) {
            this.name = name;
            if (List.of(DAC, FFT).contains(name)) {
                if (dacFound || fftFound) {
                    pathsAreValid = true;
                    this.outputs = outputs.stream().filter(o -> o.pathsMaybeValid).toList();
                }
                else {
                    pathsMaybeValid = true;
                    this.outputs = outputs;
                }
            }
            else {
                if (!dacFound && !fftFound) {
                    this.outputs = outputs;
                }
                if (dacFound ^ fftFound) {
                    this.outputs = outputs.stream().filter(o -> o.pathsMaybeValid).toList();
                    pathsMaybeValid = !this.outputs.isEmpty();
                }
                if (dacFound && fftFound) {
                    this.outputs = outputs.stream().filter(o -> o.pathsAreValid).toList();
                    pathsAreValid = !this.outputs.isEmpty();
                }
            }
            pathsCount = this.outputs.stream().mapToLong(s -> s.pathsCount).sum();
        }
    }

    private static class Servers {
        List<Server> servers = new ArrayList<>();
        boolean dacFound = false;
        boolean fftFound = false;

        public Servers() {
            Server out = new Server(OUT, 1);
            servers.add(out);
        }

        public boolean addServer(String serverInput) {
            String[] serverValues = serverInput.split(COLON);
            if (!findByName(serverValues[0])) {
                String[] outputs = serverValues[1].split(SPACE);
                if (Arrays.stream(outputs).allMatch(this::findByName)) {
                    servers.add(new Server(getByNames(outputs), serverValues[0], dacFound, fftFound));
                    if (DAC.equals(serverValues[0])) {
                        dacFound = true;
                    }
                    if (FFT.equals(serverValues[0])) {
                        fftFound = true;
                    }
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
