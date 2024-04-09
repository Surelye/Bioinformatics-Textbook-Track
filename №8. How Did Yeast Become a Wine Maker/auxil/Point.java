package auxil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Point {

    private final double[] coordinates;
    private final int dimension;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
        this.dimension = coordinates.length;
    }

    public Point(List<Double> coordinates) {
        this.coordinates = new double[coordinates.size()];
        this.dimension = coordinates.size();
        for (int i = 0; i != dimension; ++i) {
            this.coordinates[i] = coordinates.get(i);
        }
    }

    public Point(String strDoubleCoords) {
        double[] coordinates = Arrays.stream(strDoubleCoords.split("\\s"))
                .mapToDouble(Double::parseDouble)
                .toArray();
        this.coordinates = coordinates;
        this.dimension = coordinates.length;
    }

    public double getNth(int n) {
        return coordinates[n];
    }

    public int size() {
        return dimension;
    }

    @Override
    public String toString() {
        return "Point{coords={%s}}".formatted(Arrays.toString(coordinates));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        if (size() != point.size()) {
            return false;
        }
        for (int i = 0; i != size(); ++i) {
            if (!(Math.abs(getNth(i) - point.getNth(i)) < 10e-6)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimension);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }
}
