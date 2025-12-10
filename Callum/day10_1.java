import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class day10_1 {
    private static final Character ON = '#';
    private static final Character OFF = '.';

    public static long getResult() throws IOException {
        List<String> lines = util.getFileLines(10, false);
        List<List<String>> splitLines = lines.stream().map(l -> Arrays.stream(l.split(" ")).toList()).toList();
        int result = 0;
        for (List<String> splitLine : splitLines) {
            List<Boolean> lightsSetup = setupLights(splitLine);
            List<List<Integer>> availableButtons = setupAvailableButtons(splitLine);
            List<List<List<Integer>>> indexedButtons = preIndexButtons(availableButtons, lightsSetup.size());
            List<List<Integer>> pressedButtons = new ArrayList<>();

            Integer presses = pressNextButton(lightsSetup, pressedButtons, indexedButtons, availableButtons.size(), Integer.MAX_VALUE);
            if (presses != Integer.MAX_VALUE) {
                result += presses;
            } else throw new IllegalStateException("cannot find solution for line : " + splitLine);
        }
        return result;
    }

    private static Integer pressNextButton(List<Boolean> lightsSetup, List<List<Integer>> pressedButtons,
                                        List<List<List<Integer>>> indexedButtons, int maxPresses, int currentMax) {
        int i = lightsSetup.indexOf(true);
        for (List<Integer> button : indexedButtons.get(i)) {
            if (!pressedButtons.contains(button)) {
                List<Boolean> newLightsSetup = new ArrayList<>(lightsSetup);
                List<List<Integer>> newPressedButtons = new ArrayList<>(pressedButtons);
                newPressedButtons.add(button);
                if (continueAfterPressButton(newLightsSetup, button) && newPressedButtons.size() < maxPresses) {
                    Integer result = pressNextButton(newLightsSetup, newPressedButtons, indexedButtons, maxPresses, currentMax);
                    if (result < currentMax) currentMax = result;
                }
                else if (newLightsSetup.stream().noneMatch(Boolean::booleanValue))
                    return newPressedButtons.size();
            }
        }
        return currentMax;
    }

    private static boolean continueAfterPressButton(List<Boolean> newLightsSetup, List<Integer> button) {
        for (Integer light : button) {
            if (newLightsSetup.get(light)) newLightsSetup.set(light, false);
            else newLightsSetup.set(light, true);
        }
        return newLightsSetup.stream().anyMatch(Boolean::booleanValue);
    }

    private static List<Boolean> setupLights(List<String> splitLine) {
        List<Boolean> lightNeedChanging = new ArrayList<>();
        splitLine.getFirst().chars().forEach(c -> {
            if (List.of(ON, OFF).contains((char) c)){
                if ((char) c == '#') {
                    lightNeedChanging.add(true);
                } else {
                    lightNeedChanging.add(false);
                }
            }
        });
        return lightNeedChanging;
    }

    public static List<List<Integer>> setupAvailableButtons(List<String> splitLine) {
        List<List<Integer>> availableButtons = new ArrayList<>();
        for (int i = 1; i < splitLine.size() - 1; i++) {
            List<Integer> buttonEffects = new ArrayList<>();
            splitLine.get(i).chars().forEach(c -> {
                if (Character.isDigit(c)) {
                    buttonEffects.add(Character.getNumericValue(c));
                }
            });
            availableButtons.add(buttonEffects);
        }
        return availableButtons;
    }

    public static List<List<List<Integer>>> preIndexButtons(List<List<Integer>> availableButtons, int size) {
        List<List<List<Integer>>> indexedButtons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indexedButtons.add(new ArrayList<>());
        }
        for (List<Integer> button : availableButtons) {
            for (Integer value : button) {
                indexedButtons.get(value).add(button);
            }
        }
        return indexedButtons;
    }
}
