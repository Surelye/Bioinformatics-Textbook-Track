import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UTIL {

    public static List<String> readDataset(Path path) {
        List<String> sampleDataset;

        try {
            sampleDataset = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Failed to read file lines");
            return List.of();
        }

        return sampleDataset;
    }

    public static <T> void writeToFile(Collection<T> elems) {
        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (T elem : elems) {
                fileWriter.write("%s ".formatted(elem));
            }
            fileWriter.write("\n");
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static <T> void writeToFileWithNewlines(Collection<T> elems) {
        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (T elem : elems) {
                fileWriter.write("%s\n".formatted(elem));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static List<Integer> parseIntArray(String strIntArray) {
        return Arrays.stream(strIntArray
                .split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }
}
