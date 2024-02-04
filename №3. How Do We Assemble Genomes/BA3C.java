// Construct the Overlap Graph of a Collection of k-mers
// -----------------------------------------------------
//
// In this chapter, we use the terms prefix and suffix to refer to the first k − 1 nucleotides and
// last k − 1 nucleotides of a k-mer, respectively.
//
// Given an arbitrary collection of k-mers Patterns, we form a graph having a node for each k-mer
// in Patterns and connect k-mers Pattern and Pattern' by a directed edge if Suffix(Pattern) is
// equal to Prefix(Pattern'). The resulting graph is called the overlap graph on these k-mers,
// denoted Overlap(Patterns).
//
// ---------------------
// Overlap Graph Problem
//
// Construct the overlap graph of a collection of k-mers.
//
// Given: A collection Patterns of k-mers.
//
// Return: The overlap graph Overlap(Patterns), in the form of an adjacency list.
//
// Sample Dataset
// --------------
// ATGCG
// GCATG
// CATGC
// AGGCA
// GGCAT
// --------------
//
// Sample Output
// -------------
// AGGCA -> GGCAT
// CATGC -> ATGCG
// GCATG -> CATGC
// GGCAT -> GCATG
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA3C {

    private static Map<String, List<String>>
    constructOverlapGraphMachinery(List<String> patterns) {
        int patternLength = patterns.getFirst().length();
        List<String> sortedPatterns = new ArrayList<>(patterns);
        sortedPatterns.sort(new LexicographicOrderStringComparator());
        Map<String, List<String>> overlapGraph = new HashMap<>();
        for (String pattern : sortedPatterns) {
            if (!overlapGraph.containsKey(pattern)) {
                overlapGraph.put(pattern, new ArrayList<>());
            }
        }

        for (String outerPattern : patterns) {
            String suffix = outerPattern.substring(1);
            for (String innerPattern : patterns) {
                if (suffix.equals(innerPattern.substring(0, patternLength - 1))) {
                    overlapGraph.get(outerPattern).add(innerPattern);
                }
            }
        }

        for (String pattern : sortedPatterns) {
            List<String> adjacents = overlapGraph.get(pattern);
            if (!adjacents.isEmpty()) {
                for (String adjacent : adjacents) {
                    System.out.printf("%s -> %s\n".formatted(pattern, adjacent));
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (String pattern : sortedPatterns) {
                List<String> adjacents = overlapGraph.get(pattern);
                if (!adjacents.isEmpty()) {
                    for (String adjacent : adjacents) {
                        fileWriter.write("%s -> %s\n".formatted(pattern, adjacent));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }

        return overlapGraph;
    }

    public static Map<String, List<String>> constructOverlapGraph(Path path) {
        return constructOverlapGraphMachinery(UTIL.readDataset(path));
    }

    public static Map<String, List<String>> constructOverlapGraph(List<String> patterns) {
        return constructOverlapGraphMachinery(patterns);
    }
}
