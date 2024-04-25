package auxil;

public record Symbol(char symbol, int occurrence) {
    @Override
    public String toString() {
        return "Symb{s=%c, i=%d}"
                .formatted(symbol, occurrence);
    }
}
