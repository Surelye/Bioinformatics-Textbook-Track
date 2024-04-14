package auxil;

import java.util.Objects;

public class Node {

    private final int index;
    private int position;

    public Node(int index) {
        this.index = index;
        this.position = -1;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isProper() {
        return index != -1;
    }

    @Override
    public String toString() {
        return "Node{index=%d, position=%d}"
                .formatted(index, position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return index == node.index && position == node.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
