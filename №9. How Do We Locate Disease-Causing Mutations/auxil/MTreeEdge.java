package auxil;

public record MTreeEdge(int position, int length) {

    @Override
    public String toString() {
        return "MTreeEdge{pos=%d, len=%d}"
                .formatted(position, length);
    }
}
