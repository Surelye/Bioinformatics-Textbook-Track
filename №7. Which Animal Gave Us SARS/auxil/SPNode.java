package auxil;

// SPNode stands for Small Parsimony Node

import java.util.List;
import java.util.ArrayList;

public class SPNode {

    private final int index;
    private final StringBuilder DNA;
    private final boolean isLeaf;
    private boolean isTagged;
    private final List<Integer> scores = new ArrayList<>();

    public SPNode(int index, String DNA) {
        this.index = index;
        this.DNA = new StringBuilder(DNA);
        this.isLeaf = !DNA.isEmpty();
    }

    public int getIndex() {
        return index;
    }

    public StringBuilder getDNA() {
        return DNA;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void untag() {
        this.isTagged = false;
    }

    public void tag() {
        this.isTagged = true;
    }

    public List<Integer> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "SPNode{" +
                "index=" + index +
                ", DNA=" + DNA +
                ", isLeaf=" + isLeaf +
                ", isTagged=" + isTagged +
                ", scores=" + scores +
                '}';
    }
}
