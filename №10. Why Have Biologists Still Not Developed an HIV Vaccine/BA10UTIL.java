import auxil.HMM;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class BA10UTIL {

    public static final String separator = "--------";

    public static List<List<Double>> initDoubleMatrix(int fDim, int sDim) {
        List<List<Double>> dMat = new ArrayList<>(fDim);
        for (int i = 0; i != fDim; ++i) {
            dMat.add(new ArrayList<>(Collections.nCopies(sDim, 0.)));
        }
        return dMat;
    }

    public static List<List<Double>> initDoubleMatrix(int dim) {
        return initDoubleMatrix(dim, dim);
    }

    public static List<Double> parseDoubleArray(String strDoubleArr, String regex) {
        return Arrays.stream(strDoubleArr.split(regex))
                .map(Double::parseDouble)
                .toList();
    }

    public static Map<Character, Map<Character, Double>> parseTransitionMatrix(
            List<String> strMatrix, List<Character> states
    ) {
        int nStates = states.size();
        char curState;
        Map<Character, Map<Character, Double>> transition = new HashMap<>(nStates);
        List<Double> row;

        for (int i = 0; i != nStates; ++i) {
            curState = states.get(i);
            transition.put(curState, new HashMap<>(nStates));
            row = parseDoubleArray(
                    strMatrix.get(i).substring(1).strip(),
                    "\\s+"
            );
            for (int j = 0; j != nStates; ++j) {
                transition.get(curState).put(states.get(j), row.get(j));
            }
        }

        return transition;
    }

    public static Map<Character, Map<Character, Double>> parseEmissionMatrix(
            List<String> strMatrix, List<Character> states, List<Character> alphabet
    ) {
        int nStates = states.size(), alphabetSize = alphabet.size();
        char curState;
        Map<Character, Map<Character, Double>> emission = new HashMap<>(nStates);
        List<Double> row;

        for (int i = 0; i != nStates; ++i) {
            curState = states.get(i);
            emission.put(curState, new HashMap<>(alphabetSize));
            row = parseDoubleArray(
                    strMatrix.get(i).substring(1).strip(),
                    "\\s+"
            );
            for (int j = 0; j != alphabetSize; ++j) {
                emission.get(curState).put(alphabet.get(j), row.get(j));
            }
        }

        return emission;
    }

    public static List<Character> parseCharacterArray(String strCharArr, String regex) {
        return Arrays.stream(strCharArr.split(regex))
                .map(str -> str.charAt(0))
                .toList();
    }

    public static Map<Character, Integer> parseCharacterArrayToMap(String strCharArr, String regex) {
        List<Character> alphabet = parseCharacterArray(strCharArr, regex);
        Map<Character, Integer> charToIndex = new HashMap<>(alphabet.size());
        IntStream.range(0, alphabet.size())
                .forEach(i -> charToIndex.put(alphabet.get(i), i));
        return charToIndex;
    }

    public static HMM parseHMM(List<String> strHMM) {
        List<Character> alphabet = parseCharacterArray(strHMM.get(1), "\\s+");
        List<Character> states = parseCharacterArray(strHMM.get(3), "\\s+");
        int border = strHMM.lastIndexOf(separator);
        Map<Character, Map<Character, Double>> transition = parseTransitionMatrix(
                strHMM.subList(6, border), states
        );
        Map<Character, Map<Character, Double>> emission = parseEmissionMatrix(
                strHMM.subList(border + 2, strHMM.size()), states, alphabet
        );

        return new HMM(alphabet, states, transition, emission);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static<T> void writeToFile(String filename, T e) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write("%s\n".formatted(e));
        } catch (IOException ioe) {
            System.out.println("Failed to write to file");
            ioe.printStackTrace();
        }
    }
}
