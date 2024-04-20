package auxil;

public record STEdge(int position, int length) {

    @Override
    public String toString() {
        return "STEdge{pos=%d, len=%d}"
                .formatted(position, length);
    }
}
