// Translate an RNA String into an Amino Acid String
// -------------------------------------------------
//
// Much like replication, the chemical machinery underlying transcription and translation is
// fascinating, but from a computational perspective, both processes are straightforward.
// Transcription simply transforms a DNA string into an RNA string by replacing all occurrences of
// "T" with "U". The resulting strand of RNA is translated into an amino acid sequence via the
// genetic code; this process converts each 3-mer of RNA, called a codon, into one of 20 amino
// acids.
//
// Each of the 64 RNA codons encodes its own amino acid (some codons encode the same amino acid),
// with the exception of three stop codons that do not translate into amino acids and serve to halt
// translation. For example, the DNA string "TATACGAAA" transcribes into the RNA string "UAUACGAAA",
// which in turn translates into the amino acid string "Tyr-Thr-Lys".
//
// The following problem asks you to find the translation of an RNA string into an amino acid
// string.
//
// ---------------------------
// Protein Translation Problem
//
// Translate an RNA string into an amino acid string.
//
// Given: An RNA string Pattern.
//
// Return: The translation of Pattern into an amino acid string Peptide.
//
// Sample Dataset
// --------------
// AUGGCCAUGGCGCCCAGAACUGAGAUCAAUAGUACCCGUAUUAACGGGUGA
// --------------
//
// Sample Output
// -------------
// MAMAPRTEINSTRING
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA4A {

    private static String proteinTranslationMachinery(String pattern) {
        int patternLength = pattern.length();
        char aminoAcid;
        StringBuilder peptide = new StringBuilder();

        for (int i = 0; i < patternLength; i += 3) {
            String triplet = pattern.substring(i, i + 3);
            aminoAcid = BA4UTIL.RNACodonToAminoAcid(triplet);

            if (aminoAcid == '*') {
                break;
            } else {
                peptide.append(aminoAcid);
            }
        }
        UTIL.writeToFile(List.of(peptide.toString()));

        return peptide.toString();
    }

    public static String proteinTranslation(Path path) {
        return proteinTranslationMachinery(UTIL.readDataset(path).getFirst());
    }

    public static String proteinTranslation(String pattern) {
        return proteinTranslationMachinery(pattern);
    }
}
