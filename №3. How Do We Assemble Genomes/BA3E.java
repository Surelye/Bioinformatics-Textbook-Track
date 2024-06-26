// Construct the De Bruijn Graph of a Collection of k-mers
// -------------------------------------------------------
//
// Given an arbitrary collection of k-mers Patterns (where some k-mers may appear multiple times),
// we define CompositionGraph(Patterns) as a graph with |Patterns| isolated edges. Every edge is
// labeled by a k-mer from Patterns, and the starting and ending nodes of an edge are labeled by
// the prefix and suffix of the k-mer labeling that edge. We then define the de Bruijn graph of
// Patterns, denoted DeBruijn(Patterns), by gluing identically labeled nodes in
// CompositionGraph(Patterns), which yields the following algorithm.
//
// ------------------
// DEBRUIJN(Patterns)
//     represent every k-mer in Patterns as an isolated edge between its prefix and suffix
//     glue all nodes with identical labels, yielding the graph DeBruijn(Patterns)
//     return DeBruijn(Patterns)
// ------------------
//
// De Bruijn Graph from k-mers Problem
//
// Construct the de Bruijn graph from a collection of k-mers.
//
// Given: A collection of k-mers Patterns.
//
// Return: The de Bruijn graph DeBruijn(Patterns), in the form of an adjacency list.
//
// Sample Dataset
// --------------
// GAGG
// CAGG
// GGGG
// GGGA
// CAGG
// AGGG
// GGAG
// --------------
//
// Sample Output
// -------------
// AGG -> GGG
// CAG -> AGG,AGG
// GAG -> AGG
// GGA -> GAG
// GGG -> GGA,GGG
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA3E {

    private static Map<String, List<String>>
    constructDeBruijnGraphFromKMersMachinery(List<String> kMers) {
        int decK = kMers.getFirst().length() - 1;
        String prefix, suffix;
        Comparator<String> stringComparator = new LexicographicOrderStringComparator();
        Map<String, List<String>> deBruijnGraph = new TreeMap<>(stringComparator);

        for (String kMer : kMers) {
            prefix = kMer.substring(0, decK);
            suffix = kMer.substring(1);

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

    public static Map<String, List<String>> constructDeBruijnGraphFromKMers(Path path) {
        return constructDeBruijnGraphFromKMersMachinery(UTIL.readDataset(path));
    }

    public static Map<String, List<String>> constructDeBruijnGraphFromKMers(List<String> kMers) {
        return constructDeBruijnGraphFromKMersMachinery(kMers);
    }
}
