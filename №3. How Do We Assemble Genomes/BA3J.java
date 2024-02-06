// Reconstruct a String from its Paired Composition
// ------------------------------------------------
//
// Since increasing read length presents a difficult experimental problem, biologists have
// suggested an indirect way of increasing read length by generating read-pairs, which are pairs of
// reads separated by a fixed distance d in the genome.
//
// You can think about a read-pair as a long "gapped" read of length k + d + k whose first and last
// k-mers are known but whose middle segment of length d is unknown. Nevertheless, read-pairs
// contain more information than k-mers alone, and so we should be able to use them to improve our
// assemblies. If only you could infer the nucleotides in the middle segment of a read-pair, you
// would immediately increase the read length from k to 2 · k + d.
//
// Given a string Text, a (k,d)-mer is a pair of k-mers in Text separated by distance d. We use the
// notation (Pattern1|Pattern2) to refer to a (k,d)-mer whose k-mers are Pattern1 and Pattern2. The
// (k,d)-mer composition of Text, denoted PairedComposition_k,d(Text), is the collection of all
// (k,d)- mers in Text (including repeated (k,d)-mers).
//
// ---------------------------------------------
// String Reconstruction from Read-Pairs Problem
//
// Reconstruct a string from its paired composition.
//
// Given: Integers k and d followed by a collection of paired k-mers PairedReads.
//
// Return: A string Text with (k, d)-mer composition equal to PairedReads. (If multiple answers
// exist, you may return any one.)
//
// Sample Dataset
// --------------
// 4 2
// GAGA|TTGA
// TCGT|GATG
// CGTG|ATGT
// TGGT|TGAG
// GTGA|TGTT
// GTGG|GTGA
// TGAG|GTTG
// GGTC|GAGA
// GTCG|AGAT
// --------------
//
// Sample Output
// -------------
// GTGGTCGTGAGATGTTGA
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA3J {

    private static String
    reconstructAStringFromItsPairedCompositionMachinery(int k, int d, Map<Pair<String, String>,
            List<Pair<String, String>>> graph) {
        List<Pair<String, String>> kMerPairsEulerianPath = BA3G.findEulerianPath(graph);
        List<String> kDMers = kMerPairsEulerianPath
                .stream()
                .map(kMerPair -> kMerPair
                        .getFirst()
                        .concat("|")
                        .concat(kMerPair.getSecond()))
                .toList();
        String reconstructedString = BA3L.stringSpelledByGappedPatterns(kDMers, k - 1, d + 1);
        UTIL.writeToFile(List.of(reconstructedString));

        return reconstructedString;
    }

    public static String reconstructAStringFromItsPairedComposition(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> intParams = UTIL.parseIntArray(strDataset.getFirst());
        int decK = intParams.getFirst() - 1, d = intParams.getLast();
        Map<Pair<String, String>, List<Pair<String, String>>> pairedDeBruijnGraph = new HashMap<>();
        for (int i = 1; i < strDataset.size(); ++i) {
            String[] splitReadPair = strDataset.get(i).split("\\|");
            Pair<String, String> prefix = new Pair<>(splitReadPair[0].substring(0, decK),
                    splitReadPair[1].substring(0, decK));
            Pair<String, String> suffix = new Pair<>(splitReadPair[0].substring(1),
                    splitReadPair[1].substring(1));

            if (pairedDeBruijnGraph.containsKey(prefix)) {
                pairedDeBruijnGraph.get(prefix).add(suffix);
            } else {
                pairedDeBruijnGraph.put(prefix, new ArrayList<>(List.of(suffix)));
            }
        }

        return reconstructAStringFromItsPairedCompositionMachinery(decK + 1, d,
                pairedDeBruijnGraph);
    }

    public static String
    reconstructAStringFromItsPairedComposition(int k, int d, Map<Pair<String, String>,
            List<Pair<String, String>>> graph) {
        return reconstructAStringFromItsPairedCompositionMachinery(k, d, graph);
    }
}
