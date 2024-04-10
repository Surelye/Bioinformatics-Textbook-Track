package auxil;

import java.util.Map;
import java.util.HashMap;

public class MSuffixTrie {

    private final Map<Node, Map<Node, Edge>> trie;

    public MSuffixTrie(Node root) {
        this.trie = new HashMap<>(Map.of(root, new HashMap<>()));
    }

    public Node hasLabeledEdge(Node node, char symbol) {
        if (trie.containsKey(node)) {
            for (Node to : trie.get(node).keySet()) {
                if (trie.get(node).get(to).getLabel() == symbol) {
                    return to;
                }
            }
        }
        return new Node(-1);
    }

    public void addEdge(Node from, Node to, Edge edge) {
        if (trie.containsKey(from)) {
            trie.get(from).put(to, edge);
        } else {
            trie.put(from, new HashMap<>(Map.of(to, edge)));
        }
    }

    public boolean isLeaf(Node node) {
        if (!trie.containsKey(node)) {
            trie.put(node, new HashMap<>());
            return true;
        } else {
            return trie.get(node).isEmpty();
        }
    }

    @Override
    public String toString() {
        return "MSuffixTrie{trie=%s}"
                .formatted(trie);
    }
}
