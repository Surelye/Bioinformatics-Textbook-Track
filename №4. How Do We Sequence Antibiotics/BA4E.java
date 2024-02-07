// Find a Cyclic Peptide with Theoretical Spectrum Matching an Ideal Spectrum
// --------------------------------------------------------------------------
//
// In “Compute the Number of Peptides of Given Total Mass”, we first encountered the problem of
// reconstructing a cyclic peptide from its theoretical spectrum; this problem is called the
// Cyclopeptide Sequencing Problem and is given below. It is solved by the following algorithm.
//
// --------------------------------
// CYCLOPEPTIDESEQUENCING(Spectrum)
//     Peptides ← a set containing only the empty peptide
//     while Peptides is nonempty
//         Peptides ← Expand(Peptides)
//         for each peptide Peptide in Peptides
//             if Mass(Peptide) = ParentMass(Spectrum)
//                 if Cyclospectrum(Peptide) = Spectrum
//                     output Peptide
//                 remove Peptide from Peptides
//             else if Peptide is not consistent with Spectrum
//                 remove Peptide from Peptides
// -------------------------------
//
// Cyclopeptide Sequencing Problem
//
// Given an ideal experimental spectrum, find a cyclic peptide whose theoretical spectrum matches
// the experimental spectrum.
//
// Given: A collection of (possibly repeated) integers Spectrum corresponding to an ideal
// experimental spectrum.
//
// Return: Every amino acid string Peptide such that Cyclospectrum(Peptide) = Spectrum (if such a
// string exists).
//
// Sample Dataset
// --------------
// 0 113 128 186 241 299 314 427
// --------------
//
// Sample Output
// -------------
// 186-128-113 186-113-128 128-186-113 128-113-186 113-186-128 113-128-186
// -------------

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA4E {

    private static List<String> expand(List<String> peptides) {
        List<String> expandedPeptides = new ArrayList<>();

        for (String peptide : peptides) {
            for (char aminoAcid : BA4UTIL.aminoAcidsOfUniqueIntegerMass) {
                expandedPeptides.add(peptide + aminoAcid);
            }
        }

        return expandedPeptides;
    }

    private static boolean
    isPeptideConsistentWithSpectrum(String peptide, List<Integer> spectrum) {
        List<Integer> peptideSpectrum = BA4J.getLinearSpectrum(peptide);
        List<Integer> spectrumCopy = new ArrayList<>(spectrum);

        for (Integer subpeptideMass : peptideSpectrum) {
            if (!spectrumCopy.remove(subpeptideMass)) {
                return false;
            }
        }

        return true;
    }

    private static List<List<Integer>> cyclopeptideSequencingMachinery(List<Integer> spectrum) {
        int parentMass = spectrum.getLast();
        List<String> peptides = new ArrayList<>(List.of(""));
        List<String> peptidesToRemove = new ArrayList<>();
        List<String> properPeptides = new ArrayList<>();

        while (!peptides.isEmpty()) {
            peptides = expand(peptides);
            for (String peptide : peptides) {
                if (BA4UTIL.getPeptideMass(peptide) == parentMass) {
                    if (BA4C.getCyclicSpectrum(peptide).equals(spectrum)) {
                        properPeptides.add(peptide);
                    }
                    peptidesToRemove.add(peptide);
                } else if (!isPeptideConsistentWithSpectrum(peptide, spectrum)) {
                    peptidesToRemove.add(peptide);
                }
            }
            for (String peptideToRemove : peptidesToRemove) {
                peptides.remove(peptideToRemove);
            }
            peptidesToRemove = new ArrayList<>();
        }
        List<List<Integer>> peptidesAsAminoAcidsMasses = properPeptides
                .stream()
                .map(BA4UTIL::peptideToMassList)
                .toList();
        BA4UTIL.writePeptidesToFile(peptidesAsAminoAcidsMasses);

        return peptidesAsAminoAcidsMasses;
    }

    public static List<List<Integer>> cyclopeptideSequencing(Path path) {
        return cyclopeptideSequencingMachinery(
                UTIL.parseIntArray(UTIL.readDataset(path).getFirst())
        );
    }

    public static List<List<Integer>> cyclopeptideSequencing(List<Integer> spectrum) {
        return cyclopeptideSequencingMachinery(spectrum);
    }
}
