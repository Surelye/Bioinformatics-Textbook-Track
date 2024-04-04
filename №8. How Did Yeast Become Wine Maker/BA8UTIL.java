import auxil.Point;

import java.util.List;

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
}
