// Generate the Convolution of a Spectrum
// --------------------------------------
//
// We define the convolution of a cyclic spectrum by taking all positive differences of masses in
// the spectrum.
//
// As predicted, some of the values appear more frequently than others. For example, 113 (the mass
// of "L") appears eight times in the convolution of the theoretical spectrum of "NQEL"; we say
// that 113 has multiplicity 8. Six of the eight occurrences of 113 correspond to subpeptide pairs
// differing in an "L": "L" and ""; "LN" and "N"; "EL" and "E"; "LNQ" and "NQ"; "QEL" and "QE";
// "NQEL" and "NQE".
//
// ----------------------------
// Spectral Convolution Problem
//
// Compute the convolution of a spectrum.
//
// Given: A collection of integers Spectrum.
//
// Return: The list of elements in the convolution of Spectrum in decreasing order of their
// multiplicities. If an element has multiplicity k, it should appear exactly k times.
//
// Sample Dataset
// --------------
// 0 137 186 323
// --------------
//
// Sample Output
// -------------
// 137 137 186 186 323 49
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA4H {

    private static List<Integer> generateSpectrumConvolutionMachinery(List<Integer> spectrum) {
        spectrum.sort(Integer::compare);
        int spectrumSize = spectrum.size(), difference;
        Map<Integer, Integer> massToEntries = new HashMap<>();
        List<Integer> convolution = new ArrayList<>();

        for (int i = spectrumSize - 1; i > 0; --i) {
            for (int j = i - 1; j > -1; --j) {
                difference = spectrum.get(i) - spectrum.get(j);
                if (difference > 0) {
                    if (massToEntries.containsKey(difference)) {
                        massToEntries.put(difference, massToEntries.get(difference) + 1);
                    } else {
                        massToEntries.put(difference, 1);
                    }
                }
            }
        }
        List<Pair<Integer, Integer>> massToEntriesPairs = new ArrayList<>(massToEntries
                .keySet()
                .stream()
                .map(mass -> new Pair<>(mass, massToEntries.get(mass)))
                .toList());
        massToEntriesPairs.sort(Comparator.comparing(Pair::getSecond));
        Pair<Integer, Integer> massToEntryPair;
        int mass, entries;

        for (int i = massToEntriesPairs.size() - 1; i > -1; --i) {
            massToEntryPair = massToEntriesPairs.get(i);
            mass = massToEntryPair.getFirst();
            entries = massToEntryPair.getSecond();
            for (int j = 0; j < entries; ++j) {
                convolution.add(mass);
            }
        }

        return convolution;
    }

    public static List<Integer> generateSpectrumConvolution(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return generateSpectrumConvolutionMachinery(
                new ArrayList<>(UTIL.parseIntArray(strDataset.getFirst()))
        );
    }

    public static List<Integer> generateSpectrumConvolution(List<Integer> spectrum) {
        return generateSpectrumConvolutionMachinery(spectrum);
    }
}
