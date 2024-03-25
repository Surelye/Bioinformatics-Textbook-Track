package auxil;

import java.util.Objects;

public class Node {
    private final int label;
    private double age;

    public Node(int label, int weight) {
        this.label = label;
        this.age = weight;
    }

    public int getLabel() {
        return label;
    }

    public double getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(label, age);
    }
}
