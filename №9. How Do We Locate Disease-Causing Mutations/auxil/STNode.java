package auxil;

public class STNode {

    private final int index;
    private final int position;
    private final int descent;
    private STNode parent;

    public STNode(int index, int position, int descent, STNode parent) {
        this.index = index;
        this.position = position;
        this.descent = descent;
        this.parent = parent;
    }

    public STNode(int index, int descent, STNode parent) {
        this(index, -1, descent, parent);
    }

    public int getPosition() {
        return position;
    }

    public int getDescent() {
        return descent;
    }

    public STNode getParent() {
        return parent;
    }

    public void setParent(STNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "STNode{i=%d, pos=%d, desc=%d, par=%s"
                .formatted(index, position, descent, parent);
    }
}
