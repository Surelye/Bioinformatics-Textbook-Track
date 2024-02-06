public class Pair<T, V> {

    private T first;
    private V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "<%s, %s>".formatted(first.toString(), second.toString());
    }

    @Override
    public int hashCode() {
        return 31 + ((first == null) ? 0 : first.hashCode())
                + ((second == null) ? 0 : second.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Pair<?, ?> other = (Pair<?, ?>) o;

        return (first.equals(other.getFirst()) && second.equals(other.getSecond()));
    }
}
