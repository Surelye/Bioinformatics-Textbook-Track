// Implement the Neighbor Joining Algorithm
// ----------------------------------------
//
// The pseudocode below summarizes the neighbor-joining algorithm.
//
// --------------------
// NeighborJoining(D,n)
//    if n = 2
//       T ← tree consisting of a single edge of length D1,2
//       return T
//    D' ← neighbor-joining matrix constructed from the distance matrix D
//    find elements i and j such that D'i,j is a minimum non-diagonal element of D'
//    Δ ← (TotalDistanceD(i) - TotalDistanceD(j)) /(n - 2)
//    limbLengthi ← (1/2)(Di,j + Δ)
//    limbLengthj ← (1/2)(Di,j - Δ)
//    add a new row/column m to D so that Dk,m = Dm,k = (1/2)(Dk,i + Dk,j - Di,j) for any k
//    remove rows i and j from D
//    remove columns i and j from D
//    T ← NeighborJoining(D, n - 1)
//    add two new limbs (connecting node m with leaves i and j) to the tree T
//    assign length limbLengthi to Limb(i)
//    assign length limbLengthj to Limb(j)
//    return T
// --------------------
//
// Neighbor Joining Problem
//
// Construct the tree resulting from applying the neighbor-joining algorithm to a distance matrix.
//
// Given: An integer n, followed by a space-separated n x n distance matrix.
//
// Return: An adjacency list for the tree resulting from applying the neighbor-joining algorithm.
// Edge-weights should be accurate to two decimal places (they are provided to three decimal places
// in the sample output below).
//
// Note on formatting: The adjacency list must have consecutive integer node labels starting from 0.
// The n leaves must be labeled 0, 1, ..., n-1 in order of their appearance in the distance matrix.
// Labels for internal nodes may be labeled in any order but must start from n and increase
// consecutively.
//
// Sample Dataset
// --------------
// 4
// 0   23  27  20
// 23  0   30  28
// 27  30  0   30
// 20  28  30  0
// --------------
//
// Sample Output
// -------------
// 0->4:8.000
// 1->5:13.500
// 2->5:16.500
// 3->4:12.000
// 4->5:2.000
// 4->0:8.000
// 4->3:12.000
// 5->1:13.500
// 5->2:16.500
// 5->4:2.000
// -------------

