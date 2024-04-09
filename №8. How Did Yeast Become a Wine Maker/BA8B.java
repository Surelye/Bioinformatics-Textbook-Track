// Compute the Squared Error Distortion
// ------------------------------------
//
// FarthestFirstTraversal (which we introduced in “Implement FarthestFirstTraversal”) is fast, and
// its solution approximates the optimal solution of the k-Center Clustering Problem; however, this
// algorithm is rarely used for gene expression analysis. In k-Center Clustering, we selected
// Centers so that these points would minimize MaxDistance(Data, Centers), the maximum distance
// between any point in Data and its nearest center. But biologists are usually interested in
// analyzing typical rather than maximum deviations, since the latter may correspond to outliers
// representing experimental errors.
//
// To address limitations of MaxDistance, we will introduce a new scoring function. Given a set
// Data of n data points and a set Centers of k centers, the squared error distortion of Data and
// Centers, denoted Distortion(Data, Centers), is defined as the mean squared distance from each
// data point to its nearest center,
//
// Distortion(Data,Centers) = (1/n) ∑all points DataPoint in Data d(DataPoint, Centers)^2.
//
// Squared Error Distortion Problem
//
// Given: Integers k and m, followed by a set of centers Centers and a set of points Data.
//
// Return: The squared error distortion Distortion(Data, Centers).
//
// Sample Dataset
// --------------
// 2 2
// 2.31 4.55
// 5.96 9.08
// --------
// 3.42 6.03
// 6.23 8.25
// 4.76 1.64
// 4.47 4.33
// 3.95 7.61
// 8.93 2.97
// 9.74 4.03
// 1.73 1.28
// 9.72 5.01
// 7.27 3.77
// --------------
//
// Sample Output
// -------------
// 18.246
// -------------

import auxil.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BA8B {

    private double computeSquaredErrorDistortionMachinery(List<Point> points, List<Point> centers) {
        int nPoints = points.size();
        double squaredErrorDistortion = 0, dist;

        for (Point point : points) {
            dist = BA8UTIL.getDistanceFromPointToCenters(point, centers);
            squaredErrorDistortion += dist * dist;
        }

        return squaredErrorDistortion / nPoints;
    }

    public double computeSquaredErrorDistortion(List<Point> points, List<Point> centers) {
        return computeSquaredErrorDistortionMachinery(points, centers);
    }

    public double computeSquaredErrorDistortion(Path path) {
        List<String> strDataset = UTIL.readDataset(path);
        String strPoint;
        List<Point> points = new ArrayList<>(strDataset.size());
        List<Point> centers = new ArrayList<>();
        int i;

        for (i = 1; ; ++i) {
            strPoint = strDataset.get(i);
            if (strPoint.startsWith("-")) {
                ++i;
                break;
            }
            centers.add(new Point(strPoint));
        }
        for ( ; i != strDataset.size(); ++i) {
            points.add(new Point(strDataset.get(i)));
        }

        return computeSquaredErrorDistortionMachinery(points, centers);
    }

    private void run() {
        double squaredErrorDistortion = computeSquaredErrorDistortion(
                Path.of(
                        "C:\\Users\\sgnot\\Downloads\\rosalind_ba8b_test.txt"
                )
        );

        try (FileWriter fileWriter = new FileWriter("ba8b_out.txt")) {
            fileWriter.write("%.3f\n".formatted(squaredErrorDistortion));
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public static void main(String[] args) {
        new BA8B().run();
    }
}
