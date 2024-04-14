package auxil;

public class Edge {

    private final char label;
    private final int position;

    public Edge(char label, int position) {
        this.label = label;
        this.position = position;
    }

    public int getLabel() {
        return label;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Edge{label=%c, position=%d}"
                .formatted(label, position);
    }
}
