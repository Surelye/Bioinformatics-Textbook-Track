import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
}
