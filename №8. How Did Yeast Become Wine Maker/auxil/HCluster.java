package auxil;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class HCluster {

    private int label;
    private final List<Integer> nodes;

    public HCluster() {
        this.nodes = new ArrayList<>();
    }

    public HCluster(int label) {
        this.label = label;
        this.nodes = new ArrayList<>(List.of(label));
    }

    public int getLabel() {
        return label;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public void addNode(int node) {
        nodes.add(node);
    }

    public int size() {
        return nodes.size();
    }

    public static List<HCluster> initClusters(int n) {
        List<HCluster> clusters = new ArrayList<>(n);
        for (int i = 0; i != n; ++i) {
            clusters.add(new HCluster(i));
        }

        return clusters;
    }

    public static double
    getClusterDistance(HCluster f, HCluster s, Map<Integer, Map<Integer, Double>> D) {
        double dist = 0;

        for (int outerNode : f.getNodes()) {
            for (int innerNode : s.getNodes()) {
                dist += D.get(outerNode).get(innerNode);
            }
        }

        return dist / (f.size() * s.size());
    }

    public static Map<Integer, Double> getDistancesBetweenClustersAndCluster(
            List<HCluster> clusters, HCluster cluster, Map<Integer, Map<Integer, Double>> D
    ) {
        Map<Integer, Double> distances = new HashMap<>();
        for (HCluster cl : clusters) {
            distances.put(cl.getLabel(), getClusterDistance(cl, cluster, D));
        }

        return distances;
    }

    public static Map.Entry<HCluster, HCluster>
    getClosestClusters(List<HCluster> clusters, Map<Integer, Map<Integer, Double>> D) {
        int nClusters = clusters.size();
        assert (nClusters > 1);
        double curDist, minDist = Double.MAX_VALUE;
        Map.Entry<HCluster, HCluster> closestClusters = Map.entry(
                clusters.getFirst(),
                clusters.get(1)
        );

        for (int i = 0; i != nClusters; ++i) {
            for (int j = i + 1; j != nClusters; ++j) {
                curDist = getClusterDistance(clusters.get(i), clusters.get(j), D);
                if (curDist < minDist) {
                    minDist = curDist;
                    closestClusters = Map.entry(
                            clusters.get(i),
                            clusters.get(j)
                    );
                }
            }
        }

        return closestClusters;
    }

    public static HCluster mergeClusters(HCluster fCluster, HCluster sCluster, int label) {
        HCluster newCluster = new HCluster();

        for (int node : fCluster.nodes) {
            newCluster.addNode(node);
        }
        for (int node : sCluster.nodes) {
            newCluster.addNode(node);
        }
        newCluster.label = label;

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

        return "Cluster{label=%d, nodes={%s}"
                .formatted(label, str);
    }
}
