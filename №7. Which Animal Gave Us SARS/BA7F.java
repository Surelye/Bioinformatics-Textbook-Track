// Implement SmallParsimony
// ------------------------
//
// The pseudocode for SmallParsimony is shown below. It returns the parsimony score for a binary
// rooted tree T whose leaves are labeled by symbols stored in an array Character (i.e.,
// Character(v) is the label of leaf v). At each iteration, it selects a node v and computes sk(v)
// for each symbol k in the alphabet. For each node v, SmallParsimony maintains a value Tag(v),
// which indicates whether the node has been processed (i.e., Tag(v) = 1 if the array sk(v) has
// been computed and Tag(v) = 0 otherwise). We call an internal node of T ripe if its tag is 0 but
// its children’s tags are both 1. SmallParsimony works upward from the leaves, finding a ripe node
// v at which to compute sk(v) at each step.
//
// ----------------------------
// SmallParsimony(T, Character)
//    for each node v in tree T
//       Tag(v) ← 0
//       if v is a leaf
//          Tag(v) ← 1
//          for each symbol k in the alphabet
//             if Character(v) = k
//                sk(v) ← 0
//             else
//                sk(v) ← ∞
//    while there exist ripe nodes in T
//       v ← a ripe node in T
//       Tag(v) ← 1
//       for each symbol k in the alphabet
//          sk(v) ← minimum over all symbols i {si(Daughter(v))+δi,k} + minimum over all symbols j {sj(Son(v))+δj,k}
//    return minimum over all symbols k {sk(v)}
// ----------------------------
//
// Small Parsimony Problem
//
// Find the most parsimonious labeling of the internal nodes of a rooted tree.
//
// Given: An integer n followed by an adjacency list for a rooted binary tree with n leaves labeled
// by DNA strings.
//
// Return: The minimum parsimony score of this tree, followed by the adjacency list of the tree
// corresponding to labeling internal nodes by DNA strings in order to minimize the parsimony score
// of the tree.
//
// Note: Remember to run SmallParsimony on each individual index of the strings at the leaves of the
// tree.
//
// Sample Dataset
// --------------
// 4
// 4->CAAATCCC
// 4->ATTGCGAC
// 5->CTGCGCTG
// 5->ATGGACGA
// 6->4
// 6->5
// --------------
//
// Sample Output
// -------------
// 16
// ATTGCGAC->ATAGCCAC:2
// ATAGACAA->ATAGCCAC:2
// ATAGACAA->ATGGACTA:2
// ATGGACGA->ATGGACTA:1
// CTGCGCTG->ATGGACTA:4
// ATGGACTA->CTGCGCTG:4
// ATGGACTA->ATGGACGA:1
// ATGGACTA->ATAGACAA:2
// ATAGCCAC->CAAATCCC:5
// ATAGCCAC->ATTGCGAC:2
// ATAGCCAC->ATAGACAA:2
// CAAATCCC->ATAGCCAC:5
// -------------

