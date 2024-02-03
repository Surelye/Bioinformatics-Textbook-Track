import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BA2UTIL {

    public static int symbolToIndex(char symbol) {
        return
                switch (symbol) {
                    case 'A' -> 0;
                    case 'C' -> 1;
                    case 'G' -> 2;
                    case 'T' -> 3;
                    default -> throw new RuntimeException("Incorrect nucleotide: %c"
                            .formatted(symbol));
                };
    }

    public static char indexToSymbol(int index) {
        return
                switch (index) {
                    case 0 -> 'A';
                    case 1 -> 'C';
                    case 2 -> 'G';
                    case 3 -> 'T';
                    default -> throw new RuntimeException("Incorrect index: %d".formatted(index));
                };
    }

    public static int[][] formCount(List<String> motifs) {
        int motifLength = motifs.getFirst().length(), index;
        int[][] count = new int[4][motifLength];

        for (String motif : motifs) {
            for (int i = 0; i < motifLength; ++i) {
                index = symbolToIndex(motif.charAt(i));
                count[index][i] += 1;
            }
        }

        return count;
    }

    public static int[][] formCountWithPseudocounts(List<String> motifs) {
        int motifLength = motifs.getFirst().length(), index;
        int[][] countWithPseudocounts = new int[4][motifLength];
        for (int i = 0; i < 4; ++i) {
            Arrays.fill(countWithPseudocounts[i], 1);
        }

        for (String motif : motifs) {
            for (int i = 0; i < motifLength; ++i) {
                index = symbolToIndex(motif.charAt(i));
                countWithPseudocounts[index][i] += 1;
            }
        }

        return countWithPseudocounts;
    }

    public static double[][] formProfile(List<String> motifs) {
        int motifLength = motifs.getFirst().length(),
                motifsSize = motifs.size();
        int i = 0, j = 0;
        double[][] profile = new double[4][motifLength];

        for (int[] countRow : formCount(motifs)) {
            for (double count : countRow) {
                profile[i][j++] = count / motifsSize;
            }
            j = 0;
            ++i;
        }

        return profile;
    }

    public static double[][] formProfileWithPseudocounts(List<String> motifs) {
        int motifLength = motifs.getFirst().length(),
                motifsSize = motifs.size() + 4;
        int i = 0, j = 0;
        double[][] profileWithPseudocounts = new double[4][motifLength];

        for (int[] countWithPseudocountsRow : formCountWithPseudocounts(motifs)) {
            for (double countWithPseudocounts : countWithPseudocountsRow) {
                profileWithPseudocounts[i][j++] = countWithPseudocounts / motifsSize;
            }
            j = 0;
            ++i;
        }

        return profileWithPseudocounts;
    }

    public static String formConsensus(List<String> motifs) {
        int motifLength = motifs.getFirst().length();
        int freqNucleotideIndex = 0, maxFreq;
        int[][] count = formCount(motifs);
        StringBuilder consensus = new StringBuilder(motifLength);

        for (int i = 0; i < motifLength; ++i) {
            maxFreq = Integer.MIN_VALUE;
            for (int j = 0; j < 4; ++j) {
                if (count[j][i] > maxFreq) {
                    maxFreq = count[j][i];
                    freqNucleotideIndex = j;
                }
            }
            consensus.append(indexToSymbol(freqNucleotideIndex));
        }

        return consensus.toString();
    }

    public static int score(List<String> motifs) {
        int score = 0;
        String consensus = formConsensus(motifs);

        for (String motif : motifs) {
            score += BA1G.HammingDistance(consensus, motif);
        }

        return score;
    }

    public static List<String> formMotifs(double[][] profile, List<String> DNA) {
        int k = profile[0].length;

        return DNA
                .stream()
                .map(motif -> BA2C.profileMostProbableKMer(motif, k, profile))
                .toList();
    }

    public static List<String> selectRandomKMers(List<String> DNA, int motifLength, int k) {
        List<String> randomKMers = new ArrayList<>();
        Random random = new Random();
        int index;

        for (String motif : DNA) {
            index = random.nextInt(motifLength - k + 1);
            randomKMers.add(motif.substring(index, index + k));
        }

        return randomKMers;
    }
}
