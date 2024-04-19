// Find the Longest Substring Shared by Two Strings
// ------------------------------------------------
//
// In “Find the Longest Repeat in a String”, we encountered the Longest Repeat Problem, which
// could be solved using a suffix tree.
//
// The second additional exercise that we will consider is below.
//
// Longest Shared Substring Problem
//
// Find the longest substring shared by two strings.
//
// Given: Strings Text1 and Text2.
//
// Return: The longest substring that occurs in both Text1 and Text2. (Multiple solutions may
// exist, in which case you may return any one.)
//
// Sample Dataset
// --------------
// TCGGTAGATTGCGCCCACTC
// AGGGGCTCGCAGTGTAAGAA
// --------------
//
// Sample Output
// -------------
// AGA
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BA9E {

    private static String findLongestSharedSubstringMachinery(String f, String s) {
        int fLen = f.length(), sLen = s.length();
        int[][] L = new int[fLen][sLen];
        int z = 0;
        String lcs = "";

        for (int i = 0; i != fLen; ++i) {
            for (int j = 0; j != sLen; ++j) {
                if (f.charAt(i) == s.charAt(j)) {
                    if (i == 0 || j == 0) {
                        L[i][j] = 1;
                    } else {
                        L[i][j] = L[i - 1][j - 1] + 1;
                    }
                    if (L[i][j] > z) {
                        z = L[i][j];
                        lcs = f.substring(i - z + 1, i + 1);
                    }
                } else {
                    L[i][j] = 0;
                }
            }
        }

        return lcs;
    }

    public static String findLongestSharedSubstring(String f, String s) {
        return findLongestSharedSubstringMachinery(f, s);
    }

    public static String findLongestSharedSubstring(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return findLongestSharedSubstringMachinery(
                strDataset.getFirst(),
                strDataset.getLast()
        );
    }

    private void run() {
        String longestSharedSubstring = findLongestSharedSubstring(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9e.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9e_out.txt")) {
            fileWriter.write("%s\n".formatted(
                    longestSharedSubstring
            ));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9E().run();
    }
}
