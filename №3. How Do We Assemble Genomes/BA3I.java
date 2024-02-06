// Find a k-Universal Circular String
// ----------------------------------
//
// A k-universal circular string is a circular string that contains every possible k-mer
// constructed over a given alphabet.
//
// ----------------------------------
//
// k-Universal Circular String Problem
//
// Find a k-universal circular binary string.
//
// Given: An integer k.
//
// Return: A k-universal circular string. (If multiple answers exist, you may return any one.)
//
// Sample Dataset
// --------------
// 4
// --------------
//
// Sample Output
// -------------
// 0000110010111101
// -------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA3I {

    private static String repeatZeroes(int times) {
        return "0".repeat(Math.max(0, times));
    }

    private static String findAKUniversalCircularBinaryStringMachinery(int k) {
        int decK = k - 1;
        int numBinaryString = (int)Math.pow(2, k);
        String binaryString, prefix, suffix;
        Map<String, List<String>> deBruijnGraph = new HashMap<>();

        for (int i = 0; i < numBinaryString; ++i) {
            binaryString = Integer.toBinaryString(i);
            binaryString = "%s%s".formatted(
                    repeatZeroes(k - binaryString.length()),
                    binaryString);
            prefix = binaryString.substring(0, decK);
            suffix = binaryString.substring(1);

            if (deBruijnGraph.containsKey(prefix)) {
                deBruijnGraph.get(prefix).add(suffix);
            } else {
                deBruijnGraph.put(prefix, new ArrayList<>(List.of(suffix)));
            }
        }
        List<String> circuit = BA3F.findEulerianCycle(deBruijnGraph);
        StringBuilder circularString = new StringBuilder(circuit.getFirst());
        for (int i = 1; i < circuit.size() - decK; ++i) {
            circularString.append(circuit.get(i).charAt(decK - 1));
        }

        return circularString.toString();
    }

    public static String findAKUniversalCircularBinaryString(int k) {
        return findAKUniversalCircularBinaryStringMachinery(k);
    }
}
