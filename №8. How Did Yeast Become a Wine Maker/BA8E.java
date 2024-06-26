// Implement Hierarchical Clustering
// ---------------------------------
//
// HierarchicalClustering, whose pseudocode is shown below, progressively generates n different
// partitions of the underlying data into clusters, all represented by a tree in which each node is
// labeled by a cluster of genes. The first partition has n single-element clusters represented by
// the leaves of the tree, with each element forming its own cluster. The second partition merges
// the two “closest” clusters into a single cluster consisting of two elements. In general, the
// i-th partition merges the two closest clusters from the (i - 1)-th partition and has n - i + 1
// clusters. We hope this algorithm looks familiar — it is UPGMA (from “Implement UPGMA”) in
// disguise.
//
// ----------------------------
// HierarchicalClustering(D, n)
//    Clusters ← n single-element clusters labeled 1, ... , n
//    construct a graph T with n isolated nodes labeled by single elements 1, ... , n
//    while there is more than one cluster
//       find the two closest clusters Ci and Cj
//       merge Ci and Cj into a new cluster C{new} with |Ci| + |Cj| elements
//       add a new node labeled by cluster C{new} to T
//       connect node C{new} to Ci and Cj by directed edges
//       remove the rows and columns of D corresponding to Ci and Cj
//       remove Ci and Cj from Clusters
//       add a row/column to D for C{new} by computing D(C{new}, C) for each C in Clusters
//       add C{new} to Clusters
//    assign root in T as a node with no incoming edges
//    return T
// ----------------------------
//
// Note that we have not yet defined how HierarchicalClustering computes the distance D(C{new}, C)
// between a newly formed cluster C{new} and each old cluster C. In practice, clustering algorithms
// vary in how they compute these distances, with results that can vary greatly. One commonly used
// approach defines the distance between clusters C1 and C2 as the smallest distance between any
// pair of elements from these clusters,
//
// D{min}(C1,C2) = min{all points i in cluster C1, all points j in cluster C2} D{i,j}.
//
// The distance function that we encountered with UPGMA uses the average distance between elements
// in two clusters,
//
// D{avg}(C1,C2)=(∑{all points i in cluster C1} ∑{all points j in cluster C2} Di,j) / |C1|⋅|C2|
//
// Implement Hierarchical Clustering
//
// Given: An integer n, followed by a nxn distance matrix.
//
// Return: The result of applying HierarchicalClustering to this distance matrix (using D{avg}),
// with each newly created cluster listed on each line.
//
// Sample Dataset
// --------------
// 7
// 0.00 0.74 0.85 0.54 0.83 0.92 0.89
// 0.74 0.00 1.59 1.35 1.20 1.48 1.55
// 0.85 1.59 0.00 0.63 1.13 0.69 0.73
// 0.54 1.35 0.63 0.00 0.66 0.43 0.88
// 0.83 1.20 1.13 0.66 0.00 0.72 0.55
// 0.92 1.48 0.69 0.43 0.72 0.00 0.80
// 0.89 1.55 0.73 0.88 0.55 0.80 0.00
// --------------
//
// Sample Output
// -------------
// 4 6
// 5 7
// 3 4 6
// 1 2
// 5 7 3 4 6
// 1 2 5 7 3 4 6
// -------------

import auxil.HCluster;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BA8E {

    private static int internalNode;

    private static void addRowAndColumn(
            Map<Integer, Map<Integer, Double>> D, List<HCluster> clusters, HCluster cluster
    ) {
        int clusterLabel = cluster.getLabel();
        Map<Integer, Double> clusterDistances = HCluster.getDistancesBetweenClustersAndCluster(
                clusters, cluster, D
        );
        for (int node : clusterDistances.keySet()) {
            D.get(node).put(clusterLabel, clusterDistances.get(node));
        }
        D.put(clusterLabel, clusterDistances);
    }

    private static List<HCluster>
    hierarchicalClusteringMachinery(Map<Integer, Map<Integer, Double>> D, int n) {
        List<HCluster> clusters = HCluster.initClusters(n);
        List<HCluster> newlyCreatedClusters = new ArrayList<>();
        HCluster fCluster, sCluster, newCluster;

        while (clusters.size() > 1) {
            Map.Entry<HCluster, HCluster> closestClusters = HCluster.getClosestClusters(clusters, D);
            fCluster = closestClusters.getKey();
            sCluster = closestClusters.getValue();
            newCluster = HCluster.mergeClusters(fCluster, sCluster, internalNode++);
            newlyCreatedClusters.add(newCluster);
            clusters.remove(fCluster);
            clusters.remove(sCluster);
            addRowAndColumn(D, clusters, newCluster);
            clusters.add(newCluster);
        }

        return newlyCreatedClusters;
    }

    public static List<HCluster> hierarchicalClustering(
            Map<Integer, Map<Integer, Double>> D, int n
    ) {
        return hierarchicalClusteringMachinery(D, n);
    }

    public static List<HCluster> hierarchicalClustering(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int n = Integer.parseInt(strDataset.getFirst());
        internalNode = n;
        List<List<Double>> DMatrix = strDataset
                .stream()
                .skip(1)
                .map(str -> BA8UTIL.parseDoubleArray(str, "\\s+"))
                .toList();
        Map<Integer, Map<Integer, Double>> D = new HashMap<>();
        for (int i = 0; i != n; ++i) {
            D.put(i, new HashMap<>());
            for (int j = 0; j != n; ++j) {
                if (i != j) {
                    D.get(i).put(j, DMatrix.get(i).get(j));
                }
            }
        }

        return hierarchicalClusteringMachinery(D, n);
    }

    private void run() {
        List<HCluster> newlyCreatedClusters = hierarchicalClustering(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba8e.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba8e_out.txt")) {
            int clusterSize;
            List<Integer> nodes;
            for (HCluster cluster : newlyCreatedClusters) {
                nodes = cluster.getNodes();
                clusterSize = cluster.size();
                for (int i = 0; i != clusterSize; ++i) {
                    fileWriter.write("%d%c".formatted(
                            nodes.get(i) + 1,
                            (i == clusterSize - 1) ? '\n' : ' '
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA8E().run();
    }
}
