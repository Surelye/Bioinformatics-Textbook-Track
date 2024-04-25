import auxil.MSuffixTrie;
import auxil.Node;
import auxil.Edge;
import auxil.Symbol;
import com.sun.source.tree.BreakTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class BA9UTIL {

    public enum Color {
        GRAY,
        RED {
            @Override
            public String toString() {
                return "red";
            }
        },
        BLUE {
            @Override
            public String toString() {
                return "blue";
            }
        },
        PURPLE {
            @Override
            public String toString() {
                return "purple";
            }
        }
    }

    public static <K, V> Stream<K> getKeysByValue(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    public static MSuffixTrie constructModifiedSuffixTrie(String text) {
        int textLength = text.length(), index = 0;
        char currentSymbol;
        MSuffixTrie trie = new MSuffixTrie(new Node(0));
        Node currentNode, endingNode, newNode;
        Edge newEdge;

        for (int i = 0; i != textLength; ++i) {
            currentNode = new Node(0);
            for (int j = i; j != textLength; ++j) {
                currentSymbol = text.charAt(j);
                endingNode = trie.hasLabeledEdge(currentNode, currentSymbol);
                if (endingNode.isProper()) {
                    currentNode = endingNode;
                } else {
                    newNode = new Node(++index);
                    newEdge = new Edge(currentSymbol, j);
                    trie.addEdge(currentNode, newNode, newEdge);
                    currentNode = newNode;
                }
            }
            if (trie.isLeaf(currentNode)) {
                currentNode.setPosition(i);
            }
        }

        return trie;
    }

    public static List<Integer> constructLCP(String str, List<Integer> suf) {
        int n = str.length();
        int[] lcp = new int[n], pos = new int[n];
        for (int i = 0; i != n; ++i) {
            pos[suf.get(i)] = i;
        }
        int k = 0;

        for (int i = 0; i != n; ++i) {
            if (k > 0) {
                --k;
            }
            if (pos[i] == n - 1) {
                lcp[n - 1] = -1;
                k = 0;
            } else {
                int j = suf.get(pos[i] + 1);
                while (Math.max(i + k, j + k) < n && str.charAt(i + k) == str.charAt(j + k)) {
                    ++k;
                }
                lcp[pos[i]] = k;
            }
        }

        return Arrays.stream(lcp)
                .boxed()
                .toList();
    }

    public static List<Integer> parseIntArray(String strIntArr, String regex) {
        return Arrays.stream(strIntArr
                        .split(regex))
                .map(Integer::parseInt)
                .toList();
    }

    public static<T> void writeToFile(String filename, T elem) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write("%s\n".formatted(elem));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static Map<Integer, Integer> lastToFirst(String transform) {
        int textLen = transform.length(), idx;
        char cur;
        String sfc = new String(transform.chars().sorted().toArray(), 0, textLen);
        Map<Character, Integer> sm = new HashMap<>(), starts = new HashMap<>();
        Map<Integer, Integer> lastToFirst = new HashMap<>();
        for (int i = 0; i != textLen; ) {
            cur = sfc.charAt(i);
            sm.put(cur, 0);
            starts.put(cur, i);
            while (i < textLen && cur == sfc.charAt(i)) {
                ++i;
            }
        }
        for (int i = 0; i != textLen; ++i) {
            cur = transform.charAt(i);
            idx = starts.get(cur) + sm.get(cur);
            lastToFirst.put(i, idx);
            sm.put(cur, sm.get(cur) + 1);
        }

        return lastToFirst;
    }
}
