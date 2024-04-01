// Adapt SmallParsimony to Unrooted Trees
// --------------------------------------
//
// When the position of the root in an evolutionary tree is unknown, we can simply assign the root
// to any edge that we like, apply SmallParsimony from “Implement SmallParsimony” to the resulting
// rooted tree, and then remove the root. It can be shown that this method provides a solution to
// the following problem.
//
// Small Parsimony in an Unrooted Tree Problem
//
// Find the most parsimonious labeling of the internal nodes in an unrooted tree.
//
// Given: An unrooted binary tree with each leaf labeled by a string of length m.
//
// Return: A labeling of all other nodes of the tree by strings of length m that minimizes the
// parsimony score of the tree.
//
// Note on formatting: Your internal node labelings may differ from the sample provided.
//
// Sample Dataset
// --------------
// 4
// TCGGCCAA->4
// 4->TCGGCCAA
// CCTGGCTG->4
// 4->CCTGGCTG
// CACAGGAT->5
// 5->CACAGGAT
// TGAGTACC->5
// 5->TGAGTACC
// 4->5
// 5->4
// --------------
//
// Sample Output
// -------------
// 17
// TCGGCCAA->CCAGGCAC:4
// CCTGGCTG->CCAGGCAC:3
// TGAGTACC->CAAGGAAC:4
// CCAGGCAC->CCTGGCTG:3
// CCAGGCAC->CAAGGAAC:2
// CCAGGCAC->TCGGCCAA:4
// CACAGGAT->CAAGGAAC:4
// CAAGGAAC->CACAGGAT:4
// CAAGGAAC->TGAGTACC:4
// CAAGGAAC->CCAGGCAC:2
// -------------

import auxil.SPNode;
import auxil.SPGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA7G {

    private static void
    addRoot(List<SPNode> nodes, Map<Integer, List<Integer>> adjList, int lastFrom, int lastTo) {
        int root = Integer.max(lastFrom, lastTo) + 1;
        nodes.add(new SPNode(root, ""));
        adjList.put(root, List.of(lastFrom, lastTo));
    }

    private static void removeRoot(SPGraph T, int lastFrom, int lastTo) {
        int root = Integer.max(lastFrom, lastTo) + 1;
        T.nodes().removeLast();
        T.adjList().remove(root);
        T.adjList().get(lastFrom).add(lastTo);
    }

    public Map.Entry<Integer, SPGraph> smallParsimonyUnrooted(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int n = Integer.parseInt(strDataset.getFirst());
        List<SPNode> nodes = new ArrayList<>(n << 1);
        Map<Integer, List<Integer>> adjList = new HashMap<>(n);
        String[] edge;
        int from, toInt;
        String to;

        for (int i = 2; i <= (n << 1); i += 2) {
            edge = strDataset.get(i).split("->");
            to = edge[1];
            nodes.add(new SPNode((i >> 1) - 1, to));
        }
        for (int i = 2; i != strDataset.size() - 1; i += 2) {
            edge = strDataset.get(i).split("->");
            from = Integer.parseInt(edge[0]);
            to = edge[1];
            if (i <= (n << 1)) {
                toInt = (i >> 1) - 1;
            } else {
                toInt = Integer.parseInt(to);
            }
            if (adjList.containsKey(from)) {
                adjList.get(from).add(toInt);
            } else {
                adjList.put(from, new ArrayList<>(List.of(toInt)));
                nodes.add(new SPNode(from, ""));
            }
        }
        String[] lastEdge = strDataset.getLast().split("->");
        int lastFrom = Integer.parseInt(lastEdge[0]), lastTo = Integer.parseInt(lastEdge[1]);
        addRoot(nodes, adjList, lastFrom, lastTo);
        SPGraph T = new SPGraph(nodes, adjList);
        int parsimonyScore = BA7F.smallParsimony(T);
        removeRoot(T, lastFrom, lastTo);

        return Map.entry(parsimonyScore, T);
    }

    private void run() {
        Map.Entry<Integer, SPGraph> scoreAndT = smallParsimonyUnrooted(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7g.txt"
                )
        );

        int parsimonyScore = scoreAndT.getKey();
        SPGraph T = scoreAndT.getValue();
        try (FileWriter fileWriter = new FileWriter("ba7g_out.txt")) {
            String fDNA, sDNA;
            int HammingDistance;
            fileWriter.write("%d\n".formatted(parsimonyScore));
            for (int node : T.adjList().keySet()) {
                fDNA = T.getNode(node).getDNA().toString();
                for (int adj : T.adjList().get(node)) {
                    sDNA = T.getNode(adj).getDNA().toString();
                    HammingDistance = BA1G.HammingDistance(fDNA, sDNA);
                    fileWriter.write("%s->%s:%d\n%s->%s:%d\n".formatted(
                            fDNA, sDNA, HammingDistance,
                            sDNA, fDNA, HammingDistance
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA7G().run();
    }
}
