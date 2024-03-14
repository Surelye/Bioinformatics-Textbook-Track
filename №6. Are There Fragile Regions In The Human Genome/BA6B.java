// Compute the Number of Breakpoints in a Permutation
// --------------------------------------------------
//
// Number of Breakpoints Problem
//
// Find the number of breakpoints in a permutation.
//
// Given: A signed permutation P.
//
// Return: The number of breakpoints in P.
//
// Sample Dataset
// --------------
// (+3 +4 +5 -12 -8 -7 -6 +1 +2 +10 +9 -11 +13 +14)
// --------------
//
// Sample Output
// -------------
// 8
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA6B {

    public static int computeNumberOfBreakpoints(List<Integer> permutation) {
        int permSize = permutation.size(), cur, next = permutation.getFirst();
        int numberOfBreakpoints = 0;
        numberOfBreakpoints += (permutation.getFirst() == 1) ? 0 : 1;
        numberOfBreakpoints += (permutation.getLast() == permSize) ? 0 : 1;

        for (int i = 0; i < permSize - 1; ++i) {
            cur = next;
            next = permutation.get(i + 1);
            if (next - cur != 1) {
                ++numberOfBreakpoints;
            }
        }

        return numberOfBreakpoints;
    }

    public static int computeNumberOfBreakpoints(Path path) {
        List<Integer> permutation = BA6UTIL.parsePermutation(path);

        return computeNumberOfBreakpoints(permutation);
    }

    private void run() {
        int numberOfBreakpoints = computeNumberOfBreakpoints(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6b.txt"
                )
        );
        BA6UTIL.writeToFile("ba6b_out.txt", List.of(numberOfBreakpoints));
    }

    public static void main(String[] args) {
        new BA6B().run();
    }
}
