// Generate the Theoretical Spectrum of a Linear Peptide
// -----------------------------------------------------
//
// Given an amino acid string Peptide, we will begin by assuming that it represents a linear
// peptide. Our approach to generating its theoretical spectrum is based on the assumption that
// the mass of any subpeptide is equal to the difference between the masses of two prefixes of
// Peptide. We can compute an array PrefixMass storing the masses of each prefix of Peptide in
// increasing order, e.g., for Peptide = NQEL, PrefixMass = (0, 114, 242, 371, 484). Then, the
// mass of the subpeptide of Peptide beginning at position i + 1 and ending at position j can be
// computed as PrefixMass(j) − PrefixMass(i). For example, when Peptide = NQEL,
// Mass(QE) = PrefixMass(3) − PrefixMass(1) = 371 − 114 = 257.
//
// The pseudocode shown on the next step implements this idea. It also represents the alphabet of
// 20 amino acids and their integer masses as a pair of 20-element arrays AminoAcid and
// AminoAcidMass, corresponding to the top and bottom rows of the following integer mass table,
// respectively.
//
// -------------------------------------------------
// LinearSpectrum(Peptide, AminoAcid, AminoAcidMass)
//     PrefixMass(0) ← 0
//     for i ← 1 to |Peptide|
//         for j ← 1 to 20
//             if AminoAcid(j) =  i-th amino acid in Peptide
//                 PrefixMass(i) ← PrefixMass(i − 1) + AminoAcidMass(j)
//     LinearSpectrum ← a list consisting of the single integer 0
//     for i ← 0 to |Peptide| − 1
//         for j ← i + 1 to |Peptide|
//             add PrefixMass(j) − PrefixMass(i) to LinearSpectrum
//     return sorted list LinearSpectrum
// -------------------------------------------------
//
// Linear Spectrum Problem
//
// Generate the ideal linear spectrum of a peptide.
//
// Given: An amino acid string Peptide.
//
// Return: The linear spectrum of Peptide.
//
// Sample Dataset
// --------------
// NQEL
// --------------
//
// Sample Output
// -------------
// 0 113 114 128 129 242 242 257 370 371 484
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA4J {

    private static List<Integer> getLinearSpectrumMachinery(String peptide) {
        int peptideLength = peptide.length();
        char aminoAcid;
        int[] prefixMass = new int[peptideLength + 1];
        prefixMass[0] = 0;
        List<Integer> linearSpectrum = new ArrayList<>(List.of(0));

        for (int i = 1; i <= peptideLength; ++i) {
            aminoAcid = peptide.charAt(i - 1);
            prefixMass[i] = prefixMass[i - 1] + BA4UTIL.getAminoAcidIntegerMass(aminoAcid);
        }

        for (int i = 0; i < peptideLength; ++i) {
            for (int j = i + 1; j <= peptideLength; ++j) {
                linearSpectrum.add(prefixMass[j] - prefixMass[i]);
            }
        }
        linearSpectrum.sort(Integer::compare);
        UTIL.writeToFile(linearSpectrum);

        return linearSpectrum;
    }

    public static List<Integer> getLinearSpectrum(Path path) {
        return getLinearSpectrumMachinery(UTIL.readDataset(path).getFirst());
    }

    public static List<Integer> getLinearSpectrum(String peptide) {
        return getLinearSpectrumMachinery(peptide);
    }
}
