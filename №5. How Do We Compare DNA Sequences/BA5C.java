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

    private static final StringBuilder lcs = new StringBuilder();

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
                    backtrack[i][j] = 'd';
                } else if (s[i][j] == s[i][j - 1]) {
                    backtrack[i][j] = 'i';
                } else if (s[i][j] == s[i - 1][j - 1] + 1 && equalChars) {
                    backtrack[i][j] = 'm';
                }
            }
        }

        return backtrack;
    }

    private static void outputLCS(char[][] backtrack, String v, int i, int j) {
        if (i == 0 || j == 0) {
            return;
        }
        if (backtrack[i][j] == 'd') {
            outputLCS(backtrack, v, i - 1, j);
        } else if (backtrack[i][j] == 'i') {
            outputLCS(backtrack, v, i, j - 1);
        } else {
            outputLCS(backtrack, v, i - 1, j - 1);
            lcs.append(v.charAt(i - 1));
        }
    }

    private static String findLongestCommonSubsequenceMachinery(String str1, String str2) {
        int i = str1.length(), j = str2.length();
        char[][] backtrack = LCSBacktrack(str1, str2);

        outputLCS(backtrack, str1, i, j);

        return lcs.toString();
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
