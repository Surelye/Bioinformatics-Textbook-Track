import java.nio.file.Path;
import java.util.*;

public class BA5A {

    private static void shiftList(List<Integer> list) {
        list.removeFirst();
        list.add(0);
    }

    private static int DPChangeMachinery(int money, int[] coins) {
        int largestNominal = coins[coins.length - 1], windowSize = Math.min(money, largestNominal),
                diff;
        List<Integer> minNumCoinsWindow = new ArrayList<>(windowSize + 1);
        for (int i = 0; i <= windowSize; ++i) {
            minNumCoinsWindow.add(0);
        }

        for (int i = 1; i <= windowSize; ++i) {
            minNumCoinsWindow.set(i, Integer.MAX_VALUE);
            for (int coin : coins) {
                if (i >= coin) {
                    diff = i - coin;
                    if (minNumCoinsWindow.get(diff) + 1 < minNumCoinsWindow.get(i)) {
                        minNumCoinsWindow.set(i, minNumCoinsWindow.get(diff) + 1);
                    }
                }
            }
        }
        shiftList(minNumCoinsWindow);
        for (int i = windowSize + 1; i <= money; ++i) {
            minNumCoinsWindow.set(windowSize, Integer.MAX_VALUE);
            for (int coin : coins) {
                diff = windowSize - coin;
                if (minNumCoinsWindow.get(diff) + 1 < minNumCoinsWindow.get(windowSize)) {
                    minNumCoinsWindow.set(windowSize, minNumCoinsWindow.get(diff) + 1);
                }
            }
            shiftList(minNumCoinsWindow);
        }

        return minNumCoinsWindow.get(windowSize - 1);
    }

    public static int DPChange(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return DPChangeMachinery(
                Integer.parseInt(strDataset.getFirst()),
                Arrays.stream(strDataset.getLast().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray()
        );
    }

    public static int DPChange(int money, int[] coins) {
        return DPChangeMachinery(money, coins);
    }
}
