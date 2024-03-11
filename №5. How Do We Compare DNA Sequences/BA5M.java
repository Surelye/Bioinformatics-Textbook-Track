// Find a Highest-Scoring Multiple Sequence Alignment
// --------------------------------------------------
//
// Multiple Longest Common Subsequence Problem
//
// Find a longest common subsequence of multiple strings.
//
// Given: Three DNA strings.
//
// Return: The maximum score of a multiple alignment of these three strings, followed by a multiple
// alignment of the three strings achieving this maximum. Use a scoring function in which the score
// of an alignment column is 1 if all three symbols are identical and 0 otherwise. (If more than one
// multiple alignment achieve the maximum, you may return any one.)
//
// Sample Dataset
// --------------
// ATATCCG
// TCCGA
// ATGTACTG
// --------------
//
// Sample Output
// -------------
// 3
// ATATCC-G-
// ---TCC-GA
// ATGTACTG-
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BA5M {

    private static int maxMultipleAlignmentScore;

    private static int[][][] LCSBacktrack(String v, String w, String u) {
        int vLen = v.length(), wLen = w.length(), uLen = u.length();
        int vChar, wChar, uChar;
        int[][][] s = new int[vLen + 1][wLen + 1][uLen + 1];

        for (int i = 1; i <= vLen; ++i) {
            vChar = v.charAt(i - 1);
            for (int j = 1; j <= wLen; ++j) {
                wChar = w.charAt(j - 1);
                for (int k = 1; k <= uLen; ++k) {
                    uChar = u.charAt(k - 1);
                    s[i][j][k] = Stream.of(
                            s[i - 1][j][k], s[i][j - 1][k],
                            s[i][j][k - 1], s[i - 1][j - 1][k],
                            s[i - 1][j][k - 1], s[i][j - 1][k - 1],
                            s[i - 1][j - 1][k - 1] + ((vChar == wChar && vChar == uChar) ? 1 : 0)
                    ).max(Integer::compare).orElseThrow();
                }
            }
        }
        maxMultipleAlignmentScore = s[vLen][wLen][uLen];

        return s;
    }

    private static void pairedAlignment(
            int[][] bt, String v, String w, StringBuilder vAligned,
            StringBuilder wAligned, int i, int j
    ) {
        char vCh, wCh;

        while (true) {
            vCh = v.charAt(i - 1);
            wCh = w.charAt(j - 1);
            if (bt[i][j] == bt[i - 1][j - 1]) {
                vAligned.append(vCh);
                wAligned.append(wCh);
                --i;
                --j;
            } else if (bt[i][j] == bt[i - 1][j]) {
                vAligned.append(vCh);
                wAligned.append('-');
                --i;
            } else if (bt[i][j] == bt[i][j - 1]) {
                vAligned.append('-');
                wAligned.append(wCh);
                --j;
            }

            if (i == 0 || j == 0) {
                if (i == 0) {
                    vAligned.append("-".repeat(j));
                    for (int p = j - 1; p > -1; --p) {
                        wAligned.append(w.charAt(p));
                    }
                } else {
                    wAligned.append("-".repeat(i));
                    for (int p = i - 1; p > -1; --p) {
                        vAligned.append(v.charAt(p));
                    }
                }
                break;
            }
        }
    }

    private static void
    appendSymbols(StringBuilder vAl, StringBuilder wAl, StringBuilder uAl, String u, int i) {
        String gaps = "-".repeat(i);
        vAl.append(gaps);
        wAl.append(gaps);
        for (int j = i - 1; j > -1; --j) {
            uAl.append(u.charAt(j));
        }
    }

    private static Map.Entry<Integer, String[]>
    multipleAlignmentMachinery(String v, String w, String u) {
        int i = v.length(), j = w.length(), k = u.length();
        char vCh, wCh, uCh;
        StringBuilder vAligned = new StringBuilder(i), wAligned = new StringBuilder(j),
                uAligned = new StringBuilder(k);
        int[][][] bt = LCSBacktrack(v, w, u);
        int[][] btSlice;

        while (true) {
            vCh = v.charAt(i - 1); wCh = w.charAt(j - 1); uCh = u.charAt(k - 1);
            if (bt[i][j][k] == (bt[i - 1][j - 1][k - 1] + ((vCh == wCh && vCh == uCh) ? 1 : 0))) {
                vAligned.append(vCh);
                wAligned.append(wCh);
                uAligned.append(uCh);
                --i; --j; --k;
            } else if (bt[i][j][k] == bt[i - 1][j - 1][k]) {
                vAligned.append(vCh);
                wAligned.append(wCh);
                uAligned.append('-');
                --i; --j;
            } else if (bt[i][j][k] == bt[i - 1][j][k - 1]) {
                vAligned.append(vCh);
                wAligned.append('-');
                uAligned.append(uCh);
                --i; --k;
            } else if (bt[i][j][k] == bt[i][j - 1][k - 1]) {
                vAligned.append('-');
                wAligned.append(wCh);
                uAligned.append(uCh);
                --j; --k;
            } else if (bt[i][j][k] == bt[i - 1][j][k]) {
                vAligned.append(vCh);
                wAligned.append('-');
                uAligned.append('-');
                --i;
            } else if (bt[i][j][k] == bt[i][j - 1][k]) {
                vAligned.append('-');
                wAligned.append(wCh);
                uAligned.append('-');
                --j;
            } else if (bt[i][j][k] == bt[i][j][k - 1]) {
                vAligned.append('-');
                wAligned.append('-');
                uAligned.append(uCh);
                --k;
            }

            if (i == 0 || j == 0 || k == 0) {
                if (i == 0) {
                    if (j == 0) {
                        appendSymbols(vAligned, wAligned, uAligned, u, k);
                        break;
                    } else if (k == 0) {
                        appendSymbols(vAligned, uAligned, wAligned, w, j);
                        break;
                    }
                    btSlice = new int[j + 1][k + 1];
                    for (int j1 = 0; j1 <= j; ++j1) {
                        System.arraycopy(bt[0][j1], 0, btSlice[j1], 0, k + 1);
                    }
                    pairedAlignment(btSlice, w, u, wAligned, uAligned, j, k);
                    vAligned.append("-".repeat(wAligned.length() - vAligned.length()));
                } else if (j == 0) {
                    if (k == 0) {
                        appendSymbols(wAligned, uAligned, vAligned, v, i);
                        break;
                    }
                    btSlice = new int[i + 1][k + 1];
                    for (int i1 = 0; i1 <= i; ++i1) {
                        System.arraycopy(bt[i1][0], 0, btSlice[i1], 0, k + 1);
                    }
                    pairedAlignment(btSlice, v, u, vAligned, uAligned, i, k);
                    wAligned.append("-".repeat(vAligned.length() - wAligned.length()));
                } else if (k == 0) {
                    btSlice = new int[i + 1][j + 1];
                    for (int i1 = 0; i1 <= i; ++i1) {
                        for (int j1 = 0; j1 <= j; ++j1) {
                            btSlice[i1][j1] = bt[i1][j1][0];
                        }
                    }
                    pairedAlignment(btSlice, v, w, vAligned, wAligned, i, j);
                    uAligned.append("-".repeat(vAligned.length() - uAligned.length()));
                }
                break;
            }
        }
        vAligned.reverse();
        wAligned.reverse();
        uAligned.reverse();

        return Map.entry(
                maxMultipleAlignmentScore,
                new String[]{vAligned.toString(), wAligned.toString(), uAligned.toString()}
        );
    }

    public static Map.Entry<Integer, String[]> multipleAlignment(String v, String w, String u) {
        return multipleAlignmentMachinery(v, w, u);
    }

    public static Map.Entry<Integer, String[]> multipleAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return multipleAlignmentMachinery(
                strDataset.getFirst(),
                strDataset.get(1),
                strDataset.getLast()
        );
    }

    private void run() {
        Map.Entry<Integer, String[]> scoreAndAlignment = multipleAlignment(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba5m.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba5m_out.txt")) {
            fileWriter.write("%d%n".formatted(scoreAndAlignment.getKey()));
            for (int i = 0; i < 3; ++i) {
                fileWriter.write("%s%n".formatted(scoreAndAlignment.getValue()[i]));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA5M().run();
    }
}
