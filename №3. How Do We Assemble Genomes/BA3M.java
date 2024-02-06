// Generate All Maximal Non-Branching Paths in a Graph
// ---------------------------------------------------
//
// A node v in a directed graph Graph is called a 1-in-1-out node if its indegree and outdegree are
// both equal to 1, i.e., in(v) = out(v) = 1.  We can rephrase the definition of a "maximal
// non-branching path" from the main text as a path whose internal nodes are 1-in-1-out nodes and
// whose initial and final nodes are not 1-in-1-out nodes.  Also, note that the definition from the
// main text does not handle the special case when Graph has a connected component that is an
// isolated cycle, in which all nodes are 1-in-1-out nodes.
//
// The MaximalNonBranchingPaths pseudocode below generates all non-branching paths in a graph. It
// iterates through all nodes of the graph that are not 1-in-1-out nodes and generates all
// non-branching paths starting at each such node. In a final step, MaximalNonBranchingPaths finds
// all isolated cycles in the graph.
//
// -------------------------------
// MaximalNonBranchingPaths(Graph)
//     Paths ← empty list
//     for each node v in Graph
//         if v is not a 1-in-1-out node
//             if out(v) > 0
//                 for each outgoing edge (v, w) from v
//                     NonBranchingPath ← the path consisting of the single edge (v, w)
//                     while w is a 1-in-1-out node
//                         extend NonBranchingPath by the outgoing edge (w, u) from w
//                         w ← u
//                     add NonBranchingPath to the set Paths
//     for each isolated cycle Cycle in Graph
//         add Cycle to Paths
//     return Paths
// -------------------------------
//
// Maximal Non-Branching Path Problem
//
// Find all maximal non-branching paths in a graph.
//
// Given: The adjacency list of a graph whose nodes are integers.
//
// Return: The collection of all maximal non-branching paths in the graph.
//
// Sample Dataset
// --------------
// 1 -> 2
// 2 -> 3
// 3 -> 4,5
// 6 -> 7
// 7 -> 6
// --------------
//
// Sample Output
// -------------
// 1 -> 2 -> 3
// 3 -> 4
// 3 -> 5
// 7 -> 6 -> 7
// -------------

import java.nio.file.Path;
import java.util.*;

public class BA3M {

    private static List<Integer>
    getIsolatedCycle(Map<Integer, List<Integer>> graph, int node,
                     Map<Integer, int[]> inOutDegrees, Map<Integer, Boolean> visited,
                     List<Integer> cycle) {
        visited.put(node, true);
        cycle.add(node);

        for (int adjNode : graph.get(node)) {
            int[] degs = inOutDegrees.get(adjNode);
            if (degs[0] == 1 && degs[1] == 1) {
                if (visited.get(adjNode) && adjNode == cycle.getFirst()) {
                    cycle.add(adjNode);
                    return cycle;
                } else {
                    cycle = getIsolatedCycle(graph, adjNode, inOutDegrees, visited, cycle);
                    if (!cycle.isEmpty()) {
                        return cycle;
                    }
                }
            }
        }

        return List.of();
    }

    private static List<List<Integer>>
    findAllMaximalNonBranchingPathsMachinery(Map<Integer, List<Integer>> graph) {
        Map<Integer, int[]> inOutDegrees = BA3UTIL.getInOutDegrees(graph);
        List<List<Integer>> paths = BA3UTIL.findAllMaximalNonBranchingPaths(graph, inOutDegrees);
        List<Integer> cycle;
        Map<Integer, Boolean> colors = new HashMap<>();
        for (int node : graph.keySet()) {
            colors.put(node, false);
        }

        for (int node : graph.keySet()) {
            int[] degs = inOutDegrees.get(node);
            if (degs[0] == 1 && degs[1] == 1) {
                if (!colors.get(node)) {
                    cycle = getIsolatedCycle(graph, node, inOutDegrees, colors,
                            new ArrayList<>());
                    if (!cycle.isEmpty()) {
                        paths.add(cycle);
                    }
                }
            }
        }
        BA3UTIL.writeListOfPathsToFile(paths);

        return paths;
    }

    public static List<List<Integer>> findAllMaximalNonBranchingPaths(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (String adjList : strDataset) {
            String[] nodes = adjList.split(" -> ");
            graph.put(Integer.parseInt(nodes[0]), Arrays
                    .stream(nodes[1].split(","))
                    .map(Integer::parseInt)
                    .toList());
        }

        return findAllMaximalNonBranchingPathsMachinery(graph);
    }


    public static List<List<Integer>>
    findAllMaximalNonBranchingPaths(Map<Integer, List<Integer>> graph) {
        return findAllMaximalNonBranchingPathsMachinery(graph);
    }
}
