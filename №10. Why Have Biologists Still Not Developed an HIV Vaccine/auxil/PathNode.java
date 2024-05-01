package auxil;

public record PathNode(NodeType nodeType, int index) {

    public enum NodeType {
        s { @Override public String toString() { return "s"; } },
        M { @Override public String toString() { return "M"; } },
        D { @Override public String toString() { return "D"; } },
        I { @Override public String toString() { return "I"; } },
        e { @Override public String toString() { return "e"; } }
    }
}
