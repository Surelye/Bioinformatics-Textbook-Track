package auxil;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class MSuffixTree {

    private final Map<Node, Map<Node, MTreeEdge>> tree;

    public MSuffixTree(Node root) {
        this.tree = new HashMap<>(Map.of(root, new HashMap<>()));
    }

    public void addEdge(Node from, Node to, MTreeEdge edge) {
        if (tree.containsKey(from)) {
            tree.get(from).put(to, edge);
        } else {
            tree.put(from, new HashMap<>(Map.of(to, edge)));
        }
    }

    public Set<Node> keySet() {
        return tree.keySet();
    }

    public Map<Node, MTreeEdge> get(Node node) {
        return tree.get(node);
    }

    @Override
    public String toString() {
        return "MSuffixTree{tree={%s}}"
                .formatted(tree);
    }
}
