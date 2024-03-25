package auxil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Cluster {
    private int label;
    private final List<Node> nodes;
    private Node representative;

    public Cluster(int label) {
        this.label = label;
        nodes = new ArrayList<>();
    }

    public Cluster(int label, Node node) {
        this.label = label;
        nodes = new ArrayList<>(List.of(node));
        representative = node;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getRepresentative() {
        return representative;
    }

    public int size() {
        return nodes.size();
    }

    public static List<Cluster> initClusters(int n) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i != n; ++i) {
            clusters.add(
                    new Cluster(
                            i,
                            new Node(i, 0)
                    )
            );
        }

        return clusters;
    }

    public static double
    getClusterDistance(Cluster f, Cluster s, Map<Integer, Map<Integer, Integer>> D) {
        double dist = 0;
        int outerLabel;

        for (Node fCNode : f.getNodes()) {
            outerLabel = fCNode.getLabel();
            for (Node sCNode : s.getNodes()) {
                dist += D.get(outerLabel).get(sCNode.getLabel());
            }
        }

        return dist / (f.size() * s.size());
    }

    public static Pair<Cluster, Cluster>
    getClosestClusters(List<Cluster> clusters, Map<Integer, Map<Integer, Integer>> D) {
        if (clusters.size() < 2) {
            throw new RuntimeException("Incorrect number of clusters");
        }
        int numClusters = clusters.size();
        double curDist, minDist = Double.MAX_VALUE;
        Pair<Cluster, Cluster> closestClusters = new Pair<>(
                clusters.getFirst(),
                clusters.get(1)
        );

        for (int i = 0; i != numClusters; ++i) {
            for (int j = i + 1; j != numClusters; ++j) {
                curDist = getClusterDistance(clusters.get(i), clusters.get(j), D);
                if (curDist < minDist) {
                    minDist = curDist;
                    closestClusters.setFirst(clusters.get(i));
                    closestClusters.setSecond(clusters.get(j));
                }
            }
        }

        return closestClusters;
    }

    public static Cluster mergeClusters(Cluster fCluster, Cluster sCluster, Node representative) {
        Cluster newCluster = new Cluster(representative.getLabel());

        for (Node fClusterNode : fCluster.getNodes()) {
            newCluster.getNodes().add(fClusterNode);
        }
        for (Node sClusterNode : sCluster.getNodes()) {
            newCluster.getNodes().add(sClusterNode);
        }
        newCluster.representative = representative;

        return newCluster;
    }

    @Override
    public String toString() {
        int n = nodes.size();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i != n; ++i) {
            str.append(nodes.get(i));
            if (i != n - 1) {
                str.append(", ");
            }
        }

        return "Cluster{label=%d, {%s}, representative=%s}"
                .formatted(label, str, representative);
    }
}
