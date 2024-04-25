// Generate the Last-to-First Mapping of a String
// ----------------------------------------------
//
// The Last-to-First array, denoted LastToFirst(i), answers the following question: given a symbol
// at position i in LastColumn, what is its position in FirstColumn?
//
// Last-to-First Mapping Problem
//
// Given: A string Transform and an integer i.
//
// Return: The position LastToFirst(i) in FirstColumn in the Burrows-Wheeler matrix if
// LastColumn = Transform.
//
// Sample Dataset
// --------------
// T$GACCA
// 3
// --------------
//
// Sample Output
// -------------
// 1
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA9K {

    private static int lastToFirstMachinery(String transform, int i) {
        return BA9UTIL.lastToFirst(transform).get(i);
    }

    public static int lastToFirst(String transform, int i) {
        return lastToFirstMachinery(transform, i);
    }

    public static int lastToFirst(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return lastToFirstMachinery(
                strDataset.getFirst(),
                Integer.parseInt(strDataset.getLast())
        );
    }

    private void run() {
        int ifc = lastToFirst(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9k.txt"
                )
        );
        BA9UTIL.writeToFile("ba9k_out.txt", ifc);
    }

    public static void main(String[] args) {
        new BA9K().run();
    }
}
