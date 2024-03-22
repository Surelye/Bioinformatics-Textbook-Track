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
import java.util.*;

public class BA7C {

    private static int internalNode;

    private static List<List<Integer>>
    removeRowAndColumn(List<List<Integer>> D, int row, int column) {
        int DSize = D.size();
        List<Integer> currentRow;
        List<List<Integer>> newD = new ArrayList<>();

        for (int i = 0; i != DSize; ++i) {
            currentRow = new ArrayList<>();
            if (i == row) {
                continue;
            }
            for (int j = 0; j != DSize; ++j) {
                if (j == column) {
                    continue;
                }
                currentRow.add(D.get(i).get(j));
            }
            newD.add(currentRow);
        }

        return newD;
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
        iLeaf = kLeaf = dik = din = dnk = x = 0;
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
        x = D.get(iLeaf).get(n - 1);

        D = removeRowAndColumn(D, n - 1, n - 1);
        var T = findAdditivePhylogenyMachinery(n - 1, D);
        if (T.get(iLeaf).containsKey(kLeaf)) {
            dik = T.get(iLeaf).get(kLeaf);
            if (dik > x) {
                T.get(iLeaf).remove(kLeaf);
                T.get(kLeaf).remove(iLeaf);
                T.get(iLeaf).put(internalNode, x);
                T.get(kLeaf).put(internalNode, dik - x);
                T.put(internalNode, new HashMap<>(
                        Map.of(
                                iLeaf, x,
                                kLeaf, dik - x,
                                n - 1, limbLength
                        )
                ));
            }
        } else {
            int nNode = T.get(kLeaf).keySet().iterator().next();
            if (!(T.get(iLeaf).get(nNode) == din && T.get(kLeaf).get(nNode) == dnk)) {
                int curDnk = T.get(kLeaf).get(nNode);
                T.get(nNode).remove(kLeaf);
                T.get(kLeaf).remove(nNode);
                T.get(nNode).put(internalNode, curDnk - dnk);
                T.get(kLeaf).put(internalNode, dnk);
                T.put(internalNode, new HashMap<>(
                        Map.of(
                                kLeaf, dnk,
                                n - 1, limbLength,
                                nNode, curDnk - dnk
                        )
                ));
            } else {
                T.put(n - 1, new HashMap<>(Map.of(nNode, limbLength)));
                T.get(nNode).put(n - 1, limbLength);

                return T;
            }
        }
        T.put(n - 1, new HashMap<>(Map.of(internalNode++, limbLength)));

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
