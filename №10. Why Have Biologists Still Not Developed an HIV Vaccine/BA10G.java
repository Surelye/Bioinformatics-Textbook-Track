// Perform a Multiple Sequence Alignment with a Profile HMM
// --------------------------------------------------------
//
// Sequence Alignment with Profile HMM Problem
//
// Given: A string Text, a multiple alignment `Alignment`, a threshold θ, and a pseudocount σ.
//
// Return: An optimal hidden path emitting Text in HMM(Alignment,θ,σ).
//
// Sample Dataset
// AEFDFDC
// --------
// 0.4 0.01
// --------
// A   B   C   D   E   F
// --------
// ACDEFACADF
// AFDA---CCF
// A--EFD-FDC
// ACAEF--A-C
// ADDEFAAADF
// --------------
//
// Sample Output
// -------------
// M1 D2 D3 M4 M5 I5 M6 M7 M8
// -------------

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BA10G {

    public static List<auxil.Path> HmmMultipleSequenceAlignmentMachinery(
            String text, List<String> alignment, Map<Character, Integer> alphabet,
            double threshold, double pseudocount
    ) {
        Map.Entry<List<List<Double>>, List<List<Double>>> probabilities = BA10F.constructProfileHMMWithPseudocounts(
                threshold, pseudocount, alphabet, alignment
        );
        List<List<Double>> transitionProbabilities = probabilities.getKey();
        List<List<Double>> emissionProbabilities = probabilities.getValue();

        return List.of();
    }

    public static List<auxil.Path> HmmMultipleSequenceAlignment(
            String text, List<String> alignment, Map<Character, Integer> alphabet,
            double threshold, double pseudocount
    ) {
        return HmmMultipleSequenceAlignmentMachinery(text, alignment, alphabet, threshold, pseudocount);
    }

    public static List<auxil.Path> HmmMultipleSequenceAlignment(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String text = strDataset.getFirst();
        String[] doubles = strDataset.get(2).split("\\s+");
        double threshold = Double.parseDouble(doubles[0]);
        double pseudocount = Double.parseDouble(doubles[1]);
        Map<Character, Integer> alphabet = BA10UTIL.parseCharacterArrayToMap(strDataset.get(4), "\\s+");
        List<String> alignment = strDataset.subList(6, strDataset.size());

        return HmmMultipleSequenceAlignmentMachinery(text, alignment, alphabet, threshold, pseudocount);
    }

    private void run() {
        List<auxil.Path> hiddenPath = HmmMultipleSequenceAlignment(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10g.txt"
                )
        );
    }

    public static void main(String[] args) {
        new BA10G().run();
    }
}
