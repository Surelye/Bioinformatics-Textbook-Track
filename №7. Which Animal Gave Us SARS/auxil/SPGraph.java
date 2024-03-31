package auxil;

import java.util.List;
import java.util.Map;

// SPGraph stands for Small Parsimony Graph

public class SPGraph {

    private final List<SPNode> nodes;
    private final Map<Integer, List<Integer>> adjList;

    public SPGraph(List<SPNode> nodes, Map<Integer, List<Integer>> adjList) {
        this.nodes = nodes;
        this.adjList = adjList;
    }

    public List<SPNode> getNodes() {
        return nodes;
    }

    public SPNode getNode(int i) {
        return nodes.get(i);
    }

    public List<SPNode> getAdjacents(int node) {
        if (!adjList.containsKey(node)) {
            return List.of();
        }
        return adjList.get(node)
                .stream()
                .map(this::getNode)
                .toList();
    }

    @Override
    public String toString() {
        return "SPGraph{nodes{%s}, adjList{%s}}"
                .formatted(nodes, adjList);
    }
}
