package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public class RelativeAngle implements Comparable<RelativeAngle> {
    public static final RelativeAngle HALF_CIRCLE = RelativeAngle.fromRadians(Math.PI);
    private final double radians;

    private RelativeAngle(double radians) {
        this.radians = radians;
    }

    private double cosine() {
        return Math.cos(radians);
    }

    private double sine() {
        return Math.sin(radians);
    }

    public static RelativeAngle fromRadians(double radians) {
        return new RelativeAngle(radians);
    }

    public RelativeAngle plus(RelativeAngle angle) {
        return fromRadians(getRadians() + angle.getRadians());
    }

    public double getRadians() {
        return radians;
    }

    public RelativeAngle normalize() {
        return fromRadians(Math.atan2(sine(), cosine()));
    }

    public int getBygrees() {
        return (int) Math.round(radians * 128 / Math.PI) & 255;
    }

    public static RelativeAngle fromBygrees(int value) {
        return fromRadians(value * Math.PI / 128);
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

    public double getDegrees() {
        return getRadians() * 180 / Math.PI;
    }
}