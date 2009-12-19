package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public class Distance implements Comparable<Distance> {
    private final double meters;

    private Distance(double meters) {
        this.meters = meters;
    }

    public double getMeters() {
        return meters;
    }

    public static Distance fromMeters(double meters) {
        return new Distance(meters);
    }

    public Distance times(double scalar) {
        return fromMeters(getMeters() * scalar);
    }

    public Area times(Distance distance) {
        return Area.fromSquareMeters(getMeters() * distance.getMeters());
    }

    public Distance plus(Distance distance) {
        return fromMeters(getMeters() + distance.getMeters());
    }

    public double getCentimeters() {
        return getMeters() * 100.0;
    }

    public Distance minus(Distance distance) {
        return fromMeters(getMeters() - distance.getMeters());
    }

    public int compareTo(Distance o) {
        return getMeters() < o.getMeters() ? -1 : getMeters() == o.getMeters() ? 0 : 1;
    }

    public static Distance infinity() {
        return fromMeters(Double.POSITIVE_INFINITY);
    }

    public String toString() {
        return meters == Double.POSITIVE_INFINITY ? "infinity" : meters + "m";
    }

    public static Distance unit() {
        return fromMeters(1);
    }

    public Distance negate() {
        return fromMeters(-getMeters());
    }

    public double dividedBy(Distance x) {
        return getMeters() / x.getMeters();
    }
}
