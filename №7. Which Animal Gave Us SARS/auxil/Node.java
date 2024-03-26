package auxil;

public record Node(int label, double age) {

    @Override
    public String toString() {
        return "Node{label=%d, age=%.3f}"
                .formatted(label, age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return label == node.label && Double.compare(age, node.age) == 0;
    }

}
