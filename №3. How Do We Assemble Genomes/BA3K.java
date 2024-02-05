// Generate Contigs from a Collection of Reads
// -------------------------------------------
//
// Even after read breaking, most assemblies still have gaps in k-mer coverage, causing the de
// Bruijn graph to have missing edges, and so the search for an Eulerian path fails. In this case,
// biologists often settle on assembling contigs (long, contiguous segments of the genome) rather
// than entire chromosomes. For example, a typical bacterial sequencing project may result in about
// a hundred contigs, ranging in length from a few thousand to a few hundred thousand nucleotides.
// For most genomes, the order of these contigs along the genome remains unknown. Needless to say,
// biologists would prefer to have the entire genomic sequence, but the cost of ordering the contigs
// into a final assembly and closing the gaps using more expensive experimental methods is often
// prohibitive.
//
// Fortunately, we can derive contigs from the de Bruijn graph. A path in a graph is called
// non-branching if in(v) = out(v) = 1 for each intermediate node v of this path, i.e., for each
// node except possibly the starting and ending node of a path. A maximal non-branching path is a
// non-branching path that cannot be extended into a longer non-branching path. We are interested
// in these paths because the strings of nucleotides that they spell out must be present in any
// assembly with a given k-mer composition. For this reason, contigs correspond to strings spelled
// by maximal non-branching paths in the de Bruijn graph.
//
// -------------------------
// Contig Generation Problem
//
// Generate the contigs from a collection of reads (with imperfect coverage).
//
// Given: A collection of k-mers Patterns.
//
// Return: All contigs in DeBruijn(Patterns). (You may return the strings in any order.)
//
// Sample Dataset
// --------------
// ATG
// ATG
// TGT
// TGG
// CAT
// GGA
// GAT
// AGA
// --------------
//
// Sample Output
// -------------
// AGA ATG ATG CAT GAT TGGA TGT
// -------------

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA3K {

    private static Map<String, int[]> getInOutDegrees(Map<String, List<String>> graph) {
        Map<String, int[]> inOutDegrees = new HashMap<>();

        for (String node : graph.keySet()) {
            if (inOutDegrees.containsKey(node)) {
                inOutDegrees.get(node)[1] = graph.get(node).size();
            } else {
                inOutDegrees.put(node, new int[]{0, graph.get(node).size()});
            }

            for (String adjNode : graph.get(node)) {
                if (inOutDegrees.containsKey(adjNode)) {
                    inOutDegrees.get(adjNode)[0] += 1;
                } else {
                    inOutDegrees.put(adjNode, new int[]{1, 0});
                }
            }
        }

        return inOutDegrees;
    }

    private static List<String> generateContigsMachinery(List<String> patterns) {
        Map<String, List<String>> deBruijnGraph = BA3E.constructDeBruijnGraphFromKMers(patterns);
        Map<String, int[]> inOutDegrees = getInOutDegrees(deBruijnGraph);

        return List.of();
    }

    public static List<String> generateContigs(Path path) {
        List<String> patterns = UTIL.readDataset(path);

        return generateContigsMachinery(patterns);
    }

    public static List<String> generateContigs(List<String> patterns) {
        return generateContigsMachinery(patterns);
    }
}
