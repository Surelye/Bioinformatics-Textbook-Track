// Find Substrings of a Genome Encoding a Given Amino Acid String
// --------------------------------------------------------------
//
// There are three different ways to divide a DNA string into codons for translation, one starting
// at each of the first three starting positions of the string. These different ways of dividing a
// DNA string into codons are called reading frames. Since DNA is double-stranded, a genome has six
// reading frames (three on each strand), as shown in Figure 1.
//
// We say that a DNA string Pattern encodes an amino acid string Peptide if the RNA string
// transcribed from either Pattern or its reverse complement Pattern translates into Peptide.
//
// ------------------------
// Peptide Encoding Problem
//
// Find substrings of a genome encoding a given amino acid sequence.
//
// Given: A DNA string Text and an amino acid string Peptide.
//
// Return: All substrings of Text encoding Peptide (if any such substrings exist).
//
// Sample Dataset
// --------------
// ATGGCCATGGCCCCCAGAACTGAGATCAATAGTACCCGTATTAACGGGTGA
// MA
// --------------
//
// Sample Output
// -------------
// ATGGCC
// GGCCAT
// ATGGCC
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA4B {

    private static List<String> peptideEncodingMachinery(String text, String peptide) {
        int textLength = text.length(), targetEncodingStringLength = 3 * peptide.length();
        String DNASubstring, reverseComplement;
        List<String> encodingSubstrings = new ArrayList<>();

        for (int i = 0; i < textLength - targetEncodingStringLength + 1; ++i) {
            DNASubstring = text.substring(i, i + targetEncodingStringLength);
            reverseComplement = BA1C.reverseComplement(DNASubstring);

            if (BA4UTIL.translate(DNASubstring).equals(peptide) ||
                    BA4UTIL.translate(reverseComplement).equals(peptide)) {
                encodingSubstrings.add(DNASubstring);
            }
        }
        UTIL.writeToFileWithNewlines(encodingSubstrings);

        return encodingSubstrings;
    }

    public static List<String> peptideEncoding(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return peptideEncodingMachinery(strDataset.getFirst(), strDataset.getLast());
    }

    public static List<String> peptideEncoding(String text, String peptide) {
        return peptideEncodingMachinery(text, peptide);
    }
}
