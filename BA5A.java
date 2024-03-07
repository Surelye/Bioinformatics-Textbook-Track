// Find the Minimum Number of Coins Needed to Make Change
// ------------------------------------------------------
//
// The Change Problem
//
// Find the minimum number of coins needed to make change.
//
// Given: An integer money and an array Coins of positive integers.
//
// Return: The minimum number of coins with denominations Coins that changes money.
//
// Sample Dataset
// --------------
// 40
// 1,5,10,20,25,50
// --------------
//
// Sample Output
// -------------
// 2
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA5A {

    private static void shiftList(List<List<Integer>> list) {
        list.removeFirst();
        list.add(new ArrayList<>(List.of()));
    }

    private static List<Integer> DPChangeMachinery(int money, int[] coins) {
        int largestNominal = coins[coins.length - 1], windowSize = Math.min(money, largestNominal),
                diff;
        List<Integer> buffer;
        List<List<Integer>> minCoinsWindow = new ArrayList<>(windowSize + 1);
        for (int i = 0; i <= windowSize; ++i) {
            minCoinsWindow.add(new ArrayList<>(List.of()));
        }

        for (int i = 1; i <= windowSize; ++i) {
            for (int coin : coins) {
                if (i >= coin) {
                    diff = i - coin;
                    if (minCoinsWindow.get(i).isEmpty() ||
                            minCoinsWindow.get(diff).size() + 1 < minCoinsWindow.get(i).size()) {
                        buffer = new ArrayList<>(minCoinsWindow.get(diff));
                        buffer.add(coin);
                        minCoinsWindow.set(i, buffer);
                    }
                }
            }
        }
        shiftList(minCoinsWindow);
        for (int i = windowSize + 1; i <= money; ++i) {
            for (int coin : coins) {
                diff = windowSize - coin;
                if (minCoinsWindow.get(windowSize).isEmpty() ||
                        minCoinsWindow.get(diff).size() + 1 <
                                minCoinsWindow.get(windowSize).size()) {
                    buffer = new ArrayList<>(minCoinsWindow.get(diff));
                    buffer.add(coin);
                    minCoinsWindow.set(windowSize, buffer);
                }
            }
            shiftList(minCoinsWindow);
        }

        return minCoinsWindow.get(windowSize - 1);
    }

    public static int DPChangeNum(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return DPChangeMachinery(
                Integer.parseInt(strDataset.getFirst()),
                Arrays.stream(strDataset.getLast().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray()
        ).size();
    }

    public static int DPChangeNum(int money, int[] coins) {
        return DPChangeMachinery(money, coins).size();
    }

    public static List<Integer> DPChange(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return DPChangeMachinery(
                Integer.parseInt(strDataset.getFirst()),
                Arrays.stream(strDataset.getLast().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray()
        );
    }

    public static List<Integer> DPChange(int money, int[] coins) {
        return DPChangeMachinery(money, coins);
    }
}
