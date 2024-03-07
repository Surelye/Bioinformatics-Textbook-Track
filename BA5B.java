// Find the Length of a Longest Path in a Manhattan-like Grid
// ----------------------------------------------------------
//
// Length of a Longest Path in the Manhattan Tourist Problem
//
// Find the length of a longest path in a rectangular city.
//
// Given: Integers n and m, followed by an n × (m+1) matrix Down and an (n+1) × m matrix Right.
// The two matrices are separated by the "-" symbol.
//
// Return: The length of a longest path from source (0, 0) to sink (n, m) in the n × m rectangular
// grid whose edges are defined by the matrices Down and Right.
//
// Sample Dataset
// --------------
// 4 4
// 1 0 2 4 3
// 4 6 5 2 1
// 4 4 5 2 1
// 5 6 8 5 3
// -
// 3 2 4 0
// 3 2 4 2
// 0 7 3 3
// 3 3 0 2
// 1 3 2 2
// --------------
//
// Sample Output
// -------------
// 34
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA5B {

    public int
    ManhattanTouristMachinery(int n, int m, List<List<Integer>> down, List<List<Integer>> right) {
        int[][] pathLengths = new int[n + 1][m + 1];
        for (int i = 1; i <= n; ++i) {
            pathLengths[i][0] = pathLengths[i - 1][0] + down.get(i - 1).getFirst();
        }
        List<Integer> firstRow = right.getFirst();
        for (int i = 1; i <= m; ++i) {
            pathLengths[0][i] = pathLengths[0][i - 1] + firstRow.get(i - 1);
        }

        List<Integer> prevDown, curRight;
        for (int i = 1; i <= n; ++i) {
            prevDown = down.get(i - 1);
            curRight = right.get(i);
            for (int j = 1; j <= m; ++j) {
                int downEdge = prevDown.get(j);
                int rightEdge = curRight.get(j - 1);
                pathLengths[i][j] = Math.max(
                        pathLengths[i - 1][j] + downEdge,
                        pathLengths[i][j - 1] + rightEdge
                );
            }
        }

        return pathLengths[n][m];
    }

    public int
    ManhattanTourist(int n, int m, List<List<Integer>> down, List<List<Integer>> right) {
        return ManhattanTouristMachinery(
                n, m, down, right
        );
    }

    public int ManhattanTourist(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        List<Integer> dimensions = UTIL.parseIntArray(strDataset.getFirst());
        int i;
        List<List<Integer>> down = new ArrayList<>(),
                right = new ArrayList<>();
        for (i = 1; !strDataset.get(i).equals("-"); ++i) {
            down.add(UTIL.parseIntArray(strDataset.get(i)));
        }
        for (int j = i + 1; j < strDataset.size(); ++j) {
            right.add(UTIL.parseIntArray(strDataset.get(j)));
        }

        return ManhattanTouristMachinery(
                dimensions.getFirst(),
                dimensions.getLast(),
                down,
                right
        );
    }

    private void run() {
        Path path = Path.of(
                "C:\\Users\\sgnot\\Downloads\\rosalind_ba5b.txt"
        );
        int longestPath = ManhattanTourist(path);
        UTIL.writeToFile(List.of(longestPath));
    }

    public static void main(String[] args) {
        new BA5B().run();
    }
}
