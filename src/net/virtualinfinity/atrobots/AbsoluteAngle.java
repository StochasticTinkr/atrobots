package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class AbsoluteAngle {
    private final double radians;

    private AbsoluteAngle(double radians) {
        this.radians = radians;
    }

    public double cosine() {
        return Math.cos(radians);
    }

    public double sine() {
        return Math.sin(radians);
    }

    public static AbsoluteAngle fromCartesian(Distance x, Distance y) {
        return fromRadians(Math.atan2(y.getMeters(), x.getMeters()));
    }

    public static AbsoluteAngle fromRadians(double radians) {
        return new AbsoluteAngle(radians);
    }

    public AbsoluteAngle counterClockwise(RelativeAngle angle) {
        return fromRadians(getRadians() + angle.getRadians());
    }

    public double getRadians() {
        return radians;
    }

    public double getNormalizedRadians() {
        return Math.atan2(sine(), cosine());
    }

    public Vector toVector(Distance length) {
        return Vector.createPolar(this, length);
    }

    public int getBygrees() {
        return (int) Math.round(64 - (radians * 128 / Math.PI)) & 255;
    }

    public AbsoluteAngle clockwise(RelativeAngle angle) {
        return fromRadians(getRadians() - angle.getRadians());
    }

    public static AbsoluteAngle fromBygrees(int value) {
        return fromRadians((64 - value) * Math.PI / 128);
    }

    public byte getSignedBygrees() {
        return (byte) getBygrees();
    }

    public double getNormalizedRadiansClockwiseTo(AbsoluteAngle clockwiseBound) {
        final double difference = getNormalizedRadians() - clockwiseBound.getNormalizedRadians();
        return difference < 0 ? difference + Math.PI * 2.0 : difference;
    }

    public RelativeAngle getAngleClockwiseTo(AbsoluteAngle clockwiseValue) {
        final double difference = clockwiseValue.getNormalizedRadians() - getNormalizedRadians();
        return RelativeAngle.fromRadians(difference < 0 ? difference + Math.PI * 2.0 : difference);
    }

    public boolean clockwiseIsCloserTo(AbsoluteAngle angle) {
        return getAngleClockwiseTo(angle).compareTo(RelativeAngle.HALF_CIRCLE) < 0;
    }

    public static RelativeAngle fromRelativeBygrees(int bygrees) {
        return RelativeAngle.fromBygrees(bygrees);
    }

    public String toString() {
        return getNormalizedRadians() + "r/" + getBygrees();
    }

    public RelativeAngle counterClockwiseFromStandardOrigin() {
        return RelativeAngle.fromRadians(getRadians());
    }

    public double getDegrees() {
        return getRadians() / Math.PI * 360;
    }
}
