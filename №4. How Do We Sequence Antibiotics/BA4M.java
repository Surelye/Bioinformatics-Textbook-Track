// Solve the Turnpike Problem
// --------------------------
//
// If A = (a1 = 0, a2, ... , an) is a set of n points on a line segment in increasing order
// (a1 < a2 < · · · < an), then ΔA denotes the collection of all pairwise differences between
// points in A. For example, if A = (0, 2, 4, 7), then ΔA =
// (7, 5, 4, 3, 2, 2, 0, 0, 0, 0, 2, 2, 3, 4, 5, 7).
//
// The following problem asks us to reconstruct A from ΔA.
// ----------------
// Turnpike Problem
//
// Given all pairwise distances between points on a line segment, reconstruct the positions of
// those points.
//
// Given: A collection of integers L.
//
// Return: A set A such that ∆A = L.
//
// Sample Dataset
// --------------
//-10 -8 -7 -6 -5 -4 -3 -3 -2 -2 0 0 0 0 0 2 2 3 3 4 5 6 7 8 10
// --------------
//
// Sample Output
// -------------
// 0 2 4 7 10
// -------------
//
// Source: An Introduction to Bioinformatics Algorithms -- Jones N.C., Pevzner P.A. -- 2004

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BA4M {

    private static int width;

    private static boolean isSubsetEq(List<Integer> left, List<Integer> right) {
        List<Integer> rightCopy = new ArrayList<>(right);
        for (Integer elem : left) {
            if (!rightCopy.remove(elem)) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> getPointSetDistances(int point, Set<Integer> set) {
        List<Integer> distances = new ArrayList<>();
        for (int val : set) {
            distances.add(Math.abs(point - val));
        }
        return distances;
    }

    private static void addResSiteAndRemoveItsDistances(
            int potentialRestrictionSite, Set<Integer> restrictionSites,
            List<Integer> distancesToRemove, List<Integer> distances) {
        restrictionSites.add(potentialRestrictionSite);
        for (Integer distanceToRemove : distancesToRemove) {
            distances.remove(distanceToRemove);
        }
    }

    private static void removeResSiteAndAddItsDistances(
            int falseRestrictionSite, Set<Integer> restrictionSites,
            List<Integer> removedDistances, List<Integer> distances) {
        restrictionSites.remove(falseRestrictionSite);
        distances.addAll(removedDistances);
    }

    private static void place(
            List<Integer> distances, Set<Integer> restrictionSites,
            Set<Set<Integer>> allRestrictionSites) {
        if (distances.isEmpty()) {
            if (!allRestrictionSites.contains(restrictionSites)) {
                allRestrictionSites.add(new HashSet<>(restrictionSites));
            }
            return;
        }
        int rightMost = distances.stream().max(Integer::compare).orElse(-1);
        List<Integer> rightMostLengths = getPointSetDistances(rightMost, restrictionSites);
        if (isSubsetEq(rightMostLengths, distances)) {
            addResSiteAndRemoveItsDistances(rightMost, restrictionSites,
                    rightMostLengths, distances);
            place(distances, restrictionSites, allRestrictionSites);
            removeResSiteAndAddItsDistances(rightMost, restrictionSites,
                    rightMostLengths, distances);
        }
        int leftMost = width - rightMost;
        List<Integer> leftMostLengths = getPointSetDistances(leftMost, restrictionSites);
        if (isSubsetEq(leftMostLengths, distances)) {
            addResSiteAndRemoveItsDistances(leftMost, restrictionSites,
                    leftMostLengths, distances);
            place(distances, restrictionSites, allRestrictionSites);
            removeResSiteAndAddItsDistances(leftMost, restrictionSites,
                    leftMostLengths, distances);
        }
    }

    private static Set<Set<Integer>> partialDigestMachinery(List<Integer> distances) {
        width = distances.getLast();
        distances.removeLast();
        Set<Integer> restrictionSites = new HashSet<>(Set.of(0, width));
        Set<Set<Integer>> allRestrictionSites = new HashSet<>();
        place(distances, restrictionSites, allRestrictionSites);

        try (FileWriter fileWriter = new FileWriter("answer.txt")) {
            for (Set<Integer> resSites : allRestrictionSites) {
                for (int restrictionSite : resSites) {
                    fileWriter.write("%d ".formatted(restrictionSite));
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }

        return allRestrictionSites;
    }

    public static Set<Set<Integer>> partialDigest(Path path) {
        List<Integer> distances = UTIL.parseIntArray(UTIL.readDataset(path).getFirst());

        return partialDigestMachinery(
                new ArrayList<>(distances.subList(
                        distances.lastIndexOf(0) + 1,
                        distances.size()))
        );
    }

    public static Set<Set<Integer>> partialDigest(List<Integer> distances) {
        return partialDigestMachinery(distances);
    }
}
