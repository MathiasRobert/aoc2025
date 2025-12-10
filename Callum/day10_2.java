import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.microsoft.z3.*;

public class day10_2 {

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(10, false);
        List<List<String>> splitLines = lines.stream().map(l -> Arrays.stream(l.split(" ")).toList()).toList();
        long result = 0;
        int lineNumber = 1;
        for (List<String> splitLine : splitLines) {
            System.out.println("line : " + lineNumber + " of " + splitLines.size());
            lineNumber++;
            List<List<Integer>> availableButtons = day10_1.setupAvailableButtons(splitLine);
            int numButtons = availableButtons.size();
            List<Integer> joltages = setupJoltages(splitLine);
            int numCounters = joltages.size();

            HashMap<String, String> cfg = new HashMap<>();
            cfg.put("model", "true");
            try (Context ctx = new Context(cfg)) {

                Optimize opt = ctx.mkOptimize();

                IntExpr[] presses = new IntExpr[numButtons];
                for (int b = 0; b < numButtons; b++) {
                    presses[b] = ctx.mkIntConst("x_" + b);
                    opt.Add(ctx.mkGe(presses[b], ctx.mkInt(0))); // x_b >= 0
                }

                for (int counter = 0; counter < numCounters; counter++) {
                    List<ArithExpr> terms = new ArrayList<>();

                    for (int b = 0; b < numButtons; b++) {
                        if (availableButtons.get(b).contains(counter)) {
                            terms.add(presses[b]);
                        }
                    }

                    ArithExpr lhs;
                    if (terms.isEmpty()) {
                        lhs = ctx.mkInt(0);
                    } else if (terms.size() == 1) {
                        lhs = terms.get(0);
                    } else {
                        lhs = ctx.mkAdd(terms.toArray(new ArithExpr[0]));
                    }

                    IntNum rhs = ctx.mkInt(joltages.get(counter));
                    opt.Add(ctx.mkEq(lhs, rhs));
                }

                ArithExpr totalPresses;
                if (numButtons == 1) {
                    totalPresses = presses[0];
                } else {
                    totalPresses = ctx.mkAdd(presses);
                }
                opt.MkMinimize(totalPresses);

                if (opt.Check() != Status.SATISFIABLE) {
                    return -1;
                }

                Model model = opt.getModel();

                for (int b = 0; b < numButtons; b++) {
                    IntNum val = (IntNum) model.evaluate(presses[b], false);
                    result += val.getInt();
                }
            }
        }
        return result;
    }

    private static List<Integer> setupJoltages(List<String> splitLine) {
        String[] splitNumbers = splitLine.getLast().replaceAll("[{}]", "").split(",");
        List<Integer> joltages = Arrays.stream(splitNumbers).map(Integer::parseInt).toList();
        return joltages;
    }
}
