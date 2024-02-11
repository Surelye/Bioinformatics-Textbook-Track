import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BA4UTIL {

    public static List<Integer> uniqueAminoAcidMasses = new ArrayList<>(List.of(
            57, 71, 87, 97, 99, 101,
            103, 113, 114, 115, 128, 129,
            131, 137, 147, 156, 163, 186
    ));

    public static Set<List<Integer>>
    expand(Set<List<Integer>> peptides, List<Integer> aminoAcidMasses) {
        Set<List<Integer>> expandedPeptides = new HashSet<>();

        for (List<Integer> peptide : peptides) {
            for (int aminoAcidMass : aminoAcidMasses) {
                List<Integer> currentPeptide = new ArrayList<>(peptide);
                currentPeptide.add(aminoAcidMass);
                expandedPeptides.add(currentPeptide);
            }
        }

        return expandedPeptides;
    }

    public static int getPeptideMass(List<Integer> peptide) {
        int mass = 0;

        for (Integer aminoAcidMass : peptide) {
            mass += aminoAcidMass;
        }

        return mass;
    }

    public static boolean
    isPeptideConsistentWithSpectrum(List<Integer> peptide, List<Integer> spectrum) {
        List<Integer> peptideSpectrum = BA4J.getLinearSpectrum(peptide);
        List<Integer> spectrumCopy = new ArrayList<>(spectrum);

        for (Integer subpeptideMass : peptideSpectrum) {
            if (!spectrumCopy.remove(subpeptideMass)) {
                return false;
            }
        }

        return true;
    }

    public static char RNACodonToAminoAcid(String codon) {
        return switch (codon) {
            case "AAA", "AAG" -> 'K';
            case "AAC", "AAU" -> 'N';
            case "ACA", "ACC", "ACG", "ACU" -> 'T';
            case "AGA", "AGG", "CGA", "CGC", "CGG", "CGU" -> 'R';
            case "AGC", "AGU", "UCA", "UCC", "UCG", "UCU" -> 'S';
            case "AUA", "AUC", "AUU" -> 'I';
            case "AUG" -> 'M';
            case "CAA", "CAG" -> 'Q';
            case "CAC", "CAU" -> 'H';
            case "CCA", "CCC", "CCG", "CCU" -> 'P';
            case "CUA", "CUC", "CUG", "CUU", "UUA", "UUG" -> 'L';
            case "GAA", "GAG" -> 'E';
            case "GAC", "GAU" -> 'D';
            case "GCA", "GCC", "GCG", "GCU" -> 'A';
            case "GGA", "GGC", "GGG", "GGU" -> 'G';
            case "GUA", "GUC", "GUG", "GUU" -> 'V';
            case "UAA", "UAG", "UGA" -> '*';
            case "UAC", "UAU" -> 'Y';
            case "UGC", "UGU" -> 'C';
            case "UGG" -> 'W';
            case "UUC", "UUU" -> 'F';
            default -> throw new RuntimeException("Nonexistent RNA codon provided: %s"
                    .formatted(codon));
        };
    }

    public static char DNACodonToAminoAcid(String codon) {
        return switch (codon) {
            case "AAA", "AAG" -> 'K';
            case "AAC", "AAT" -> 'N';
            case "ACA", "ACC", "ACG", "ACT" -> 'T';
            case "AGA", "AGG", "CGA", "CGC", "CGG", "CGT" -> 'R';
            case "AGC", "AGT", "TCA", "TCC", "TCG", "TCT" -> 'S';
            case "ATA", "ATC", "ATT" -> 'I';
            case "ATG" -> 'M';
            case "CAA", "CAG" -> 'Q';
            case "CAC", "CAT" -> 'H';
            case "CCA", "CCC", "CCG", "CCT" -> 'P';
            case "CTA", "CTC", "CTG", "CTT", "TTA", "TTG" -> 'L';
            case "GAA", "GAG" -> 'E';
            case "GAC", "GAT" -> 'D';
            case "GCA", "GCC", "GCG", "GCT" -> 'A';
            case "GGA", "GGC", "GGG", "GGT" -> 'G';
            case "GTA", "GTC", "GTG", "GTT" -> 'V';
            case "TAA", "TAG", "TGA" -> '*';
            case "TAC", "TAT" -> 'Y';
            case "TGC", "TGT" -> 'C';
            case "TGG" -> 'W';
            case "TTC", "TTT" -> 'F';
            default -> throw new RuntimeException("Nonexistent DNA codon provided: %s"
                    .formatted(codon));
        };
    }

    public static String translate(String DNA) {
        int DNALength = DNA.length();
        String codon;
        StringBuilder peptide = new StringBuilder();

        for (int i = 0; i < DNALength; i += 3) {
            codon = DNA.substring(i, i + 3);
            peptide.append(DNACodonToAminoAcid(codon));
        }

        return peptide.toString();
    }

    public static int getAminoAcidIntegerMass(char aminoAcid) {
        return switch (aminoAcid) {
            case 'G' -> 57;
            case 'A' -> 71;
            case 'S' -> 87;
            case 'P' -> 97;
            case 'V' -> 99;
            case 'T' -> 101;
            case 'C' -> 103;
            case 'I', 'L' -> 113;
            case 'N' -> 114;
            case 'D' -> 115;
            case 'K', 'Q' -> 128;
            case 'E' -> 129;
            case 'M' -> 131;
            case 'H' -> 137;
            case 'F' -> 147;
            case 'R' -> 156;
            case 'Y' -> 163;
            case 'W' -> 186;
            default -> throw new RuntimeException("Nonexistent amino acid provided: %c"
                    .formatted(aminoAcid));
        };
    }

    public static void writePeptidesToFile(Collection<List<Integer>> peptides) {
        int i, peptideSize;

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (List<Integer> peptide : peptides) {
                peptideSize = peptide.size();
                i = 1;
                for (int aminoAcidMass : peptide) {
                    fileWriter.write("%d%c"
                            .formatted(aminoAcidMass, (i == peptideSize) ? ' ' : '-'));
                    ++i;
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }
}
