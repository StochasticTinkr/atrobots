package net.virtualinfinity.atrobots.measures;

/**
 * Represents a physical distance.
 *
 * @author Daniel Pitts
 */
public class Distance implements Comparable<Distance> {
    private final double meters;

    private Distance(double meters) {
        this.meters = meters;
    }

    /**
     * Get the distance in meters.
     *
     * @return the distance.
     */
    public double getMeters() {
        return meters;
    }

    /**
     * Create a distance from meters.
     *
     * @param meters the distance in meters.
     * @return a Distance.
     */
    public static Distance fromMeters(double meters) {
        return new Distance(meters);
    }

    /**
     * Get a distance which is this distance scaled by the given factor.
     *
     * @param scalar the factor by which to scale this distance.
     * @return this distance scaled by the given factor.
     */
    public Distance times(double scalar) {
        return fromMeters(getMeters() * scalar);
    }

    /**
     * Get an area which is formed by a rectangle with one side of this distance and another side of the given distance.
     *
     * @param distance the other side of the area.
     * @return an area.
     */
    public Area times(Distance distance) {
        return Area.fromSquareMeters(getMeters() * distance.getMeters());
    }

    /**
     * Find the sum of this distance with the given distance.
     *
     * @param distance the summand
     * @return the sum.
     */
    public Distance plus(Distance distance) {
        return fromMeters(getMeters() + distance.getMeters());
    }

    /**
     * Get this distance in centimeters.
     *
     * @return the number of centimeters represented by this distance.
     */
    public double getCentimeters() {
        return getMeters() * 100.0;
    }

    /**
     * Find the different between this distance and the given distance
     *
     * @param distance the other distance
     * @return the difference.
     */
    public Distance minus(Distance distance) {
        return fromMeters(getMeters() - distance.getMeters());
    }

    /**
     * Compares the distances
     *
     * @param distance other distance
     * @return a number which can be compared with zero to determine the comparison result of the comparison of
     *         these distances.
     */
    public int compareTo(Distance distance) {
        return Double.compare(getMeters(), distance.getMeters());
    }

    /**
     * Get an infinity distance.
     *
     * @return an infinity distance
     */
    public static Distance infinity() {
        return fromMeters(Double.POSITIVE_INFINITY);
    }

    public String toString() {
        return isInfinite() ? "infinity" : meters + "m";
    }

    /**
     * Tests if this distance is infinite.
     *
     * @return true if this distance is positive infinity.
     */
    public boolean isInfinite() {
        return meters == Double.POSITIVE_INFINITY;
    }

    /**
     * Get a distance of one meter.
     *
     * @return 1 meter.
     */
    public static Distance unit() {
        return fromMeters(1);
    }

    /**
     * Get the negative of this distance.
     *
     * @return the negative of this distance.
     */
    public Distance negate() {
        return fromMeters(-getMeters());
    }

    /**
     * divide the distances.
     *
     * @param distance the distance
     * @return the scale.
     */
    public double dividedBy(Distance distance) {
        return getMeters() / distance.getMeters();
    }
}
