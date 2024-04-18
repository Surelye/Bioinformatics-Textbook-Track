import auxil.MSuffixTrie;
import auxil.Node;
import auxil.Edge;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
}