import auxil.Pair;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class BA7E {

    private static int internalNode;

    private static Map<Integer, Double>
    computeTotalDistances(Map<Integer, Map<Integer, Double>> D) {
        double distance;
        Map<Integer, Double> totalDistances = new HashMap<>(D.size());

        for (int node : D.keySet()) {
            distance = 0;
            for (int adj : D.get(node).keySet()) {
                distance += D.get(node).get(adj);
            }
            totalDistances.put(node, distance);
        }

        return totalDistances;
    }

    private static Map<Integer, Map<Integer, Double>>
    constructNeighborJoiningMatrix(
            Map<Integer, Map<Integer, Double>> D,
            Map<Integer, Double> distances) {
        int n = D.size();
        double val;
        Map<Integer, Map<Integer, Double>> njMatrix = new HashMap<>(n);
        for (int node : D.keySet()) {
            njMatrix.put(node, new HashMap<>());
        }

        for (int node : D.keySet()) {
            for (int adj : D.get(node).keySet()) {
                if (njMatrix.get(node).containsKey(adj)) {
                    continue;
                }
                val = (n - 2) * D.get(node).get(adj) - distances.get(node) - distances.get(adj);
                njMatrix.get(node).put(adj, val);
                njMatrix.get(adj).put(node, val);
            }
        }

        return njMatrix;
    }

    private static Pair<Integer, Integer>
    findMinimumInNJMatrix(Map<Integer, Map<Integer, Double>> njMatrix) {
        int defaultNode = njMatrix.keySet().iterator().next();
        Pair<Integer, Integer> minIndices = new Pair<>(defaultNode, defaultNode);
        double min = Double.MAX_VALUE, cur;

        for (int node : njMatrix.keySet()) {
            for (int adj : njMatrix.get(node).keySet()) {
                cur = njMatrix.get(node).get(adj);
                if (cur < min) {
                    min = cur;
                    minIndices.setBoth(node, adj);
                }
            }
        }

        return minIndices;
    }

    private static void addNewRowAndColumn(Map<Integer, Map<Integer, Double>> D, int i, int j) {
        double dkm, dki, dkj, dij = D.get(i).get(j);
        Map<Integer, Double> distances = new HashMap<>(D.size() - 2);

        for (int node : D.keySet()) {
            if (node != i && node != j) {
                dki = D.get(node).get(i);
                dkj = D.get(node).get(j);
                dkm = (dki + dkj - dij) / 2;
                distances.put(node, dkm);
                D.get(node).put(internalNode, dkm);
            }
        }
        D.put(internalNode, distances);
    }

    private static void removeRowsAndColumns(Map<Integer, Map<Integer, Double>> D, int i, int j) {
        D.remove(i);
        D.remove(j);

        for (int node : D.keySet()) {
            D.get(node).remove(i);
            D.get(node).remove(j);
        }
    }

    private static void addNewLimbs(
            Map<Integer, Map<Integer, Double>> T, int node,
            int limbI, double limbLenI, int limbJ, double limbLenJ) {
        T.get(node).putAll(Map.of(limbI, limbLenI, limbJ, limbLenJ));
        T.put(limbI, new HashMap<>(Map.of(node, limbLenI)));
        T.put(limbJ, new HashMap<>(Map.of(node, limbLenJ)));
    }

    private static Map<Integer, Map<Integer, Double>>
    neighborJoiningMachinery(Map<Integer, Map<Integer, Double>> D, int n) {
        if (n == 2) {
            var key = D.keySet().iterator();
            int node1 = key.next(), node2 = key.next();
            double edgeWeight = D.get(node1).get(node2);

            return new HashMap<>(Map.of(
                    node1, new HashMap<>(Map.of(node2, edgeWeight)),
                    node2, new HashMap<>(Map.of(node1, edgeWeight))
            ));
        }

        Map<Integer, Double> distances = computeTotalDistances(D);
        Map<Integer, Map<Integer, Double>> njMatrix = constructNeighborJoiningMatrix(D, distances);
        Pair<Integer, Integer> iAndJ = findMinimumInNJMatrix(njMatrix);
        int i = iAndJ.getFirst(), j = iAndJ.getSecond();
        double delta = (distances.get(i) - distances.get(j)) / (n - 2);
        double limbLenI = (D.get(i).get(j) + delta) / 2, limbLenJ = (D.get(i).get(j) - delta) / 2;
        addNewRowAndColumn(D, i, j);
        removeRowsAndColumns(D, i, j);
        int m = internalNode++;
        var T = neighborJoiningMachinery(D, n - 1);
        addNewLimbs(T, m, i, limbLenI, j, limbLenJ);

        return T;
    }

    public static Map<Integer, Map<Integer, Double>>
    neighborJoining(Map<Integer, Map<Integer, Double>> D, int n) {
        internalNode = n;
        return neighborJoiningMachinery(D, n);
    }

    public static Map<Integer, Map<Integer, Double>> neighborJoining(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int n = Integer.parseInt(strDataset.getFirst());
        internalNode = n;
        Map<Integer, Map<Integer, Double>> D = new HashMap<>();
        List<Integer> row;

        for (int i = 0; i != n; ++i) {
            D.put(i, new HashMap<>());
            row = UTIL.parseIntArray(strDataset.get(i + 1));
            for (int j = 0; j != n; ++j) {
                if (i != j) {
                    D.get(i).put(j, ((double) row.get(j)));
                }
            }
        }

        return neighborJoiningMachinery(D, n);
    }

    private void run() {
        var adjList = neighborJoining(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7e.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba7e_out.txt")) {
            int numNodes = adjList.size();
            for (int node = 0; node != numNodes; ++node) {
                for (int adj : adjList.get(node).keySet()) {
                    fileWriter.write("%d->%d:%.3f\n".formatted(
                            node, adj, adjList.get(node).get(adj)
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA7E().run();
    }
}