import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BA7UTIL {

    public static Map<Integer, List<Map.Entry<Integer, Integer>>>
    parseAdjList(List<String> strAdjList) {
        int from, to, weight;
        String[] edgeComponents;
        String[] toComponents;
        Map<Integer, List<Map.Entry<Integer, Integer>>> adjList = new HashMap<>();

        for (String rawEdge : strAdjList) {
            edgeComponents = rawEdge.split("->");
            toComponents = edgeComponents[1].split(":");
            from = Integer.parseInt(edgeComponents[0]);
            to = Integer.parseInt(toComponents[0]);
            weight = Integer.parseInt(toComponents[1]);

            if (adjList.containsKey(from)) {
                adjList.get(from).add(Map.entry(to, weight));
            } else {
                adjList.put(from, new ArrayList<>(List.of(Map.entry(to, weight))));
            }
        }

        return adjList;
    }

    public static <T> void writeToFile(String filename, Collection<T> elems) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            int i = 0, elemsSize = elems.size();
            for (T elem : elems) {
                fileWriter.write("%s%c".formatted(
                        elem, (i == elemsSize - 1) ? '\n' : ' '
                ));
                ++i;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void
    writeAdjListToFile(String filename, Map<Integer, Map<Integer, Integer>> adjList) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            int numNodes = adjList.size();
            for (int from = 0; from < numNodes; ++from) {
                for (int to : adjList.get(from).keySet()) {
                    fileWriter.write("%d->%d:%d\n".formatted(
                            from, to, adjList.get(from).get(to)
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }
}
