// Implement the Soft k-Means Clustering Algorithm
// -----------------------------------------------
//
// The soft k-means clustering algorithm starts from randomly chosen centers and iterates the
// following two steps:
// 1) Centers to Soft Clusters (E-step): After centers have been selected, assign each data point a
//    “responsibility” value for each cluster, where higher values correspond to stronger cluster
//    membership.
// 2) Soft Clusters to Centers (M-step): After data points have been assigned to soft clusters,
//    compute new centers.
//
// We begin with the “Centers to Soft Clusters” step. If we think about the centers as stars and
// the data points as planets, then the closer a point is to a center, the stronger that center’s
// “pull” should be on the point. Given k centers Centers = (x1, ..., xk) and n points
// Data = (Data1, ... , Datan), we therefore need to construct a k × n responsibility matrix
// HiddenMatrix for which HiddenMatrixi,j is the pull of center i on data point j. This pull can be
// computed according to the Newtonian inverse-square law of gravitation,
//
// HiddenMatrixi,j=1/d(Dataj,xi)2∑all centers xi1/d(Dataj,xi)2
//
// Unfortunately for Newton fans, the following partition function from statistical physics often
// works better in practice:
//
// HiddenMatrixi,j=e−β⋅d(Dataj,xi)∑all centers xie−β⋅d(Dataj,xi)
//
// In this formula, e is the base of the natural logarithm (e ≈ 2.72), and β is a parameter
// reflecting the amount of flexibility in our soft assignment and called — appropriately enough —
// the stiffness parameter.
//
// In soft k-means clustering, if we let HiddenMatrixi denote the i-th row of HiddenMatrix, then we
// can update center xi using an analogue of the above formulas. Specifically, we will define the
// j-th coordinate of center xi, denoted xi, j, as
//
// xi,j=HiddenMatrixi⋅DatajHiddenMatrixi⋅1→
//
// Here, Dataj is the n-dimensional vector holding the j-th coordinates of the n points in Data.
//
// The updated center xi is called a weighted center of gravity of the points Data.
//
// Implement the Soft k-Means Clustering Algorithm
//
// Given: Integers k and m, followed by a stiffness parameter β, followed by a set of points Data
// in m-dimensional space.
//
// Return: A set Centers consisting of k points (centers) resulting from applying the soft k-means
// clustering algorithm. Select the first k points from Data as the first centers for the algorithm
// and run the algorithm for 100 steps. Results should be accurate up to three decimal places.
//
// Sample Dataset
// --------------
// 2 2
// 2.7
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
// 1.662 2.623
// 1.075 1.148
// -------------

import auxil.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class BA8D {

    private static double
    computeHiddenMatrixDenom(List<List<Double>> distances, double beta, int i, int j) {
        double denom = 0;

        for (int iPrime = 0; iPrime != distances.size(); ++iPrime) {
            if (iPrime == i) {
                continue;
            }
            denom += Math.exp(-beta * distances.get(iPrime).get(j));
        }

        return denom;
    }

    private static List<List<Double>>
    centersToSoftClusters(List<Point> centers, List<Point> points, double beta) {
        int nCenters = centers.size(), nPoints = points.size();
        double numer, denom;
        List<List<Double>> mutualDistances = BA8UTIL.getMutualDistance(centers, points);
        List<List<Double>> hiddenMatrix = new ArrayList<>(nCenters);
        for (int i = 0; i != nCenters; ++i) {
            hiddenMatrix.add(new ArrayList<>(nPoints));
        }

        for (int i = 0; i != nCenters; ++i) {
            for (int j = 0; j != nPoints; ++j) {
                numer = Math.exp(-beta * mutualDistances.get(i).get(j));
                denom = numer + computeHiddenMatrixDenom(mutualDistances, beta, i, j);
                hiddenMatrix.get(i).add(numer / denom);
            }
        }

        return hiddenMatrix;
    }

    private static double dot(List<List<Double>> hiddenMatrix, int i, List<Point> data, int j) {
        return BA8UTIL.dot(
                hiddenMatrix.get(i),
                data.stream()
                        .map(point -> point.getNth(j))
                        .toList()
        );
    }

    private static List<Point>
    softClustersToCenters(List<Point> data, List<List<Double>> hiddenMatrix) {
        int k = hiddenMatrix.size(), m = data.getFirst().size();
        double sumHMRow, coordinate;
        List<Double> coordinates;
        List<Point> centers = new ArrayList<>(k);

        for (int i = 0; i != k; ++i) {
            coordinates = new ArrayList<>(m);
            sumHMRow = hiddenMatrix.get(i)
                    .stream()
                    .mapToDouble(x -> x)
                    .sum();
            for (int j = 0; j != m; ++j) {
                coordinate = dot(hiddenMatrix, i, data, j);
                coordinates.add(coordinate / sumHMRow);
            }
            centers.add(new Point(coordinates));
        }

        return centers;
    }

    private static List<Point> softKMeansClusteringMachinery(List<Point> points, int k, double beta) {
        final int NUM_STEPS = 100;
        List<Point> centers = new ArrayList<>(k);
        for (int i = 0; i != k; ++i) {
            centers.add(points.get(i));
        }
        List<List<Double>> hiddenMatrix;

        for (int i = 0; i != NUM_STEPS; ++i) {
            hiddenMatrix = centersToSoftClusters(centers, points, beta);
            centers = softClustersToCenters(points, hiddenMatrix);
        }

        return centers;
    }

    public static List<Point> softKMeansClustering(List<Point> points, int k, double beta) {
        return softKMeansClusteringMachinery(points, k, beta);
    }

    public static List<Point> softKMeansClustering(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int k = Integer.parseInt(strDataset.getFirst().split("\\s+")[0]);
        double beta = Double.parseDouble(strDataset.get(1));
        List<Point> points = new ArrayList<>(strDataset.size());

        for (int i = 2; i != strDataset.size(); ++i) {
            points.add(new Point(strDataset.get(i)));
        }

        return softKMeansClusteringMachinery(points, k, beta);
    }

    private void run() {
        List<Point> centers = softKMeansClustering(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba8d.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba8d_out.txt")) {
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
        new BA8D().run();
    }
}
