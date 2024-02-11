// Implement ConvolutionCyclopeptideSequencing
// -------------------------------------------
//
// Implement ConvolutionCyclopeptideSequencing
//
// Given: An integer M, an integer N, and a collection of (possibly repeated) integers Spectrum.
//
// Return: A cyclic peptide LeaderPeptide with amino acids taken only from the top M elements (and
// ties) of the convolution of Spectrum that fall between 57 and 200, and where the size of
// Leaderboard is restricted to the top N (and ties).
//
// Sample Dataset
// --------------
// 20
// 60
// 57 57 71 99 129 137 170 186 194 208 228 265 285 299 307 323 356 364 394 422 493
// --------------
//
// Sample Output
// -------------
// 99-71-137-57-72-57
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA4I {

    private static List<Pair<Integer, Integer>>
    generateSpectrumConvolution(List<Integer> spectrum) {
        int spectrumSize = spectrum.size(), difference;
        Map<Integer, Integer> massToEntries = new HashMap<>();
        List<Pair<Integer, Integer>> massToEntriesPairs;

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
        massToEntriesPairs = new ArrayList<>(massToEntries
                .keySet()
                .stream()
                .map(mass -> new Pair<>(mass, massToEntries.get(mass)))
                .toList());
        massToEntriesPairs.sort(Comparator.comparing(Pair::getSecond));

        return massToEntriesPairs.reversed();
    }

    private static List<Integer> getAminoAcidAlphabet(List<Integer> spectrum, int M) {
        List<Pair<Integer, Integer>> convolution = generateSpectrumConvolution(spectrum);
        int  found = 0, mass, curNumEntries = 0, prevNumEntries;
        List<Integer> alphabet = new ArrayList<>();

        for (Pair<Integer, Integer> massToEntries : convolution) {
            mass = massToEntries.getFirst();
            if (56 < mass && mass < 201) {
                prevNumEntries = curNumEntries;
                curNumEntries = massToEntries.getSecond();
                if (found < M) {
                    alphabet.add(mass);
                    ++found;
                } else if (found == M) {
                    if (prevNumEntries == curNumEntries) {
                        alphabet.add(mass);
                    } else {
                        break;
                    }
                }
            }
        }

        return alphabet;
    }

    private static List<Integer>
    convolutionCyclopeptideSequencingMachinery(int M, int N, List<Integer> spectrum) {
        spectrum.sort(Integer::compareTo);
        List<Integer> aminoAcidAlphabet = getAminoAcidAlphabet(spectrum, M);
        List<Integer> leaderPeptide = BA4G.leaderboardCyclopeptideSequencing(
                N, spectrum, aminoAcidAlphabet);
        BA4UTIL.writePeptidesToFile(List.of(leaderPeptide));

        return leaderPeptide;
    }

    public static List<Integer> convolutionCyclopeptideSequencing(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return convolutionCyclopeptideSequencingMachinery(
                Integer.parseInt(strDataset.getFirst()),
                Integer.parseInt(strDataset.get(1)),
                new ArrayList<>(UTIL.parseIntArray(strDataset.getLast()))
        );
    }

    public static List<Integer>
    convolutionCyclopeptideSequencing(int M, int N, List<Integer> spectrum) {
        return convolutionCyclopeptideSequencingMachinery(M, N, spectrum);
    }
}
