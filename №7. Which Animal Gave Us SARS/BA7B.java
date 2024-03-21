// Compute Limb Lengths in a Tree
// ------------------------------
//
// Limb Length Problem
//
// Find the limb length for a leaf in a tree.
//
// Given: An integer n, followed by an integer j between 0 and n - 1, followed by a space-separated
// additive distance matrix D (whose elements are integers).
//
// Return: The limb length of the leaf in Tree(D) corresponding to row j of this distance matrix
// (use 0-based indexing).
//
// Sample Dataset
// --------------
// 4
// 1
// 0   13  21  22
// 13  0   12  13
// 21  12  0   13
// 22  13  13  0
// --------------
//
// Sample Output
// -------------
// 2
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA7B {

    private static int findLimbLengthMachinery(int n, int j, List<List<Integer>> D) {
        int limbLength = Integer.MAX_VALUE, dij;
        List<Integer> iRow, jRow = D.get(j);

        for (int i = 0; i != n; ++i) {
            if (i == j) {
                continue;
            }
            dij = D.get(i).get(j);
            iRow = D.get(i);
            for (int k = i + 1; k != n; ++k) {
                if (k == j) {
                    continue;
                }
                limbLength = Math.min(
                        (dij + jRow.get(k) - iRow.get(k)) >> 1,
                        limbLength
                );
            }
        }

        return limbLength;
    }

    public static int findLimbLength(int n, int j, List<List<Integer>> D) {
        return findLimbLengthMachinery(n, j, D);
    }

    public static int findLimbLength(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findLimbLengthMachinery(
                Integer.parseInt(strDataset.getFirst()),
                Integer.parseInt(strDataset.get(1)),
                strDataset
                        .stream()
                        .skip(2)
                        .map(UTIL::parseIntArray)
                        .toList()
        );
    }

    private void run() {
        int limbLength = findLimbLength(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba7b.txt"
                )
        );
        BA7UTIL.writeToFile("ba7b_out.txt", List.of(limbLength));
    }

    public static void main(String[] args) {
        new BA7B().run();
    }
}