import auxil.SPNode;
import auxil.SPGraph;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BA7F {

    private static final List<Character> alphabet = List.of('A', 'C', 'G', 'T');

    private static char indexToNucleotide(int index) {
        return switch (index) {
            case 0 -> 'A';
            case 1 -> 'C';
            case 2 -> 'G';
            case 3 -> 'T';
            default -> throw new RuntimeException("Incorrect nucleotide index");
        };
    }

    private static SPNode findRipe(SPGraph T) {
        next:
        for (SPNode node : T.nodes()) {
            if (node.isTagged()) {
                continue;
            }
            for (SPNode adj : T.getAdjacents(node.getIndex())) {
                if (!adj.isTagged()) {
                    continue next;
                }
            }
            return node;
        }

        return new SPNode(-1, "");
    }

    private static Map.Entry<Integer, List<Integer>> findMinAtIndices(List<Integer> list) {
        int lSize = list.size();
        int min = Integer.MAX_VALUE;
        Map.Entry<Integer, List<Integer>> minAtIndices;

        for (int i = 0; i != lSize; ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
            }
        }
        minAtIndices = Map.entry(min, new ArrayList<>());

        for (int i = 0; i != lSize; ++i) {
            if (list.get(i) == min) {
                minAtIndices.getValue().add(i);
            }
        }

        return minAtIndices;
    }

    private static int findMinEachSymbol(
            int sumMins, List<Integer> dIndices, List<Integer> sIndices, int nucIndex) {
        int min = Integer.MAX_VALUE, sum;

        for (int dIndex : dIndices) {
            for (int sIndex : sIndices) {
                sum = sumMins + (dIndex == nucIndex ? 0 : 1) + (sIndex == nucIndex ? 0 : 1);
                if (sum < min) {
                    min = sum;
                }
            }
        }

        return min;
    }

    private static void dfs(SPGraph T, int node, int charIndex) {
        for (SPNode adj : T.getAdjacents(node)) {
            if (adj.isLeaf()) {
                continue;
            }
            List<Integer> scores = adj.getScores();
            int min = Integer.MAX_VALUE, index = 0;
            int[] deltas = {1, 1, 1, 1};
            deltas[charIndex] = 0;
            for (int i = 0; i != 4; ++i) {
                if (scores.get(i) + deltas[i] < min) {
                    min = scores.get(i) + deltas[i];
                    index = i;
                }
            }
            T.getNode(adj.getIndex()).getDNA().append(indexToNucleotide(index));
            dfs(T, adj.getIndex(), index);
        }
    }

    private static void assignSymbols(SPGraph T) {
        int rootIndex = T.nodes().getLast().getIndex();
        int charIndex = findMinAtIndices(T.getNode(rootIndex).getScores()).getValue().getFirst();
        T.getNode(rootIndex).getDNA().append(indexToNucleotide(charIndex));

        dfs(T, rootIndex, charIndex);
    }

    private static int smallParsimonyEach(SPGraph T, int index) {
        SPNode v, daughter, son;
        Map.Entry<Integer, List<Integer>> dMinAtIndices, sMinAtIndices;
        List<Integer> dIndices, sIndices;
        int sumMins, minEachSymbol;

        for (SPNode node : T.nodes()) {
            node.untag();
            node.getScores().clear();
            if (node.isLeaf()) {
                node.tag();
                char nucleotide = node.getDNA().charAt(index);
                for (char symbol : alphabet) {
                    if (nucleotide == symbol) {
                        node.getScores().add(0);
                    } else {
                        node.getScores().add(Integer.MAX_VALUE);
                    }
                }
            }
        }

        while (true) {
            v = findRipe(T);
            if (v.getIndex() == -1) {
                break;
            }
            v.tag();
            daughter = T.getAdjacents(v.getIndex()).getFirst();
            son = T.getAdjacents(v.getIndex()).getLast();
            dMinAtIndices = findMinAtIndices(daughter.getScores());
            sMinAtIndices = findMinAtIndices(son.getScores());
            sumMins = dMinAtIndices.getKey() + sMinAtIndices.getKey();
            dIndices = dMinAtIndices.getValue();
            sIndices = sMinAtIndices.getValue();
            for (int i = 0; i != 4; ++i) {
                minEachSymbol = findMinEachSymbol(sumMins, dIndices, sIndices, i);
                v.getScores().add(minEachSymbol);
            }
        }
        assignSymbols(T);

        return T.nodes().getLast()
                .getScores()
                .stream()
                .min(Integer::compare)
                .orElseThrow();
    }

    private static int smallParsimonyMachinery(SPGraph T) {
        int m = T.getNode(0).getDNA().length();
        int parsimonyScore = 0;

        for (int i = 0; i != m; ++i) {
            parsimonyScore += smallParsimonyEach(T, i);
        }

        return parsimonyScore;
    }

    public static int smallParsimony(SPGraph T) {
        return smallParsimonyMachinery(T);
    }

    public static Map.Entry<Integer, SPGraph> smallParsimony(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int n = Integer.parseInt(strDataset.getFirst());
        List<SPNode> nodes = new ArrayList<>(n << 1);
        Map<Integer, List<Integer>> adjList = new HashMap<>(n);
        String[] edge;
        int from, toInt;
        String to;

        for (int i = 1; i <= n; ++i) {
            edge = strDataset.get(i).split("->");
            to = edge[1];
            nodes.add(new SPNode(i - 1, to));
        }
        for (int i = 1; i != strDataset.size(); ++i) {
            edge = strDataset.get(i).split("->");
            from = Integer.parseInt(edge[0]);
            to = edge[1];
            if (i <= n) {
                toInt = i - 1;
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
        SPGraph T = new SPGraph(nodes, adjList);
        int parsimonyScore = smallParsimonyMachinery(T);

        return Map.entry(parsimonyScore, T);
    }

    private void run() {
        Map.Entry<Integer, SPGraph> scoreAndT = smallParsimony(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7f.txt"
                )
        );

        String fDNA, sDNA;
        int HammingDistance;
        try (FileWriter fileWriter = new FileWriter("ba7f_out.txt")) {
            int parsimonyScore = scoreAndT.getKey();
            SPGraph T = scoreAndT.getValue();
            fileWriter.write("%d\n".formatted(parsimonyScore));
            for (SPNode node : T.nodes()) {
                fDNA = node.getDNA().toString();
                for (SPNode adj : T.getAdjacents(node.getIndex())) {
                    sDNA = adj.getDNA().toString();
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
        new BA7F().run();
    }
}