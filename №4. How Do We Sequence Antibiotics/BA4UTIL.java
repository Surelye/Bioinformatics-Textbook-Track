public class BA4UTIL {

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

    public static String transcribe(String DNA) {
        int DNALength = DNA.length();
        StringBuilder RNA = new StringBuilder(DNALength);

        for (int i = 0; i < DNALength; ++i) {
            RNA.append(
                    switch (DNA.charAt(i)) {
                        case 'A' -> 'A';
                        case 'C' -> 'C';
                        case 'G' -> 'G';
                        case 'T' -> 'U';
                        default -> throw new RuntimeException("Incorrect nucleotide: %c"
                                .formatted(DNA.charAt(i)));
                    }
            );
        }

        return RNA.toString();
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
}
