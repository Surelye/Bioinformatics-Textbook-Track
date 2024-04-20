// Construct a Suffix Tree from a Suffix Array
// -------------------------------------------
//
// SuffixTree(Text) can be constructed in linear time from SuffixArray(Text) by using the longest
// common prefix (LCP) array of Text, LCP(Text), which stores the length of the longest common
// prefix shared by consecutive lexicographically ordered suffixes of Text. For example,
// LCP("panamabananas$") is (0, 0, 1, 1, 3, 3, 1, 0, 0, 0, 2, 2, 0, 0).
//
// Suffix Tree Construction from Suffix Array Problem
//
// Construct a suffix tree from the suffix array and LCP array of a string.
//
// Given: A string Text, SuffixArray(Text), and LCP(Text).
//
// Return: The strings labeling the edges of SuffixTree(Text). (You may return these strings in
// any order.)
//
// Sample Dataset
// --------------
// GTAGT$
// 5, 2, 3, 0, 4, 1
// 0, 0, 0, 2, 0, 1
// --------------
//
// Sample Output
// -------------
// $
// T
// AGT$
// $
// AGT$
// GT
// $
// AGT$
// -------------

import auxil.STNode;
import auxil.STEdge;
import auxil.SuffixTree;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BA9R {

    private static SuffixTree constructSuffixTreeMachinery(
            String text, List<Integer> suffixArray, List<Integer> lcp
    ) {
        int index = 0, textLen = text.length(), desc;
        STNode root = new STNode(index++, 0, null);
        SuffixTree st = new SuffixTree(text, root);
        st.addEdge(
                root,
                new STNode(index++, textLen - 1, 1, root),
                new STEdge(textLen - 1, 1)
        );
        STNode rightmostNode, prevNode, newNode, newLeaf;
        STEdge newEdge;

        for (int i = 1; i != textLen; ++i) {
            prevNode = null;
            rightmostNode = st.getRightmostLeaf();
            while (true) {
                if (rightmostNode.getDescent() == lcp.get(i)) {
                    desc = textLen - (suffixArray.get(i) + lcp.get(i));
                    newNode = new STNode(
                            index++, suffixArray.get(i),
                            rightmostNode.getDescent() + desc, rightmostNode
                    );
                    newEdge = new STEdge(suffixArray.get(i) + lcp.get(i), desc);
                    st.addEdge(rightmostNode, newNode, newEdge);
                    st.setRightmostLeaf(newNode);
                    break;
                } else if (rightmostNode.getDescent() < lcp.get(i)) {
                    st.removeEdge(rightmostNode, prevNode);
                    newEdge = new STEdge(
                            suffixArray.get(i) + rightmostNode.getDescent(),
                            lcp.get(i) - rightmostNode.getDescent()
                    );
                    newNode = new STNode(
                            index++, rightmostNode.getDescent() + newEdge.length(),
                            rightmostNode
                    );
                    st.addEdge(rightmostNode, newNode, newEdge);
                    assert prevNode != null;
                    prevNode.setParent(newNode);
                    st.addEdge(
                            newNode, prevNode,
                            new STEdge(
                                    suffixArray.get(i - 1) + lcp.get(i),
                                    prevNode.getDescent() - lcp.get(i)
                            )
                    );
                    newLeaf = new STNode(
                            index++,
                            suffixArray.get(i),
                            newNode.getDescent() + textLen - (suffixArray.get(i) + lcp.get(i)),
                            newNode
                    );
                    st.addEdge(
                            newNode, newLeaf,
                            new STEdge(
                                    suffixArray.get(i) + lcp.get(i),
                                    textLen - (suffixArray.get(i) + lcp.get(i))
                            )
                    );
                    st.setRightmostLeaf(newLeaf);
                    break;
                } else {
                    prevNode = rightmostNode;
                    rightmostNode = rightmostNode.getParent();
                }
            }
        }

        return st;
    }

    public static SuffixTree constructSuffixTree(
            String text, List<Integer> suffixArray, List<Integer> lcp
    ) {
        return constructSuffixTreeMachinery(text, suffixArray, lcp);
    }

    public static SuffixTree constructSuffixTree(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        return constructSuffixTreeMachinery(
                strDataset.getFirst(),
                BA9UTIL.parseIntArray(strDataset.get(1), ", "),
                BA9UTIL.parseIntArray(strDataset.getLast(), ", ")
        );
    }

    private void run() {
        SuffixTree st = constructSuffixTree(
                Path.of(
                        "/home/surelye/Downloads/rosalind_files/rosalind_ba9r.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba9r_out.txt")) {
            for (STNode node : st.keySet()) {
                for (STEdge edge : st.get(node).values()) {
                    fileWriter.write("%s\n".formatted(
                            st.getText().substring(
                                    edge.position(),
                                    edge.position() + edge.length()
                            )
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA9R().run();
    }
}
