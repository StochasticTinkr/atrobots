package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class RelativeAngle implements Comparable<RelativeAngle> {
    public static final RelativeAngle HALF_CIRCLE = RelativeAngle.fromCounterClockwiseRadians(Math.PI);
    private final double radians;

    private RelativeAngle(double radians) {
        this.radians = radians;
    }

    public double cosine() {
        return Math.cos(radians);
    }

    public double sine() {
        return Math.sin(radians);
    }

    public static RelativeAngle fromCounterClockwiseRadians(double radians) {
        return new RelativeAngle(radians);
    }

    public RelativeAngle plus(RelativeAngle angle) {
        return fromCounterClockwiseRadians(getRadians() + angle.getRadians());
    }

    public double getRadians() {
        return radians;
    }

    public RelativeAngle normalize() {
        return fromCounterClockwiseRadians(Math.atan2(sine(), cosine()));
    }

    public int getBygrees() {
        return (int) Math.round(radians * 128 / Math.PI) & 255;
    }

    public static RelativeAngle fromCounterClockwiseBygrees(int value) {
        return fromCounterClockwiseRadians(value * Math.PI / 128);
    }

    public byte getSignedBygrees() {
        return (byte) getBygrees();
    }

    public String toString() {
        return getRadians() + "r/" + getBygrees();
    }

    public int compareTo(RelativeAngle angle) {
        return Double.valueOf(getRadians()).compareTo(angle.getRadians());
    }
}