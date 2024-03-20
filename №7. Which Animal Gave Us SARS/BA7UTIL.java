import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
