// Compute the Number of Peptides of Given Total Mass
// --------------------------------------------------
//
// In “Generate the Theoretical Spectrum of a Cyclic Peptide”, we generated the theoretical spectrum
// of a known cyclic peptide. Although this task is relatively easy, our aim in mass spectrometry is
// to solve the reverse problem: we must reconstruct an unknown peptide from its experimental
// spectrum. We will start by assuming that a biologist is lucky enough to generate an ideal
// experimental spectrum Spectrum, which is one coinciding with the peptide’s theoretical spectrum.
// Can we reconstruct a peptide whose theoretical spectrum is Spectrum?
//
// Denote the total mass of an amino acid string Peptide as Mass(Peptide). In mass spectrometry
// experiments, whereas the peptide that generated a spectrum is unknown, the peptide’s mass is
// typically known and is denoted ParentMass(Spectrum). Of course, given an ideal experimental
// spectrum, Mass(Peptide) is given by the largest mass in the spectrum.
//
// A brute force approach to reconstructing a peptide from its theoretical spectrum would generate
// all possible peptides whose mass is equal to ParentMass(Spectrum) and then check which of these
// peptides has theoretical spectra matching Spectrum. However, we should be concerned about the
// running time of such an approach: how many peptides are there having mass equal to
// ParentMass(Spectrum)?
//
// -----------------------------------------
// Counting Peptides with Given Mass Problem
//
// Compute the number of peptides of given total mass.
//
// Given: An integer m.
//
// Return: The number of linear peptides having integer mass m.
//
// Sample Dataset
// --------------
// 1024
// --------------
//
// Sample Output
// -------------
// 14712706211
// -------------

import java.math.BigInteger;
import java.nio.file.Path;

public class BA4D {

    private static long computeTheNumberOfPeptidesOfGivenTotalMassMachinery(int m) {
        long[] numLinearPeptides = new long[m + 1];
        for (int aminoAcidMass : BA4UTIL.uniqueAminoAcidMasses) {
            numLinearPeptides[aminoAcidMass] = 1;
        }

        for (int i = 114; i <= m; ++i) {
            for (int aminoAcidMass : BA4UTIL.uniqueAminoAcidMasses) {
                if (i >= aminoAcidMass) {
                    numLinearPeptides[i] += numLinearPeptides[i - aminoAcidMass];
                }
            }
        }

        return numLinearPeptides[m];
    }

    public static long computeTheNumberOfPeptidesOfGivenTotalMass(Path path) {
        return computeTheNumberOfPeptidesOfGivenTotalMassMachinery(
                Integer.parseInt(UTIL.readDataset(path).getFirst())
        );
    }

    public static long computeTheNumberOfPeptidesOfGivenTotalMass(int m) {
        return computeTheNumberOfPeptidesOfGivenTotalMassMachinery(m);
    }
}
