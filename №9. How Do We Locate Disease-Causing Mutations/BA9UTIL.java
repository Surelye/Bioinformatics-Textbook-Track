import auxil.MSuffixTrie;
import auxil.Node;
import auxil.Edge;

import java.util.Map;
import java.util.stream.Stream;

public class BA9UTIL {

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
}
