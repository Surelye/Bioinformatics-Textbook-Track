package auxil;

public class Edge {

    private final char label;
    private final int position;

    public Edge(char label, int position) {
        this.label = label;
        this.position = position;
    }

    public char getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Edge{label=%c, position=%d}"
                .formatted(label, position);
    }
}
