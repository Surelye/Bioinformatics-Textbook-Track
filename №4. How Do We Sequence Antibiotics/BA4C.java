// Generate the Theoretical Spectrum of a Cyclic Peptide
// -----------------------------------------------------
//
// The workhorse of peptide sequencing is the mass spectrometer, an expensive molecular scale that
// shatters molecules into pieces and then weighs the resulting fragments. The mass spectrometer
// measures the mass of a molecule in daltons (Da); 1 Da is approximately equal to the mass of a
// single nuclear particle (i.e., a proton or neutron).
//
// We will approximate the mass of a molecule by simply adding the number of protons and neutrons
// found in the molecule’s constituent atoms, which yields the molecule’s integer mass. For
// example, the amino acid "Gly", which has chemical formula C2H3ON, has an integer mass of 57,
// since 2·12 + 3·1 + 1·16 + 1·14 = 57. Yet 1 Da is not exactly equal to the mass of a
// proton/neutron, and we may need to account for different naturally occurring isotopes of each
// atom when weighing a molecule. As a result, amino acids typically have non-integer masses
// (e.g., "Gly" has total mass equal to approximately 57.02 Da); for simplicity, however, we will
// work with the integer mass table.
//
// The theoretical spectrum of a cyclic peptide Peptide, denoted Cyclospectrum(Peptide), is the
// collection of all of the masses of its subpeptides, in addition to the mass 0 and the mass of the
// entire peptide. We will assume that the theoretical spectrum can contain duplicate elements, as
// is the case for "NQEL", where "NQ" and "EL" have the same mass.
//
// ---------------------------------------
// Generating Theoretical Spectrum Problem
//
// Generate the theoretical spectrum of a cyclic peptide.
//
// Given: An amino acid string Peptide.
//
// Return: Cyclospectrum(Peptide).
//
// Sample Dataset
// --------------
// LEQN
// --------------
//
// Sample Output
// -------------
// 0 113 114 128 129 227 242 242 257 355 356 370 371 484
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA4C {

    private static List<Integer> getCyclicSpectrumMachinery(String cyclicPeptide) {
        int peptideLength = cyclicPeptide.length();
        String peptideExtended = cyclicPeptide + cyclicPeptide.substring(0, peptideLength - 2);
        int extendedLength = 2 * peptideLength - 2;
        char aminoAcid;
        int[] prefixMass = new int[extendedLength + 1];
        prefixMass[0] = 0;
        List<Integer> cyclospectrum = BA4J.getLinearSpectrum(cyclicPeptide);

        for (int i = 1; i <= extendedLength; ++i) {
            aminoAcid = peptideExtended.charAt(i - 1);
            prefixMass[i] = prefixMass[i - 1] + BA4UTIL.getAminoAcidIntegerMass(aminoAcid);
        }
        for (int i = 1; i < peptideLength - 1; ++i) {
            for (int j = 0; j < i; ++j) {
                cyclospectrum.add(prefixMass[peptideLength + j + 1] -
                        prefixMass[peptideLength - i + j]);
            }
        }
        cyclospectrum.sort(Integer::compare);
        UTIL.writeToFile(cyclospectrum);

        return cyclospectrum;
    }

    public static List<Integer> getCyclicSpectrum(Path path) {
        return getCyclicSpectrumMachinery(UTIL.readDataset(path).getFirst());
    }

    public static List<Integer> getCyclicSpectrum(String peptide) {
        return getCyclicSpectrumMachinery(peptide);
    }
}
