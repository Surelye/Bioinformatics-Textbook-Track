// Reconstruct a String from its k-mer Composition
// -----------------------------------------------
//
// String Reconstruction Problem
//
// Reconstruct a string from its k-mer composition.
//
// Given: An integer k followed by a list of k-mers Patterns.
//
// Return: A string Text with k-mer composition equal to Patterns. (If multiple answers exist, you
// may return any one.)
//
// Sample Dataset
// --------------
// 4
// CTTA
// ACCA
// TACC
// GGCT
// GCTT
// TTAC
// --------------
//
// Sample Output
// -------------
// GGCTTACCA
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA3H {

    private static String
    reconstructAStringFromItsKMerCompositionMachinery(int k, List<String> kMers) {
        int decK = k - 1;
        String prefix, suffix;
        Map<String, List<String>> deBruijnGraph = new HashMap<>();

        for (String kMer : kMers) {
            prefix = kMer.substring(0, decK);
            suffix = kMer.substring(1);

            if (deBruijnGraph.containsKey(prefix)) {
                deBruijnGraph.get(prefix).add(suffix);
            } else {
                deBruijnGraph.put(prefix, new ArrayList<>(List.of(suffix)));
            }
        }
        List<String> path = BA3G.findEulerianPath(deBruijnGraph);
        StringBuilder text = new StringBuilder(path.getFirst());
        for (int i = 1; i < path.size(); ++i) {
            text.append(path.get(i).charAt(decK - 1));
        }
        BA3UTIL.writePathToFile(List.of(text));

        return text.toString();
    }

    public static String reconstructAStringFromItsKMerComposition(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return reconstructAStringFromItsKMerCompositionMachinery(
                Integer.parseInt(strDataset.getFirst()),
                strDataset.stream().skip(1).toList());
    }

    public static String reconstructAStringFromItsKMerComposition(int k, List<String> kMers) {
        return reconstructAStringFromItsKMerCompositionMachinery(k, kMers);
    }
}
