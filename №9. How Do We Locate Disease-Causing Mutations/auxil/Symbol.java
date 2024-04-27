package auxil;

public record Symbol(char symbol, int occurrence) implements Comparable<Symbol> {

    @Override
    public String toString() {
        return "Symb{%c, i=%d}".formatted(symbol, occurrence);
    }

    @Override
    public int compareTo(Symbol o) {
        int res = Character.compare(symbol, o.symbol);
        return (res == 0) ?
                Integer.compare(occurrence, o.occurrence) :
                res;
    }
}
