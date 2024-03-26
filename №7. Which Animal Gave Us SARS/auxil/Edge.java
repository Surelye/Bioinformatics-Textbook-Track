package auxil;

public record Edge(int from, int to, double weight) implements Comparable<Edge> {

    @Override
    public String toString() {
        return "Edge{from=%s, to=%s, weight=%.3f}"
                .formatted(from, to, weight);
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(this.from, o.from);
    }
}
