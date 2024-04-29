// Construct a Profile HMM
// -----------------------
//
// Profile HMM Problem
//
// Given: A threshold θ, followed by an alphabet Σ, followed by a multiple alignment `Alignment`
// whose strings are formed from Σ.
//
// Return: The transition and emission probabilities of the profile HMM HMM(Alignment, θ).
//
// Sample Dataset
// --------------
// 0.289
// --------
// A   B   C   D   E
// --------
// EBA
// EBD
// EB-
// EED
// EBD
// EBE
// E-D
// EBD
// --------------
//
// Sample Output
// -------------
// S   I0  M1  D1  I1  M2  D2  I2  M3  D3  I3  E
// S   0   0   1.0 0   0   0   0   0   0   0   0   0
// I0  0   0   0   0   0   0   0   0   0   0   0   0
// M1  0   0   0   0   0   0.875   0.125   0   0   0   0   0
// D1  0   0   0   0   0   0   0   0   0   0   0   0
// I1  0   0   0   0   0   0   0   0   0   0   0   0
// M2  0   0   0   0   0   0   0   0   0.857   0.143   0   0
// D2  0   0   0   0   0   0   0   0   1.0 0   0   0
// I2  0   0   0   0   0   0   0   0   0   0   0   0
// M3  0   0   0   0   0   0   0   0   0   0   0   1.0
// D3  0   0   0   0   0   0   0   0   0   0   0   1.0
// I3  0   0   0   0   0   0   0   0   0   0   0   0
// E   0   0   0   0   0   0   0   0   0   0   0   0
// --------
//     A   B   C   D   E
// S   0   0   0   0   0
// I0  0   0   0   0   0
// M1  0   0   0   0   1.0
// D1  0   0   0   0   0
// I1  0   0   0   0   0
// M2  0   0.857   0   0   0.143
// D2  0   0   0   0   0
// I2  0   0   0   0   0
// M3  0.143   0   0   0.714   0.143
// D3  0   0   0   0   0
// I3  0   0   0   0   0
// E   0   0   0   0   0
// -------------

import java.nio.file.Path;
import java.util.List;

public class BA10E {

    public static void constructProfileHMM(
            double threshold, List<Character> alphabet, List<String> alignment
    ) {

    }

    public static void constructProfileHMM(Path path) {

    }

    private void run() {
        constructProfileHMM(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/ba10/rosalind_ba10e.txt"
                )
        );
    }

    public static void main(String[] args) {
        new BA10E().run();
    }
}
