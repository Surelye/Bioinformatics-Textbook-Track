// Implement FarthestFirstTraversal
// --------------------------------
//
// To think about clustering as dividing data points Data into k clusters, we will try to select a
// set Centers of k points that will serve as the centers of these clusters. We would like to
// choose Centers so that they minimize some distance function between Centers and Data over all
// possible choices of centers. But how should this distance function be defined?
//
// First, we define the Euclidean distance between points v = (v1, ... , vm) and w = (w1, ... , wm)
// in m-dimensional space, denoted d(v, w), as the length of the line segment connecting these
// points, d(v,w)=∑i=1m(vi−wi)2−−−−−−−−−−−√
//
// Next, given a point DataPoint in multi-dimensional space and a set of k points Centers, we define
// the distance from DataPoint to Centers, denoted d(DataPoint, Centers), as the Euclidean distance
// from DataPoint to its closest center,
//
// d(DataPoint,Centers) = minall points x from Centersd(DataPoint, x).
//
// The length of the segments in the figure below correspond to d(DataPoint, Centers) for each point
// DataPoint.
//
// We now define the distance between all data points Data and centers Centers. This distance,
// denoted MaxDistance(Data, Centers), is the maximum of d(DataPoint, Centers) among all data points
// DataPoint,
// MaxDistance(Data, Centers) = maxall points DataPoint from Data d(DataPoints,Centers).
//
// We can now formulate a well-defined clustering problem.
//
// k-Center Clustering Problem: Given a set of data points, find k centers minimizing the maximum
// distance between these data points and centers.
// Input: A set of points Data and an integer k.
// Output: A set Centers of k centers that minimize the distance MaxDistance(DataPoints, Centers)
// over all possible choices of k centers.
//
// Although the k-Center Clustering Problem is easy to state, it is NP-Hard. The Farthest First
// Traversal heuristic, whose pseudocode is shown below, selects centers from the points in Data
// (instead of from all possible points in m-dimensional space). It begins by selecting an arbitrary
// point in Data as the first center and iteratively adds a new center as the point in Data that is
// farthest from the centers chosen so far, with ties broken arbitrarily.
//
// -------------------------------
// FarthestFirstTraversal(Data, k)
//    Centers ← the set consisting of a single randomly chosen point from Data
//       while |Centers| < k
//          DataPoint ← the point in Data maximizing d(DataPoint, Centers)
//          add DataPoint to Centers
//    return Centers
//
// Implement FarthestFirstTraversal
//
// Given: Integers k and m followed by a set of points Data in m-dimensional space.
//
// Return: A set Centers consisting of k points (centers) resulting from applying
// FarthestFirstTraversal(Data, k), where the first point from Data is chosen as the first center to
// initialize the algorithm.
//
// Sample Dataset
// --------------
// 3 2
// 0.0 0.0
// 5.0 5.0
// 0.0 5.0
// 1.0 1.0
// 2.0 2.0
// 3.0 3.0
// 1.0 2.0
// --------------
//
// Sample Output
// -------------
// 0.0 0.0
// 5.0 5.0
// 0.0 5.0
// -------------

import auxil.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class BA8A {

    private static Point getPointMaximizingDistance(List<Point> points, List<Point> centers) {
        Point maximizingPoint = points.getFirst();
        double maxDist = Double.MIN_VALUE, curDist;

        for (Point point : points) {
            curDist = BA8UTIL.getDistanceFromPointToCenters(point, centers);
            if (curDist > maxDist) {
                maximizingPoint = point;
                maxDist = curDist;
            }
        }

        return maximizingPoint;
    }

    private static List<Point> farthestFirstTraversalMachinery(List<Point> points, int k) {
        List<Point> centers = new ArrayList<>(List.of(points.getFirst()));
        points.remove(centers.getLast());

        while (centers.size() < k) {
            Point dataPoint = getPointMaximizingDistance(points, centers);
            centers.add(dataPoint);
            points.remove(centers.getLast());
        }

        return centers;
    }

    public static List<Point> farthestFirstTraversal(List<Point> points, int k) {
        return farthestFirstTraversalMachinery(points, k);
    }

    public List<Point> farthestFirstTraversal(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        int k = Integer.parseInt(strDataset.getFirst().split("\\s+")[0]);
        List<Point> points = new ArrayList<>(strDataset.size());

        for (int i = 1; i != strDataset.size(); ++i) {
            points.add(new Point(strDataset.get(i)));
        }

        return farthestFirstTraversalMachinery(points, k);
    }

    private void run() {
        List<Point> centers = farthestFirstTraversal(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba8a.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba8a_out.txt")) {
            int dimension = centers.getFirst().size();
            for (Point point : centers) {
                for (int i = 0; i != dimension; ++i) {
                    fileWriter.write("%.1f%c".formatted(
                            point.getNth(i),
                            (i == dimension - 1) ? '\n' : ' ')
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA8A().run();

    }
}
