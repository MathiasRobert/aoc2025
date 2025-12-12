import java.io.IOException;

public class launcher {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println(day12_1.getResult());
        long finish = System.currentTimeMillis();
        System.out.println(finish - start + "ms");
    }
}
