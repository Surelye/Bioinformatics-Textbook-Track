// Find All Shared k-mers of a Pair of Strings
// -------------------------------------------
//
// We say that a k-mer is shared by two genomes if either the k-mer or its reverse complement
// appears in each genome. In the example there are four pairs of 3-mers that are shared by
// "AAACTCATC" and "TTTCAAATC".
//
// A shared k-mer can be represented by an ordered pair (x, y), where x is the starting position of
// the k-mer in the first genome and y is the starting position of the k-mer in the second genome.
// For the genomes "AAACTCATC" and "TTTCAAATC", these shared k-mers are (0,4), (0,0), (4,2), and
// (6,6).
//
// Shared k-mers Problem
//
// Given two strings, find all their shared k-mers.
//
// Given: An integer k and two strings.
//
// Return: All k-mers shared by these strings, in the form of ordered pairs (x, y) corresponding to
// starting positions of these k-mers in the respective strings.
//
// Sample Dataset
// --------------
// 3
// AAACTCATC
// TTTCAAATC
// --------------
//
// Sample Output
// -------------
// (0, 4)
// (0, 0)
// (4, 2)
// (6, 6)
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BA6E {

    private static List<Map.Entry<Integer, Integer>>
    findSharedKMersMachinery(int k, String fGenome, String sGenome) {
        int fLen = fGenome.length(), sLen = sGenome.length();
        String fSubstring, fRevComp, sSubstring;
        Map<String, List<Integer>> strToIndices = new HashMap<>();
        List<Map.Entry<Integer, Integer>> points = new ArrayList<>();

        for (int x = 0; x < fLen - k + 1; ++x) {
            fSubstring = fGenome.substring(x, x + k);
            fRevComp = BA1C.reverseComplement(fSubstring);
            if (!strToIndices.containsKey(fSubstring)) {
                strToIndices.put(fSubstring, new ArrayList<>());
                for (int y = 0; y < sLen - k + 1; ++y) {
                    sSubstring = sGenome.substring(y, y + k);
                    if (fSubstring.equals(sSubstring) || fRevComp.equals(sSubstring)) {
                        strToIndices.get(fSubstring).add(y);
                    }
                }
            }
            for (int y : strToIndices.get(fSubstring)) {
                points.add(Map.entry(x, y));
            }
        }

        return points;
    }

    public static List<Map.Entry<Integer, Integer>>
    findSharedKMers(int k, String fGenome, String sGenome) {
        return findSharedKMersMachinery(k, fGenome, sGenome);
    }

    public static List<Map.Entry<Integer, Integer>> findSharedKMers(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return findSharedKMersMachinery(
                Integer.parseInt(strDataset.getFirst()),
                strDataset.get(1),
                strDataset.getLast()
        );
    }

    private void run() {
        List<Map.Entry<Integer, Integer>> sharedKMersPositions = findSharedKMers(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba6e.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba6e_out.txt")) {
            for (Map.Entry<Integer, Integer> point : sharedKMersPositions) {
                fileWriter.write("(%d, %d)\n".formatted(
                        point.getKey(),
                        point.getValue()
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA6E().run();
    }
}
