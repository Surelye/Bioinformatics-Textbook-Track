// Find a Longest Common Subsequence of Two Strings
// ------------------------------------------------
//
// Longest Common Subsequence Problem
//
// Given: Two strings.
//
// Return: A longest common subsequence of these strings.
//
// Sample Dataset
// --------------
// AACCTTGG
// ACACTGTGA
// --------------
//
// Sample Output
// -------------
// AACTGG
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA5C {

    private static char[][] LCSBacktrack(String v, String w) {
        int vLen = v.length(), wLen = w.length();
        int[][] s = new int[vLen + 1][wLen + 1];
        char[][] backtrack = new char[vLen + 1][wLen + 1];

        for (int i = 1; i <= vLen; ++i) {
            for (int j = 1; j <= wLen; ++j) {
                boolean equalChars = v.charAt(i - 1) == w.charAt(j - 1);
                s[i][j] = Math.max(
                        s[i - 1][j],
                        Math.max(
                                s[i][j - 1],
                                s[i - 1][j - 1] + (equalChars ? 1 : 0)
                        )
                );
                if (s[i][j] == s[i - 1][j]) {
                    backtrack[i][j] = 'b';
                } else if (s[i][j] == s[i][j - 1]) {
                    backtrack[i][j] = 'r';
                } else if (s[i][j] == s[i - 1][j - 1] + 1 && equalChars) {
                    backtrack[i][j] = 'd';
                }
            }
        }

        return backtrack;
    }

    private static String findLongestCommonSubsequenceMachinery(String str1, String str2) {
        StringBuilder lcs = new StringBuilder();
        int i = str1.length(), j = str2.length();
        char[][] backtrack = LCSBacktrack(str1, str2);

        while (i != 0 && j != 0) {
            if (backtrack[i][j] == 'b') {
                i -= 1;
            } else if (backtrack[i][j] == 'r') {
                j -= 1;
            } else {
                i -= 1;
                j -= 1;
                try {
                    lcs.append(str1.charAt(i));
                } catch (StringIndexOutOfBoundsException e) {
                    break;
                }
            }
        }

        return lcs.reverse().toString();
    }

    public static String findLongestCommonSubsequence(String str1, String str2) {
        return findLongestCommonSubsequenceMachinery(str1, str2);
    }

    public static String findLongestCommonSubsequence(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findLongestCommonSubsequenceMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        String lcs = findLongestCommonSubsequence(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5c.txt"
                )
        );
        UTIL.writeToFile(List.of(lcs));
    }

    public static void main(String[] args) {
        new BA5C().run();
    }
}
