// Construct a Trie from a Collection of Patterns
// ----------------------------------------------
//
// Reads will form a collection of strings Patterns that we wish to match against a reference
// genome Text. For each string in Patterns, we will first find all its exact matches as a
// substring of Text (or conclude that it does not appear in Text). When hunting for the cause of
// a genetic disorder, we can immediately eliminate from consideration areas of the reference
// genome where exact matches occur. We will later generalize this problem to find approximate
// matches, where single nucleotide substitutions in reads separate the individual from the
// reference genome (or represent errors in reads).
//
// Multiple Pattern Matching Problem: Find all occurrences of a collection of patterns in a text.
//     Input: A string Text and a collection Patterns containing (shorter) strings.
//     Output: All starting positions in Text where a string from Patterns appears as a substring.
//
// To solve this problem, we will consolidate Patterns into a directed tree called a trie
// (pronounced “try”), which is written Trie(Patterns) and has the following properties.
// 1) The trie has a single root node with indegree 0, denoted root.
// 2) Each edge of Trie(Patterns) is labeled with a letter of the alphabet.
// 3) Edges leading out of a given node have distinct labels.
// 4) Every string in Patterns is spelled out by concatenating the letters along some path from the
//    root downward.
// 5) Every path from the root to a leaf, or node with outdegree 0, spells a string from Patterns.
//
// The most obvious way to construct Trie(Patterns) is by iteratively adding each string from
// Patterns to the growing trie, as implemented by the following algorithm.
//
// --------------------------
// TRIECONSTRUCTION(Patterns)
//    Trie ← a graph consisting of a single node root
//    for each string Pattern in Patterns
//       currentNode ← root
//       for i ← 1 to |Pattern|
//          if there is an outgoing edge from currentNode with label currentSymbol
//             currentNode ← ending node of this edge
//          else
//             add a new node newNode to Trie
//             add a new edge from currentNode to newNode with label currentSymbol
//             currentNode ← newNode
//    return Trie
// --------------------------
//
// Trie Construction Problem
//
// Construct a trie on a collection of patterns.
//
// Given: A collection of strings Patterns.
//
// Return: The adjacency list corresponding to Trie(Patterns), in the following format. If
// Trie(Patterns) has n nodes, first label the root with 1 and then label the remaining nodes with
// the integers 2 through n in any order you like. Each edge of the adjacency list of Trie(Patterns)
// will be encoded by a triple: the first two members of the triple must be the integers labeling the
// initial and terminal nodes of the edge, respectively; the third member of the triple must be the
// symbol labeling the edge.
//
// Sample Dataset
// --------------
// ATAGA
// ATC
// GAT
// --------------
//
// Sample Output
// -------------
// 0->1:A
// 1->2:T
// 2->3:A
// 3->4:G
// 4->5:A
// 2->6:C
// 0->7:G
// 7->8:A
// 8->9:T
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BA9A {

    private static Map<Integer, Map<Integer, Character>> constructATrieMachinery(
            List<String> patterns
    ) {
        Map<Integer, Map<Integer, Character>> trie = new HashMap<>(Map.of(0, new HashMap<>()));
        int newNode = 0, currentNode;
        char currentSymbol;

        for (String pattern : patterns) {
            currentNode = 0;
            for (int i = 0; i != pattern.length(); ++i) {
                currentSymbol = pattern.charAt(i);
                if (trie.get(currentNode).containsValue(currentSymbol)) {
                    currentNode = BA9UTIL.getKeysByValue(trie.get(currentNode), currentSymbol)
                            .findFirst()
                            .orElseThrow();
                } else {
                    trie.put(++newNode, new HashMap<>());
                    trie.get(currentNode).put(newNode, currentSymbol);
                    currentNode = newNode;
                }
            }
        }

        return trie;
    }

    public static Map<Integer, Map<Integer, Character>> constructATrie(List<String> patterns) {
        return constructATrieMachinery(patterns);
    }

    public static Map<Integer, Map<Integer, Character>> constructATrie(Path path) {
        return constructATrieMachinery(UTIL.readDataset(path));
    }

    private void run() {
        Map<Integer, Map<Integer, Character>> trie = constructATrie(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9a.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9a_out.txt")) {
            for (int from : trie.keySet()) {
                for (int to : trie.get(from).keySet()) {
                    fileWriter.write("%d->%d:%c\n".formatted(
                            from,
                            to,
                            trie.get(from).get(to)
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9A().run();
    }
}
