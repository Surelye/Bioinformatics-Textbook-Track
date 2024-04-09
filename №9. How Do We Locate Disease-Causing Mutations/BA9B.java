// Implement TrieMatching
// ----------------------
//
// Given a string Text and Trie(Patterns), we can quickly check whether any string from Patterns
// matches a prefix of Text. To do so, we start reading symbols from the beginning of Text and see
// what string these symbols “spell” as we proceed along the path downward from the root of the
// trie. For each new symbol in Text, if we encounter this symbol along an edge leading down from
// the present node, then we continue along this edge; otherwise, we stop and conclude that no
// string in Patterns matches a prefix of Text. If we make it all the way to a leaf, then the
// pattern spelled out by this path matches a prefix of Text.
//
// This algorithm is called PREFIXTRIEMATCHING.
//
// ------------------------------
// PREFIXTRIEMATCHING(Text, Trie)
//    symbol ← first letter of Text
//    v ← root of Trie
//    while forever
//       if v is a leaf in Trie
//          return the pattern spelled by the path from the root to v
//       else if there is an edge (v, w) in Trie labeled by symbol
//          symbol ← next letter of Text
//          v ← w
//       else
//          output "no matches found"
//          return
// ------------------------------
//
//
// PREFIXTRIEMATCHING finds whether any strings in Patterns match a prefix of Text. To find whether
// any strings in Patterns match a substring of Text starting at position k, we chop off the first
// k − 1 symbols from Text and run PREFIXTRIEMATCHING on the shortened string. As a result, to solve
// the Multiple Pattern Matching Problem (introduced in “Construct a Trie from a Collection of
// Patterns”, we simply iterate PREFIXTRIEMATCHING |Text| times, chopping the first symbol off of
// Text before each new iteration.
//
// ------------------------
// TRIEMATCHING(Text, Trie)
//    while Text is nonempty
//       PREFIXTRIEMATCHING(Text, Trie)
//       remove first symbol from Text
// ------------------------
//
// Implement TrieMatching
//
// Given: A string Text and a collection of strings Patterns.
//
// Return: All starting positions in Text where a string from Patterns appears as a substring.
//
// Sample Dataset
// --------------
// AATCGGGTTCAATCGGGGT
// ATCG
// GGGT
// --------------
//
// Sample Output
// -------------
// 1 4 11 15
// -------------

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class BA9B {

    private static String prefixTrieMatching(
            StringBuilder text, Map<Integer, Map<Integer, Character>> trie
    ) {
        int textLength = text.length(), index = 0, v = 0;
        char symbol = text.charAt(index++);
        StringBuilder pattern = new StringBuilder();

        while (true) {
            if (trie.get(v).isEmpty()) {
                return pattern.toString();
            } else if (trie.get(v).containsValue(symbol)) {
                v = BA9UTIL.getKeysByValue(trie.get(v), symbol)
                        .findFirst()
                        .orElseThrow();
                pattern.append(symbol);
                if (index != textLength) {
                    symbol = text.charAt(index++);
                }
            } else {
                return "";
            }
        }
    }

    private static List<Integer> trieMatchingMachinery(
            StringBuilder text, Map<Integer, Map<Integer, Character>> trie
    ) {
        List<Integer> positions = new ArrayList<>();
        int shift = 0;

        while (!text.isEmpty()) {
            if (!prefixTrieMatching(text, trie).isEmpty()) {
                positions.add(shift);
            }
            text.deleteCharAt(0);
            ++shift;
        }

        return positions;
    }

    public static List<Integer> trieMatching(
            String text, Map<Integer, Map<Integer, Character>> trie
    ) {
        return trieMatchingMachinery(new StringBuilder(text), trie);
    }

    public static List<Integer> trieMatching(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String text = strDataset.getFirst();
        Map<Integer, Map<Integer, Character>> trie = BA9A.constructATrie(
                strDataset
                        .stream()
                        .skip(1)
                        .toList()
        );

        return trieMatchingMachinery(new StringBuilder(text), trie);
    }

    private void run() {
        List<Integer> positions = trieMatching(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9b.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9b_out.txt")) {
            int nPositions = positions.size();
            for (int i = 0; i != nPositions; ++i) {
                fileWriter.write("%d%c".formatted(
                        positions.get(i),
                        (i == nPositions - 1) ? '\n' : ' '
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9B().run();
    }
}
