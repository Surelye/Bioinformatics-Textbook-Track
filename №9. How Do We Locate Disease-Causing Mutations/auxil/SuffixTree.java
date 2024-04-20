package auxil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SuffixTree {

    private final String text;
    private final Map<STNode, Map<STNode, STEdge>> st;
    private STNode rightmostLeaf;

    public SuffixTree(String text, STNode root) {
        this.text = text;
        st = new HashMap<>(Map.of(root, new HashMap<>()));
        this.rightmostLeaf = root;
    }

    public String getText() {
        return text;
    }

    public STNode getRightmostLeaf() {
        return rightmostLeaf;
    }

    public void setRightmostLeaf(STNode leaf) {
        if (st.containsKey(leaf)) {
            if (st.get(leaf).isEmpty()) {
                this.rightmostLeaf = leaf;
            } else {
                throw new RuntimeException("Provided node is not a leaf");
            }
        } else {
            throw new RuntimeException("Provided node is not in suffix tree");
        }
    }

    public void addEdge(STNode from, STNode to, STEdge edge) {
        if (st.containsKey(from)) {
            st.get(from).put(to, edge);
        } else {
            st.put(from, new HashMap<>(Map.of(to, edge)));
        }
        if (!st.containsKey(to)) {
            st.put(to, new HashMap<>());
        }
    }

    public void removeEdge(STNode from, STNode to) {
        if (st.containsKey(from)) {
            if (st.containsKey(to)) {
                st.get(from).remove(to);
            } else {
                throw new RuntimeException("Provided 'to' node is not in suffix tree");
            }
        } else {
            throw new RuntimeException("Provided 'from' node is not in suffix tree");
        }
    }

    public Set<STNode> keySet() {
        return st.keySet();
    }

    public Map<STNode, STEdge> get(STNode node) {
        return st.get(node);
    }

    @Override
    public String toString() {
        return "%s".formatted(st);
    }
}
