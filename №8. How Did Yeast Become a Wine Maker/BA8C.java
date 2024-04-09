// Implement the Lloyd Algorithm for k-Means Clustering
// ----------------------------------------------------
//
// The Lloyd algorithm is one of the most popular clustering heuristics for the k-Means Clustering
// Problem. It first chooses k arbitrary points Centers from Data as centers and then iteratively
// performs the following two steps:
// 1) Centers to Clusters: After centers have been selected, assign each data point to the cluster
//    corresponding to its nearest center; ties are broken arbitrarily.
// 2) Clusters to Centers: After data points have been assigned to clusters, assign each cluster’s
//    center of gravity to be the cluster’s new center.
//
// We say that the Lloyd algorithm has converged if the centers (and therefore their clusters)
// stop changing between iterations.
//
// Implement the Lloyd algorithm
//
// Given: Integers k and m followed by a set of points Data in m-dimensional space.
//
// Return: A set Centers consisting of k points (centers) resulting from applying the Lloyd
// algorithm to Data and Centers, where the first k points from Data are selected as the first k
// centers.
//
// Sample Dataset
// --------------
// 2 2
// 1.3 1.1
// 1.3 0.2
// 0.6 2.8
// 3.0 3.2
// 1.2 0.7
// 1.4 1.6
// 1.2 1.0
// 1.2 1.1
// 0.6 1.5
// 1.8 2.6
// 1.2 1.3
// 1.2 1.0
// 0.0 1.9
// --------------
//
// Sample Output
// -------------
// 1.800 2.867
// 1.060 1.140
// -------------

import auxil.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BA8C {

    private static int getClosestCenterIndex(Point point, List<Point> centers) {
        double minDist = Double.MAX_VALUE, curDist;
        int index = 0;

        for (int i = 0; i != centers.size(); ++i) {
            curDist = BA8UTIL.EuclideanDistance(point, centers.get(i));
            if (curDist < minDist) {
                index = i;
                minDist = curDist;
            }
        }

        return index;
    }

    private static List<List<Point>> centersToClusters(List<Point> points, List<Point> centers) {
        List<List<Point>> clusters = new ArrayList<>(centers.size());
        for (int i = 0; i != centers.size(); ++i) {
            clusters.add(new ArrayList<>());
        }
        int closestCenterIndex;

        for (Point point : points) {
            closestCenterIndex = getClosestCenterIndex(point, centers);
            clusters.get(closestCenterIndex).add(point);
        }

        return clusters;
    }

    private static Point getCenterOfGravity(List<Point> cluster) {
        int nPoints = cluster.size();
        int m = cluster.getFirst().size();
        List<Double> coordinates = new ArrayList<>(Collections.nCopies(m, 0.));

        for (Point point : cluster) {
            for (int i = 0; i != m; ++i) {
                coordinates.set(i, coordinates.get(i) + point.getNth(i));
            }
        }
        for (int i = 0; i != m; ++i) {
            coordinates.set(i, coordinates.get(i) / nPoints);
        }

        return new Point(coordinates);
    }

    private static List<Point> getCentersOfGravity(List<List<Point>> clusters) {
        List<Point> centersOfGravity = new ArrayList<>(clusters.size());
        Point centerOfGravity;

        for (List<Point> cluster : clusters) {
            centerOfGravity = getCenterOfGravity(cluster);
            centersOfGravity.add(centerOfGravity);
        }

        return centersOfGravity;
    }

    private static List<Point> LloydAlgorithmMachinery(List<Point> points, int k) {
        List<Point> centers = new ArrayList<>(k);
        for (int i = 0; i != k; ++i) {
            centers.add(points.get(i));
        }
        List<List<Point>> clusters;
        List<Point> centersOfGravity;
        boolean updated = true;

        while (updated) {
            updated = false;
            clusters = centersToClusters(points, centers);
            centersOfGravity = getCentersOfGravity(clusters);
            for (int i = 0; i != k; ++i) {
                if (centers.get(i).equals(centersOfGravity.get(i))) {
                    continue;
                }
                centers.set(i, centersOfGravity.get(i));
                updated = true;
            }
        }

        return centers;
    }

    public static List<Point> LloydAlgorithm(List<Point> points, int k) {
        return LloydAlgorithmMachinery(points, k);
    }

    public static List<Point> LloydAlgorithm(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int k = Integer.parseInt(strDataset.getFirst().split("\\s+")[0]);
        List<Point> points = new ArrayList<>(strDataset.size());

        for (int i = 1; i != strDataset.size(); ++i) {
            points.add(new Point(strDataset.get(i)));
        }

        return LloydAlgorithmMachinery(points, k);
    }

    private void run() {
        List<Point> centers = LloydAlgorithm(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba8c.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba8c_out.txt")) {
            int m = centers.getFirst().size();
            for (Point center : centers) {
                for (int i = 0; i != m; ++i) {
                    fileWriter.write("%.3f%c".formatted(
                            center.getNth(i),
                            (i == m - 1) ? '\n' : ' '
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA8C().run();
    }
}
