// Implement AdditivePhylogeny
// ---------------------------
//
// The following recursive algorithm, called AdditivePhylogeny, finds the simple tree fitting an
// n x n additive distance matrix D. We assume that you have already implemented a program
// Limb(D, j) that computes LimbLength(j) for a leaf j based on the distance matrix D. Rather than
// selecting an arbitrary leaf j from Tree(D) for trimming, AdditivePhylogeny selects leaf n
// (corresponding to the last row and column of D).
//
// -----------------------
// AdditivePhylogeny(D, n)
//    if n = 2
//        return the tree consisting of a single edge of length D1,2
//    limbLength ← Limb(D, n)
//    for j ← 1 to n - 1
//        Dj,n ← Dj,n - limbLength
//        Dn,j ← Dj,n
//    (i,n,k) ← three leaves such that Di,k = Di,n + Dn,k
//    x ← Di,n
//    remove row n and column n from D
//    T ← AdditivePhylogeny(D, n - 1)
//    v ← the (potentially new) node in T at distance x from i on the path between i and k
//    add leaf n back to T by creating a limb (v, n) of length limbLength
//    return T
// -----------------------
//
// Additive Phylogeny Problem
//
// Construct the simple tree fitting an additive matrix.
//
// Given: n and a tab-delimited n x n additive matrix.
//
// Return: A weighted adjacency list for the simple tree fitting this matrix.
//
// Note on formatting: The adjacency list must have consecutive integer node labels starting from 0.
// The n leaves must be labeled 0, 1, ..., n-1 in order of their appearance in the distance matrix.
// Labels for internal nodes may be labeled in any order but must start from n and increase
// consecutively.
//
// Sample Dataset
// --------------
// 4
// 0   13  21  22
// 13  0   12  13
// 21  12  0   13
// 22  13  13  0
// --------------
//
// Sample Output
// -------------
// 0->4:11
// 1->4:2
// 2->5:6
// 3->5:7
// 4->0:11
// 4->1:2
// 4->5:4
// 5->4:4
// 5->3:7
// 5->2:6
// -------------

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BA7C {

    private static int internalNode;

    private static boolean
    dfs(Map<Integer, Map<Integer, Integer>> adjList, Map<Integer, Boolean> visited,
        int node, int to, List<Integer> path) {
        if (node == to) {
            return true;
        }
        visited.put(node, true);

        for (int v : adjList.get(node).keySet()) {
            if (!visited.get(v)) {
                path.add(v);
                if (dfs(adjList, visited, v, to, path)) {
                    return true;
                }
                path.removeLast();
                visited.put(v, false);
            }
        }

        return false;
    }

    private static List<Integer>
    findPath(Map<Integer, Map<Integer, Integer>> adjList, int from, int to) {
        List<Integer> path = new ArrayList<>();
        Map<Integer, Boolean> visited = new HashMap<>();
        for (int vertex : adjList.keySet()) {
            visited.put(vertex, false);
        }
        if (!dfs(adjList, visited, from, to, path)) {
            throw new RuntimeException("Path not found");
        }

        return path;
    }

    private static void
    removeLastRowAndColumn(List<List<Integer>> D) {
        int DSize = D.size();

        for (int i = 0; i != DSize - 1; ++i) {
            D.set(i, D.get(i).subList(0, DSize - 1));
        }
        D.removeLast();
    }

    private static Map<Integer, Map<Integer, Integer>>
    findAdditivePhylogenyMachinery(int n, List<List<Integer>> D) {
        if (n == 2) {
            int edgeWeight = D.getFirst().getLast();
            return new HashMap<>(Map.of(
                    0, new HashMap<>(Map.of(1, edgeWeight)),
                    1, new HashMap<>(Map.of(0, edgeWeight))
            ));
        }

        int limbLength = BA7B.findLimbLength(n, n - 1, D);
        for (int j = 0; j != n - 1; ++j) {
            D.get(j).set(n - 1, D.get(j).get(n - 1) - limbLength);
            D.get(n - 1).set(j, D.get(j).get(n - 1));
        }

        int iLeaf, kLeaf, dik, din, dnk, x;
        iLeaf = kLeaf = 0;
        outerCycle:
        for (int i = 0; i != n - 1; ++i) {
            for (int k = 0; k != n - 1; ++k) {
                if (i == k) {
                    continue;
                }
                dik = D.get(i).get(k);
                din = D.get(i).get(n - 1);
                dnk = D.get(n - 1).get(k);
                if (dik == din + dnk) {
                    iLeaf = i;
                    kLeaf = k;
                    break outerCycle;
                }
            }
        }
        if (iLeaf == 0 && kLeaf == 0) {
            throw new RuntimeException("Given matrix is not additive");
        }
        x = D.get(iLeaf).get(n - 1);

        removeLastRowAndColumn(D);
        var T = findAdditivePhylogenyMachinery(n - 1, D);
        List<Integer> path = findPath(T, iLeaf, kLeaf);
        int pathLength = 0, segmentLength, curNode = iLeaf;
        for (int pathNode : path) {
            segmentLength = T.get(curNode).get(pathNode);
            if (pathLength + segmentLength == x) {
                T.get(pathNode).put(n - 1, limbLength);
                T.put(n - 1, new HashMap<>(Map.of(pathNode, limbLength)));
                break;
            } else if (pathLength + segmentLength > x) {
                int diff = x - pathLength;
                T.get(curNode).remove(pathNode);
                T.get(pathNode).remove(curNode);
                T.get(curNode).put(internalNode, diff);
                T.get(pathNode).put(internalNode, segmentLength - diff);
                T.put(internalNode, new HashMap<>(Map.of(
                        curNode, diff,
                        pathNode, segmentLength - diff,
                        n - 1, limbLength
                )));
                T.put(n - 1, new HashMap<>(Map.of(internalNode++, limbLength)));
                break;
            }
            curNode = pathNode;
            pathLength += segmentLength;
        }

        return T;
    }

    public static Map<Integer, Map<Integer, Integer>>
    findAdditivePhylogeny(int n, List<List<Integer>> matrix) {
        internalNode = n;
        return findAdditivePhylogenyMachinery(n, matrix);
    }

    public static Map<Integer, Map<Integer, Integer>>
    findAdditivePhylogeny(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<List<Integer>> D = new ArrayList<>();
        for (int i = 1; i != strDataset.size(); ++i) {
            D.add(new ArrayList<>(UTIL.parseIntArray(strDataset.get(i))));
        }
        int n = Integer.parseInt(strDataset.getFirst());
        internalNode = n;

        return findAdditivePhylogenyMachinery(n, D);
    }

    private void run() {
        Map<Integer, Map<Integer, Integer>> adjList = findAdditivePhylogeny(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7c.txt"
                )
        );
        BA7UTIL.writeAdjListToFile("ba7c_out.txt", adjList);
    }

    public static void main(String[] args) {
        new BA7C().run();
    }
}
