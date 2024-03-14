// Implement GreedySorting to Sort a Permutation by Reversals
// ----------------------------------------------------------
//
// Implement GreedySorting
//
// Given: A signed permutation P.
//
// Return: The sequence of permutations corresponding to applying GreedySorting to P, ending with
// the identity permutation.
//
// Sample Dataset
// --------------
// (-3 +4 +1 +5 -2)
// --------------
//
// Sample Output
// -------------
// (-1 -4 +3 +5 -2)
// (+1 -4 +3 +5 -2)
// (+1 +2 -5 -3 +4)
// (+1 +2 +3 +5 +4)
// (+1 +2 +3 -4 -5)
// (+1 +2 +3 +4 -5)
// (+1 +2 +3 +4 +5)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA6A {

    private static List<Integer> kSortingReversal(List<Integer> perm, int k) {
        int permSize = perm.size();
        int kIndex = perm.contains(k) ? perm.indexOf(k) : perm.indexOf(-k);
        List<Integer> kSorted = new ArrayList<>();

        for (int i = 0; i < k - 1; ++i) {
            kSorted.add(perm.get(i));
        }
        for (int i = kIndex; i >= k - 1; --i) {
            kSorted.add(-perm.get(i));
        }
        for (int i = kIndex + 1; i < permSize; ++i) {
            kSorted.add(perm.get(i));
        }

        return kSorted;
    }

    public static List<List<Integer>> greedySorting(List<Integer> permutation) {
        int permSize = permutation.size();
        List<Integer> currentPerm = new ArrayList<>(permutation);
        List<List<Integer>> permSeq = new ArrayList<>();

        for (int i = 0; i < permSize; ++i) {
            if (currentPerm.get(i) != i + 1) {
                currentPerm = kSortingReversal(currentPerm, i + 1);
                permSeq.add(currentPerm);
                if (currentPerm.get(i) == -(i + 1)) {
                    currentPerm = new ArrayList<>(currentPerm);
                    currentPerm.set(i, i + 1);
                    permSeq.add(currentPerm);
                }
            }
        }

        return permSeq;
    }

    public static List<List<Integer>> greedySorting(Path path) {
        List<Integer> permutation = BA6UTIL.parsePermutation(path);

        return greedySorting(permutation);
    }

    private void run() {
        List<List<Integer>> permSeq = greedySorting(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6a.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6a_out.txt")) {
            int permSize = permSeq.getFirst().size(), blockNum;
            for (List<Integer> perm : permSeq) {
                fileWriter.write('(');
                for (int i = 0; i < permSize; ++i) {
                    blockNum = perm.get(i);
                    fileWriter.write("%s%d%s".formatted(
                            (blockNum > 0) ? "+" : "",
                            blockNum,
                            (i == permSize - 1) ? ")\n" : " "
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6A().run();
    }
}
