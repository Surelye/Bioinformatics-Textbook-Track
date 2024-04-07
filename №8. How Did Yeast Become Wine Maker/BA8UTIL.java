import auxil.Point;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class BA8UTIL {

    public static double EuclideanDistance(Point v, Point w) {
        assert v.size() == w.size() : "Points must have equal dimensions";
        int dimension = v.size();
        double distance = 0, diff;

        for (int i = 0; i != dimension; ++i) {
            diff = v.getNth(i) - w.getNth(i);
            distance += diff * diff;
        }

        return Math.sqrt(distance);
    }

    public static double getDistanceFromPointToCenters(Point point, List<Point> centers) {
        double pointDist = Double.MAX_VALUE;

        for (Point center : centers) {
            pointDist = Math.min(BA8UTIL.EuclideanDistance(point, center), pointDist);
        }

        return pointDist;
    }

    public static List<List<Double>> getMutualDistance(List<Point> centers, List<Point> points) {
        int nCenters = centers.size(), nPoints = points.size();
        List<List<Double>> mutualDistances = new ArrayList<>(nCenters);
        for (int i = 0; i != nCenters; ++i) {
            mutualDistances.add(new ArrayList<>(nPoints));
        }

        for (int i = 0; i != nCenters; ++i) {
            for (int j = 0; j != nPoints; ++j) {
                mutualDistances.get(i).add(EuclideanDistance(centers.get(i), points.get(j)));
            }
        }

        return mutualDistances;
    }

    public static double dot(List<Double> lhs, List<Double> rhs) {
        assert lhs.size() == rhs.size();
        return IntStream.range(0, lhs.size())
                .mapToDouble(i -> lhs.get(i) * rhs.get(i))
                .sum();
    }
}
