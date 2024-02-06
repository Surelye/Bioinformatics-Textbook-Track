// Construct the De Bruijn Graph of a String
// -----------------------------------------
//
// Given a genome Text, PathGraph_k(Text) is the path consisting of |Text| - k + 1 edges, where the
// i-th edge of this path is labeled by the i-th k-mer in Text and the i-th node of the path is
// labeled by the i-th (k - 1)-mer in Text. The de Bruijn graph DeBruijn_k(Text) is formed by
// gluing identically labeled nodes in PathGraphk(Text).
//
// -------------------------------------
// De Bruijn Graph from a String Problem
//
// Construct the de Bruijn graph of a string.
//
// Given: An integer k and a string Text.
//
// Return:DeBruijnk(Text), in the form of an adjacency list.
//
// Sample Dataset
// --------------
// 4
// AAGATTCTCTAC
// --------------
//
// Sample Output
// -------------
// AAG -> AGA
// AGA -> GAT
// ATT -> TTC
// CTA -> TAC
// CTC -> TCT
// GAT -> ATT
// TCT -> CTA,CTC
// TTC -> TCT
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BA3D {

    private static Map<String, List<String>> constructDeBruijnGraphMachinery(int k, String text) {
        int decK = k - 1;
        int textLength = text.length();
        String prefix, suffix;
        Comparator<String> stringComparator = new LexicographicOrderStringComparator();
        Map<String, List<String>> deBruijnGraph = new TreeMap<>(stringComparator);

        for (int i = 0; i < textLength - k + 1; ++i) {
            prefix = text.substring(i, i + decK);
            suffix = text.substring(i + 1, i + 1 + decK);

            if (deBruijnGraph.containsKey(prefix)) {
                deBruijnGraph.get(prefix).add(suffix);
            } else {
                deBruijnGraph.put(prefix, new ArrayList<>(List.of(suffix)));
            }
        }
        BA3UTIL.sortGraphAdjLists(deBruijnGraph, stringComparator);
        BA3UTIL.writeGraphToFile(deBruijnGraph);

        return deBruijnGraph;
    }

    public static Map<String, List<String>> constructDeBruijnGraph(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return constructDeBruijnGraphMachinery(Integer.parseInt(strDataset.getFirst()),
                strDataset.getLast());
    }

    public static Map<String, List<String>> constructDeBruijnGraph(int k, String text) {
        return constructDeBruijnGraphMachinery(k, text);
    }
}
