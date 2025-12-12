import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class day12_1 {

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(12, false);
        List<Present> presents = new ArrayList<>();
        List<Tree> trees = new ArrayList<>();
        getPresentsAndTrees(lines, presents, trees);
        for (int i = 0; i < presents.size(); i++) {
            assert presents.get(i).index == i;
        }
        long result = 0;
        for (Tree tree : trees) {
            int squares = 0;
            for (int i = 0; i < presents.size(); i++) {
                squares += presents.get(i).occupiedSquares * tree.presents.get(i);
            }
            if (squares <= tree.size()) {
                result++;
            }
        }
        return result;
    }

    private static void getPresentsAndTrees(List<String> lines, List<Present> presents, List<Tree> trees) {
        List<List<String>> presentInputs = new ArrayList<>();
        List<String> treeInputs = new ArrayList<>();
        separatePresentAndTreeInputs(lines, presentInputs, treeInputs);
        presents.addAll(presentInputs.stream().map(Present::new).toList());
        trees.addAll(treeInputs.stream().map(Tree::new).toList());
    }

    private static void separatePresentAndTreeInputs(List<String> lines, List<List<String>> presents, List<String> trees) {
        List<String> tmpList = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.isEmpty()){
                tmpList.add(line);
            }
            else {
                presents.add(new ArrayList<>(tmpList));
                tmpList = new ArrayList<>();
            }
        }
        trees.addAll(tmpList);
    }

    private static class Tree {
        public int x;
        public int y;
        public int size() {return x * y;}
        public List<Integer> presents = new ArrayList<>();

        public Tree(String input) {
            String[] inputs = input.split(": ");
            String[] dimensions = inputs[0].split("x");
            String[] presents = inputs[1].split(" ");
            this.x = Integer.parseInt(dimensions[0]);
            this.y = Integer.parseInt(dimensions[1]);
            for (String present : presents) {
                this.presents.add(Integer.parseInt(present));
            }
        }
    }

    private static class Present {
        public int[][] shape = new int[3][3];
        public int index;
        public int occupiedSquares;

        public Present(List<String> input) {
            this.index = Integer.parseInt(input.get(0).substring(0,1));
            for (int i = 1; i < input.size(); i++) {
                int[] line = input.get(i).chars().map(x -> x == '#' ? 1 : 0).toArray();
                shape[i-1] = line;
            }
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    occupiedSquares += shape[i][j];
                }
            }
        }
    }
}
