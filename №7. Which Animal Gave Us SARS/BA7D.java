// Implement UPGMA
// ---------------
//
// The pseudocode for UPGMA is shown below.
// ----------------------------------------
// UPGMA(D, n)
//    Clusters ← n single-element clusters labeled 1, ... , n
//    construct a graph T with n isolated nodes labeled by single elements 1, ... , n
//    for every node v in T
//       Age(v) ← 0
//    while there is more than one cluster
//       find the two closest clusters Ci and Cj 
//       merge Ci and Cj into a new cluster Cnew with |Ci| + |Cj| elements
//       add a new node labeled by cluster Cnew to T
//       connect node Cnew to Ci and Cj by directed edges 
//       remove the rows and columns of D corresponding to Ci and Cj 
//       remove Ci and Cj from Clusters 
//       add a row/column to D for Cnew by computing D(Cnew, C) for each C in Clusters
//       add Cnew to Clusters
//    root ← the node in T corresponding to the remaining cluster
//    for each edge (v, w) in T
//       length of (v, w) ← Age(v) - Age(w)
//    return T
// ----------------------------------------
//
// UPGMA Problem
//
// Construct the ultrametric tree resulting from UPGMA.
//
// Given: An integer n followed by a space-delimited n x n distance matrix.
//
// Return: An adjacency list for the ultrametric tree output by UPGMA. Weights should be accurate
// to three decimal places.
//
// Note on formatting: The adjacency list must have consecutive integer node labels starting from 0.
// The n leaves must be labeled 0, 1, ..., n-1 in order of their appearance in the distance matrix.
// Labels for internal nodes may be labeled in any order but must start from n and increase
// consecutively.
//
// Sample Dataset
// --------------
// 4
// 0   20  17  11
// 20  0   20  13
// 17  20  0   10
// 11  13  10  0
// --------------
//
// Sample Output
// -------------
// 0->5:7.000
// 1->6:8.833
// 2->4:5.000
// 3->4:5.000
// 4->2:5.000
// 4->3:5.000
// 4->5:2.000
// 5->0:7.000
// 5->4:2.000
// 5->6:1.833
// 6->5:1.833
// 6->1:8.833
// -------------

import auxil.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BA7D {

    private static int internalNode;

    private static void
    addRowAndColumn(Map<Integer, Map<Integer, Double>> D, List<Cluster> clusters, Cluster cluster) {
        int clusterLabel = cluster.getLabel();
        Map<Integer, Double> clusterDistances = Cluster.getDistancesBetweenClustersAndCluster(
                clusters, cluster, D
        );
        for (int nodeLabel : clusterDistances.keySet()) {
            D.get(nodeLabel).put(clusterLabel, clusterDistances.get(nodeLabel));
        }
        D.put(clusterLabel, clusterDistances);
    }

    private static void
    connectNodes(Map<Node, List<Node>> T, Node newNode, Node iClRepr, Node jClRepr) {
        T.put(newNode, new ArrayList<>(List.of(iClRepr, jClRepr)));
        T.get(iClRepr).add(newNode);
        T.get(jClRepr).add(newNode);
    }

    private static Map<Node, List<Node>>
    UPGMAMachinery(Map<Integer, Map<Integer, Double>> D, int n) {
        List<Cluster> clusters = Cluster.initClusters(n);
        Map<Node, List<Node>> T = new HashMap<>();
        Cluster newCluster;
        Node innerNode;
        for (int i = 0; i != n; ++i) {
            T.put(new Node(i, 0), new ArrayList<>());
        }

        int fLabel, sLabel;
        while (clusters.size() > 1) {
            Pair<Cluster, Cluster> closestClusters = Cluster.getClosestClusters(clusters, D);
            fLabel = closestClusters.getFirst().getLabel();
            sLabel = closestClusters.getSecond().getLabel();
            innerNode = new Node(internalNode++, D.get(fLabel).get(sLabel) / 2);
            newCluster = Cluster.mergeClusters(
                    closestClusters.getFirst(),
                    closestClusters.getSecond(),
                    innerNode
            );
            connectNodes(
                    T,
                    innerNode,
                    closestClusters.getFirst().getRepresentative(),
                    closestClusters.getSecond().getRepresentative()
            );
            clusters.remove(closestClusters.getFirst());
            clusters.remove(closestClusters.getSecond());
            addRowAndColumn(D, clusters, newCluster);
            clusters.add(newCluster);
        }

        return T;
    }

    public static Map<Node, List<Node>> UPGMA(int n, Map<Integer, Map<Integer, Double>> D) {
        internalNode = n;
        return UPGMAMachinery(D, n);
    }

    public static Map<Node, List<Node>> UPGMA(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int n = Integer.parseInt(strDataset.getFirst());
        internalNode = n;
        List<List<Integer>> DMatrix = strDataset
                .stream()
                .skip(1)
                .map(UTIL::parseIntArray)
                .toList();
        Map<Integer, Map<Integer, Double>> D = new HashMap<>();
        for (int i = 0; i != n; ++i) {
            D.put(i, new HashMap<>());
            for (int j = 0; j != n; ++j) {
                if (i != j) {
                    D.get(i).put(j, (double) DMatrix.get(i).get(j));
                }
            }
        }

        return UPGMAMachinery(D, n);
    }

    private void run() {
        Map<Node, List<Node>> adjList = UPGMA(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7d.txt"
                )
        );
        int from, to;
        double age;
        List<Edge> edges = new ArrayList<>();
        for (Node node : adjList.keySet()) {
            from = node.label();
            for (Node adj : adjList.get(node)) {
                to = adj.label();
                age = Math.abs(node.age() - adj.age());
                edges.add(new Edge(from, to, age));
            }
        }
        edges.sort(Edge::compareTo);

        try (FileWriter fileWriter = new FileWriter("ba7d_out.txt")) {
            for (Edge edge : edges) {
                fileWriter.write("%d->%d:%.3f\n".formatted(
                        edge.from(),
                        edge.to(),
                        edge.weight()
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA7D().run();
    }
}
