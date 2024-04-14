// Construct the Suffix Tree of a String
// -------------------------------------
//
// Storing Trie(Patterns), which was introduced in “Implement TrieMatching”, requires a great deal
// of memory. So let’s process Text into a data structure instead. Our goal is to compare each
// string in Patterns against Text without needing to traverse Text from beginning to end. In more
// familiar terms, instead of packing Patterns onto a bus and riding the long distance down Text,
// our new data structure will be able to “teleport” each string in Patterns directly to its
// occurrences in Text.
//
// A suffix trie, denoted SuffixTrie(Text), is the trie formed from all suffixes of Text. From now
// on, we append the dollar-sign ("$") to Text in order to mark the end of Text. We will also label
// each leaf of the resulting trie by the starting position of the suffix whose path through the
// trie ends at this leaf (using 0-based indexing). This way, when we arrive at a leaf, we will
// immediately know where this suffix came from in Text.
//
// However, the runtime and memory required to construct SuffixTrie(Text) are both equal to the
// combined length of all suffixes in Text. There are |Text| suffixes of Text, ranging in length
// from 1 to |Text| and having total length |Text|·(|Text|+1)/2, which is O(|Text|^2). Thus, we
// need to reduce both the construction time and memory requirements of suffix tries to make them
// practical.
//
// Let’s not give up hope on suffix tries. We can reduce the number of edges in SuffixTrie(Text)
// by combining the edges on any non-branching path into a single edge. We then label this edge with
// the concatenation of symbols on the consolidated edges. The resulting data structure is called a
// suffix tree, written SuffixTree(Text).
//
// To match a single Pattern to Text, we thread Pattern into SuffixTree(Text) by the same process
// used for a suffix trie. Similarly to the suffix trie, we can use the leaf labels to find starting
// positions of successfully matched patterns.
//
// Suffix trees save memory because they do not need to store concatenated edge labels from each
// non-branching path. For example, a suffix tree does not need ten bytes to store the edge labeled
// "mabananas$" in SuffixTree("panamabananas$"); instead, it suffices to store a pointer to position
// 4 of "panamabananas$", as well as the length of "mabananas$". Furthermore, suffix trees can be
// constructed in linear time, without having to first construct the suffix trie! We will not ask
// you to implement this fast suffix tree construction algorithm because it is quite complex.
//
// Suffix Tree Construction Problem
//
// Construct the suffix tree of a string.
//
// Given: A string Text.
//
// Return: The strings labeling the edges of SuffixTree(Text). (You may return these strings in any
// order.)
//
// Sample Dataset
// --------------
// ATAAATG$
// --------------
//
// Sample Output
// -------------
// AAATG$
// G$
// T
// ATG$
// TG$
// A
// A
// AAATG$
// G$
// T
// G$
// $
// -------------

import auxil.Node;
import auxil.MSuffixTrie;
import auxil.MSuffixTree;
import auxil.MTreeEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class BA9C {

    private static final Node root = new Node(0);

    private static void dfs(
            MSuffixTrie trie, Node node, List<Node> currentPath,
            List<List<Node>> nonBranchingPaths
    ) {
        for (Node adj : trie.get(node).keySet()) {
            if (trie.isLeaf(adj)) {
                List<Node> nonBranchingPath = new ArrayList<>(currentPath);
                nonBranchingPath.add(adj);
                nonBranchingPaths.add(nonBranchingPath);
            } else if (trie.get(adj).size() > 1) {
                List<Node> nonBranchingPath = new ArrayList<>(currentPath);
                nonBranchingPath.add(adj);
                nonBranchingPaths.add(nonBranchingPath);
                dfs(trie, adj, new ArrayList<>(List.of(adj)), nonBranchingPaths);
            } else {
                currentPath.add(adj);
                dfs(trie, adj, currentPath, nonBranchingPaths);
                currentPath.removeLast();
            }
        }
    }

    private static List<List<Node>> getNonBranchingPaths(MSuffixTrie trie) {
        List<List<Node>> nonBranchingPaths = new ArrayList<>();
        List<Node> currentPath = new ArrayList<>(List.of(root));
        dfs(trie, root, currentPath, nonBranchingPaths);

        return nonBranchingPaths;
    }

    private static List<String> constructSuffixTreeMachinery(String text) {
        MSuffixTrie trie = BA9UTIL.constructModifiedSuffixTrie(text);
        MSuffixTree tree = new MSuffixTree(root);
        List<List<Node>> nonBranchingPaths = getNonBranchingPaths(trie);
        Node from, to;
        MTreeEdge edge;
        int position, length;
        List<String> edges = new ArrayList<>();

        for (List<Node> path : nonBranchingPaths) {
            from = path.getFirst();
            to = path.getLast();
            position = trie.get(from).get(path.get(1)).getPosition();
            length = path.size() - 1;
            edge = new MTreeEdge(position, length);
            tree.addEdge(from, to, edge);
        }

        for (Node node : tree.keySet()) {
            for (Node adj : tree.get(node).keySet()) {
                edge = tree.get(node).get(adj);
                position = edge.position();
                length = edge.length();
                edges.add(text.substring(position, position + length));
            }
        }
        return edges;
    }

    public static List<String> constructSuffixTree(String text) {
        return constructSuffixTreeMachinery(text);
    }

    public static List<String> constructSuffixTree(Path path) {
        String text = UTIL.readDataset(path).getFirst();
        return constructSuffixTreeMachinery(text);
    }

    private void run() {
        List<String> edges = constructSuffixTree(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9c.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9c_out.txt")) {
            for (String edge : edges) {
                fileWriter.write("%s\n".formatted(edge));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9C().run();
    }
}
