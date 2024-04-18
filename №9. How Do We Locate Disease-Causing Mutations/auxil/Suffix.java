package auxil;

public record Suffix(String suffix, int position) {

    @Override
    public String toString() {
        return "Suffix{suffix=%s, pos=%d}"
                .formatted(suffix, position);
    }
}
