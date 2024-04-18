// Implement TreeColoring
// ----------------------
//
// In “Find the Longest Substring Shared by Two Strings”, we introduced the Longest Shared
// Substring Problem. A naive approach for finding a longest shared substring of strings Text1
// and Text2 would construct one suffix tree for Text1 and another for Text2. Instead, we will
// add "#" to the end of Text1, add "$" to the end of Text2, and then construct the single
// suffix tree for the concatenation of Text1 and Text2. We color a leaf in this suffix tree
// blue if it is labeled by the starting position of a suffix starting in Text1; we color a leaf
// red if it is labeled by the starting position of a suffix starting in Text2.
//
// We also color the remaining nodes of the suffix tree blue, red, and purple according to the
// following rules:
// 1) a node is colored blue or red if all leaves in its subtree (i.e., the subtree beneath it)
//    are all blue or all red, respectively;
// 2) a node is colored purple if its subtree contains both blue and red leaves.
//
// We use Color(v) to denote the color of node v.
//
// In order to find the longest shared substring between Text1 and Text2, we need to examine all
// purple nodes as well as the strings spelled by paths leading to the purple nodes. A longest
// such string yields a solution to the Longest Shared Substring Problem.
//
// TREECOLORING, whose pseudocode is shown below, colors the nodes of a suffix tree from the
// leaves upward. This algorithm assumes that the leaves of the suffix tree have been labeled
// "blue" or "red" and all other nodes have been labeled "gray". A node in a tree is called ripe
// if it is gray but has no gray children.
//
// -------------------------
// TREECOLORING(ColoredTree)
//    while ColoredTree has ripe nodes
//       for each ripe node v in ColoredTree
//          if there exist differently colored children of v
//             Color(v) ← "purple"
//          else
//             Color(v) ← color of all children of v
//    return ColoredTree
// -------------------------
//
// Tree Coloring Problem
//
// Color the internal nodes of a suffix tree given colors of the leaves.
//
// Given: An adjacency list, followed by color labels for leaf nodes.
//
// Return: Color labels for all nodes, in any order.
//
// Sample Dataset
// --------------
// 0 -> {}
// 1 -> {}
// 2 -> 0,1
// 3 -> {}
// 4 -> {}
// 5 -> 3,2
// 6 -> {}
// 7 -> 4,5,6
// -
// 0: red
// 1: red
// 3: blue
// 4: blue
// 6: red
// --------------
//
// Sample Output
// -------------
// 0: red
// 1: red
// 2: red
// 3: blue
// 4: blue
// 5: purple
// 6: red
// 7: purple
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BA9P {

    private static List<Integer> getRipeNodes(
            List<List<Integer>> adjList, List<BA9UTIL.Color> colorLabels
    ) {
        int numNodes = adjList.size();
        List<Integer> ripeNodes = new ArrayList<>();

        outerLoop:
        for (int node = 0; node != numNodes; ++node) {
            if (colorLabels.get(node) == BA9UTIL.Color.GRAY) {
                for (int adj : adjList.get(node)) {
                    if (colorLabels.get(adj) == BA9UTIL.Color.GRAY) {
                        continue outerLoop;
                    }
                }
                ripeNodes.add(node);
            }
        }

        return ripeNodes;
    }

    private static boolean areDifferentlyColored(
            List<Integer> nodes, List<BA9UTIL.Color> colorLabels
    ) {
        byte red = 0, blue = 0, purple = 0;

        for (int node : nodes) {
            if (colorLabels.get(node) == BA9UTIL.Color.RED) {
                red = 1;
            } else if (colorLabels.get(node) == BA9UTIL.Color.BLUE) {
                blue = 1;
            } else if (colorLabels.get(node) == BA9UTIL.Color.PURPLE) {
                purple = 1;
            }
        }

        return (red + blue + purple) > 1;
    }

    private static void treeColoringMachinery(
            List<List<Integer>> adjList, List<BA9UTIL.Color> colorLabels
    ) {
        List<Integer> ripes;

        while (true) {
            ripes = getRipeNodes(adjList, colorLabels);
            if (ripes.isEmpty()) {
                break;
            }
            for (int ripe : ripes) {
                if (areDifferentlyColored(adjList.get(ripe), colorLabels)) {
                    colorLabels.set(ripe, BA9UTIL.Color.PURPLE);
                } else {
                    colorLabels.set(
                            ripe,
                            colorLabels.get(adjList.get(ripe).getFirst())
                    );
                }
            }
        }
    }

    public static void treeColoring(
            List<List<Integer>> adjList, List<BA9UTIL.Color> colorLabels
    ) {
        treeColoringMachinery(adjList, colorLabels);
    }

    public static List<BA9UTIL.Color> treeColoring(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int nRows = strDataset.size(), colorsIndex, from;
        String[] split;
        List<List<Integer>> adjList = new ArrayList<>();
        List<BA9UTIL.Color> colorLabels = new ArrayList<>();
        for (int i = 0; ; ++i) {
            if (strDataset.get(i).equals("-")) {
                colorsIndex = i + 1;
                break;
            } else {
                split = strDataset.get(i).split(" -> ");
                from = Integer.parseInt(split[0]);
                adjList.add(new ArrayList<>());
                colorLabels.add(BA9UTIL.Color.GRAY);
                if (!split[1].equals("{}")) {
                    adjList.get(from).addAll(
                            Arrays.stream(split[1].split(","))
                                    .map(Integer::parseInt)
                                    .toList()
                    );
                }
            }
        }
        for (int i = colorsIndex; i != nRows; ++i) {
            split = strDataset.get(i).split(": ");
            from = Integer.parseInt(split[0]);
            if (split[1].equals("red")) {
                colorLabels.set(from, BA9UTIL.Color.RED);
            } else if (split[1].equals("blue")) {
                colorLabels.set(from, BA9UTIL.Color.BLUE);
            } else {
                throw new RuntimeException("Incorrect color provided");
            }
        }
        treeColoringMachinery(adjList, colorLabels);

        return colorLabels;
    }

    private void run() {
        List<BA9UTIL.Color> colorLabels = treeColoring(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9p.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9p_out.txt")) {
            int numNodes = colorLabels.size();
            for (int i = 0; i != numNodes; ++i) {
                fileWriter.write("%d: %s\n".formatted(
                        i,
                        colorLabels.get(i)
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9P().run();
    }
}
