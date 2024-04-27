package auxil;

public record Suffix(String suffix, int position) {

    @Override
    public String toString() {
        return "Suffix{%s, at=%d}".formatted(suffix, position);
    }
}
