import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    public static <T> void writePathToFile(List<T> path) {
        int pathSize = path.size();
        int i = 1;

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (T node : path) {
                fileWriter.write("%s%s".formatted(node, (i == pathSize) ? "\n" : "->"));
                ++i;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }
}
