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

import java.nio.file.Path;
import java.util.List;

public class BA8D {

    private static List<Point> softKMeansClusteringMachinery() {
        
    }

    public static List<Point> softKMeansClustering() {

    }

    public static List<Point> softKMeansClustering(Path path) {

    }

    private void run() {

    }

    public static void main(String[] args) {

    }
}
