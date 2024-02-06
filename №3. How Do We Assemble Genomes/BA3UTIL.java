import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BA3UTIL {

    public static void
    sortGraphAdjLists(Map<String, List<String>> graph, Comparator<String> stringComparator) {
        for (String node : graph.keySet()) {
            graph.get(node).sort(stringComparator);
        }
    }

    public static void printGraph(Map<String, List<String>> graph) {
        for (String node : graph.keySet()) {
            List<String> adjacents = graph.get(node);
            int adjacentsSize = adjacents.size();
            int i = 1;
            System.out.printf("%s -> ", node);
            for (String adjacent : adjacents) {
                System.out.printf("%s%c", adjacent, (i == adjacentsSize) ? '\n' : ',');
                ++i;
            }
        }
    }

    public static void writeGraphToFile(Map<String, List<String>> graph) {
        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (String node : graph.keySet()) {
                List<String> adjacents = graph.get(node);
                int adjacentsSize = adjacents.size();
                int i = 1;
                fileWriter.write("%s -> ".formatted(node));
                for (String adjacent : adjacents) {
                    fileWriter.write("%s%c".formatted(adjacent,
                            (i == adjacentsSize) ? '\n' : ','));
                    ++i;
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static<T> void printPath(List<T> path) {
        int pathSize = path.size();
        int i = 1;

        for (T node : path) {
            System.out.printf("%s%s", node, (i == pathSize) ? "\n" : "->");
            ++i;
        }
    }

    public static<T> void writePathToFile(List<T> path, FileWriter fileWriter) throws IOException {
        int pathSize = path.size();
        int i = 1;

        for (T node : path) {
            fileWriter.write("%s%s".formatted(node, (i == pathSize) ? "\n" : " -> "));
            ++i;
        }
    }
    public static<T> void writePathToFile(List<T> path) {
        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            writePathToFile(path, fileWriter);
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static<T> void writeListOfPathsToFile(List<List<T>> listOfPaths) {
        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (List<T> path : listOfPaths) {
                writePathToFile(path, fileWriter);
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static<T> Map<T, int[]> getInOutDegrees(Map<T, List<T>> graph) {
        Map<T, int[]> inOutDegrees = new HashMap<>();

        for (T node : graph.keySet()) {
            if (inOutDegrees.containsKey(node)) {
                inOutDegrees.get(node)[1] = graph.get(node).size();
            } else {
                inOutDegrees.put(node, new int[]{0, graph.get(node).size()});
            }

            for (T adjNode : graph.get(node)) {
                if (inOutDegrees.containsKey(adjNode)) {
                    inOutDegrees.get(adjNode)[0] += 1;
                } else {
                    inOutDegrees.put(adjNode, new int[]{1, 0});
                }
            }
        }
        return inOutDegrees;
    }

    private static<T> void
    findAllMaximalNonBranchingPathsFromEachVertex(Map<T, List<T>> graph, T node, Map<T, int[]> inOutDegrees,
                                                  List<T> pathList, List<List<T>> paths) {
        pathList.add(node);
        int[] degs = inOutDegrees.get(node);
        if (!(degs[0] == 1 && degs[1] == 1) && pathList.size() != 1) {
            paths.add(new ArrayList<>(pathList));
            pathList.removeLast();
            return;
        }

        for (T adjNode : graph.get(node)) {
            findAllMaximalNonBranchingPathsFromEachVertex(graph, adjNode, inOutDegrees, pathList, paths);
        }
        pathList.removeLast();
    }

    private static<T> List<List<T>>
    findAllMaximalNonBranchingPathsMachinery(Map<T, List<T>> graph, Map<T, int[]> inOutDegrees) {
        List<List<T>> paths = new ArrayList<>();

        for (T node : graph.keySet()) {
            int[] degs = inOutDegrees.get(node);
            if (!(degs[0] == 1 && degs[1] == 1)) {
                findAllMaximalNonBranchingPathsFromEachVertex(graph, node, inOutDegrees,
                        new ArrayList<>(), paths);
            }
        }

        return paths;
    }

    public static<T> List<List<T>> findAllMaximalNonBranchingPaths(Map<T, List<T>> graph) {
        Map<T, int[]> inOutDegrees = getInOutDegrees(graph);

        return findAllMaximalNonBranchingPathsMachinery(graph, inOutDegrees);
    }

    public static<T> List<List<T>>
    findAllMaximalNonBranchingPaths(Map<T, List<T>> graph, Map<T, int[]> inOutDegrees) {
        return findAllMaximalNonBranchingPathsMachinery(graph, inOutDegrees);
    }
}
