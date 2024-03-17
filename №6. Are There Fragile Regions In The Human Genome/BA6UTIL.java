import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BA6UTIL {

    public static List<Integer> parsePermutation(String strPerm) {
        return Arrays.stream(strPerm.substring(1, strPerm.length() - 1)
                .split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }

    public static List<Integer> parsePermutation(Path path) {
        List<String> strDataset = UTIL.readDataset(path);

        return parsePermutation(strDataset.getFirst());
    }

    public static <T> void writeToFile(String filename, Collection<T> elems) {
        int elemsSize = elems.size(), i = 0;

        try (FileWriter fileWriter = new FileWriter(filename)) {
            for (T elem : elems) {
                fileWriter.write("%s%c".formatted(elem, (i == elemsSize - 1) ? '\n' : ' '));
                ++i;
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static List<List<Integer>> parseGenome(String strGenome) {
        String strGenomeCopy = strGenome;
        int lParenIndex, rParenIndex;
        List<List<Integer>> genome = new ArrayList<>();

        while (true) {
            lParenIndex = strGenomeCopy.indexOf('(');
            rParenIndex = strGenomeCopy.indexOf(')');
            if (lParenIndex == -1 || rParenIndex == -1) {
                break;
            }
            genome.add(
                    Arrays.stream(strGenomeCopy.substring(lParenIndex + 1, rParenIndex)
                            .split("\\s+"))
                            .map(Integer::parseInt)
                            .toList()
            );
            strGenomeCopy = strGenomeCopy.substring(rParenIndex + 1);
        }

        return genome;
    }

    public static List<Integer> parseIntArray(String strIntArr, String separator) {
        return Arrays.stream(strIntArr.split(separator))
                .map(Integer::parseInt)
                .toList();
    }

    public static List<Map.Entry<Integer, Integer>> parseEdges(String strEdges) {
        String edgesCopy = strEdges;
        int lParenIndex, rParenIndex;
        List<Integer> edge;
        List<Map.Entry<Integer, Integer>> edges = new ArrayList<>();

        while (true) {
            lParenIndex = edgesCopy.indexOf('(');
            rParenIndex = edgesCopy.indexOf(')');
            if (lParenIndex == -1 || rParenIndex == -1) {
                break;
            }
            edge = parseIntArray(
                    edgesCopy.substring(lParenIndex + 1, rParenIndex), ", "
            );
            edges.add(Map.entry(edge.getFirst(), edge.getLast()));
            edgesCopy = edgesCopy.substring(rParenIndex + 1);
        }

        return edges;
    }

    public static void writeEdgesToFile(String filename, List<Map.Entry<Integer, Integer>> edges) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            int edgesSize = edges.size();
            Map.Entry<Integer, Integer> edge;
            for (int i = 0; i < edgesSize; ++i) {
                edge = edges.get(i);
                fileWriter.write("(%d, %d)%s".formatted(
                        edge.getKey(), edge.getValue(), (i == edgesSize - 1) ? '\n' : ", "
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void
    writeGenomeToFile(String filename, List<List<Integer>> genome, String separator) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            int genomeSize = genome.size(), chromosomeSize, syntenyBlock;
            List<Integer> chromosome;
            for (int i = 0; i < genomeSize; ++i) {
                chromosome = genome.get(i);
                chromosomeSize = chromosome.size();
                fileWriter.write('(');
                for (int j = 0; j < chromosomeSize; ++j) {
                    syntenyBlock = chromosome.get(j);
                    fileWriter.write("%s%d%c".formatted(
                            (syntenyBlock > 0) ? "+" : "",
                            syntenyBlock,
                            (j == chromosomeSize - 1) ? ')' : ' '
                    ));
                }
                fileWriter.write("%c".formatted(
                        (i == genomeSize - 1) ? '\n' : ' '
                ));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }
}
