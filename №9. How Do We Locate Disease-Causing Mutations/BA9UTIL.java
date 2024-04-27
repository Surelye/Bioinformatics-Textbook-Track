import auxil.MSuffixTrie;
import auxil.Node;
import auxil.Edge;
import auxil.Symbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class BA9UTIL {

    public static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    public enum Color {
        GRAY,
        RED {
            @Override
            public String toString() {
                return "red";
            }
        },
        BLUE {
            @Override
            public String toString() {
                return "blue";
            }
        },
        PURPLE {
            @Override
            public String toString() {
                return "purple";
            }
        }
    }

    public static <K, V> Stream<K> getKeysByValue(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    public static MSuffixTrie constructModifiedSuffixTrie(String text) {
        int textLength = text.length(), index = 0;
        char currentSymbol;
        MSuffixTrie trie = new MSuffixTrie(new Node(0));
        Node currentNode, endingNode, newNode;
        Edge newEdge;

        for (int i = 0; i != textLength; ++i) {
            currentNode = new Node(0);
            for (int j = i; j != textLength; ++j) {
                currentSymbol = text.charAt(j);
                endingNode = trie.hasLabeledEdge(currentNode, currentSymbol);
                if (endingNode.isProper()) {
                    currentNode = endingNode;
                } else {
                    newNode = new Node(++index);
                    newEdge = new Edge(currentSymbol, j);
                    trie.addEdge(currentNode, newNode, newEdge);
                    currentNode = newNode;
                }
            }
            if (trie.isLeaf(currentNode)) {
                currentNode.setPosition(i);
            }
        }

        return trie;
    }

    public static List<Integer> constructLCP(String str, List<Integer> suf) {
        int n = str.length();
        int[] lcp = new int[n], pos = new int[n];
        for (int i = 0; i != n; ++i) {
            pos[suf.get(i)] = i;
        }
        int k = 0;

        for (int i = 0; i != n; ++i) {
            if (k > 0) {
                --k;
            }
            if (pos[i] == n - 1) {
                lcp[n - 1] = -1;
                k = 0;
            } else {
                int j = suf.get(pos[i] + 1);
                while (Math.max(i + k, j + k) < n && str.charAt(i + k) == str.charAt(j + k)) {
                    ++k;
                }
                lcp[pos[i]] = k;
            }
        }

        return Arrays.stream(lcp)
                .boxed()
                .toList();
    }

    public static List<Integer> parseIntArray(String strIntArr, String regex) {
        return Arrays.stream(strIntArr
                        .split(regex))
                .map(Integer::parseInt)
                .toList();
    }

    public static<T> void writeToFile(String filename, T elem) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write("%s\n".formatted(elem));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static<T> void writeToFile(String filename, List<T> elems, String sep) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            int eSize = elems.size();
            for (int i = 0; i != eSize; ++i) {
                fileWriter.write("%s%s".formatted(elems.get(i), (i == eSize - 1) ? "\n" : sep));
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static Map<Integer, Integer> lastToFirst(String transform, String sfc) {
        int textLen = transform.length(), idx;
        char cur;
        Map<Character, Integer> sm = new HashMap<>(), starts = new HashMap<>();
        Map<Integer, Integer> lastToFirst = new HashMap<>();
        for (int i = 0; i != textLen; ) {
            cur = sfc.charAt(i);
            sm.put(cur, 0);
            starts.put(cur, i);
            while (i < textLen && cur == sfc.charAt(i)) {
                ++i;
            }
        }
        for (int i = 0; i != textLen; ++i) {
            cur = transform.charAt(i);
            idx = starts.get(cur) + sm.get(cur);
            lastToFirst.put(i, idx);
            sm.put(cur, sm.get(cur) + 1);
        }

        return lastToFirst;
    }

    public static Map<Integer, Integer> lastToFirst(String transform) {
        return lastToFirst(
                transform,
                new String(transform.chars().sorted().toArray(), 0, transform.length())
        );
    }

    public static Map<Character, List<Integer>> getCountArrays(String str) {
        int strLen = str.length();
        char cur;
        List<Integer> curCount;
        Map<Character, List<Integer>> count = new HashMap<>();
        for (int i = 0; i != strLen; ++i) {
            cur = str.charAt(i);
            if (count.containsKey(cur)) {
                curCount = count.get(cur);
                curCount.addAll(
                        Collections.nCopies(
                                i + 1 - curCount.size(),
                                curCount.getLast()
                        )
                );
                curCount.add(curCount.getLast() + 1);
            } else {
                count.put(cur, new ArrayList<>(Collections.nCopies(i + 1, 0)));
                count.get(cur).add(1);
            }
        }
        for (char symbol : count.keySet()) {
            curCount = count.get(symbol);
            curCount.addAll(
                    Collections.nCopies(
                            strLen + 1 - curCount.size(),
                            curCount.getLast()
                    )
            );
        }

        return count;
    }

    public static String getFirstColumn(String text) {
        return new String(text.chars().sorted().toArray(), 0, text.length());
    }

    public static Map<Character, Integer> getFirstOccurrence(String text, boolean isSorted) {
        int strLen = text.length();
        char cur;
        String fc;
        if (isSorted) {
            fc = text;
        } else {
            fc = getFirstColumn(text);
        }
        Map<Character, Integer> firstOccurrence = new HashMap<>();
        for (int i = 0; i != strLen; ) {
            cur = fc.charAt(i);
            firstOccurrence.put(cur, i);
            while (i < strLen && cur == fc.charAt(i)) {
                ++i;
            }
        }

        return firstOccurrence;
    }

    public static Map<Character, Integer> getFirstOccurrence(String text) {
        return getFirstOccurrence(text, false);
    }

    public static Map<Character, List<Integer>> getCheckpointArrays(String str, int C) {
        int li = str.length(), nEls = li / C + 1;
        Map<Character, List<Integer>> countArrays = getCountArrays(str);
        List<Integer> checkPointArray, curCount;
        Map<Character, List<Integer>> checkpointArrays = new HashMap<>();
        for (char symbol : countArrays.keySet()) {
            checkPointArray = new ArrayList<>(nEls);
            curCount = countArrays.get(symbol);
            for (int i = 0; i <= li; i += C) {
                checkPointArray.add(curCount.get(i));
            }
            checkpointArrays.put(symbol, checkPointArray);
        }

        return checkpointArrays;
    }

    public static List<Symbol> stringToSymbolList(String str) {
        int strLen = str.length();
        char chr;
        Map<Character, Integer> occs = new HashMap<>();
        List<Symbol> sl = new ArrayList<>();
        for (int i = 0; i != strLen; ++i) {
            chr = str.charAt(i);
            if (occs.containsKey(chr)) {
                sl.add(new Symbol(chr, occs.get(chr)));
                occs.put(chr, occs.get(chr) + 1);
            } else {
                sl.add(new Symbol(chr, 1));
                occs.put(chr, 2);
            }
        }
        return sl;
    }

    public static<T> List<T> getFutureResults(List<Callable<T>> tasks) {
        List<T> results = new ArrayList<>(tasks.size());

        try {
            List<Future<T>> futureResults = executor.invokeAll(tasks);
            executor.shutdown();
            for (Future<T> futureResult : futureResults) {
                T result = futureResult.get();
                results.add(result);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Failed to execute task");
            e.printStackTrace();
        }

        return results;
    }
}
