package net.virtualinfinity.atrobots;

import javax.management.Query;

/**
 * @author Daniel Pitts
 */
public final class Angle {
    private final double radians;

    private Angle(double radians) {
        this.radians = radians;
    }

    public double cosine() {
        return Math.cos(radians);
    }

    public double sine() {
        return Math.sin(radians);
    }

    public static Angle fromCartesian(Distance x, Distance y) {
        return fromRadians(Math.atan2(y.getMeters(), x.getMeters()));
    }

    public static Angle fromRadians(double radians) {
        return new Angle(radians);
    }

    public Angle counterClockwise(Angle angle) {
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
        return (int)Math.round(64 - (radians * 128 / Math.PI)) & 255;
    }

    public Angle clockwise(Angle angle) {
        return fromRadians(getRadians() - angle.getRadians());
    }

    public static Angle fromBygrees(int value) {
        return fromRadians((64 - value) * Math.PI / 128);
    }

    public byte getSignedBygrees() {
        return (byte)getBygrees();
    }

    public double getNormalizedRadiansClockwiseTo(Angle clockwiseBound) {
        final double difference = getNormalizedRadians() - clockwiseBound.getNormalizedRadians();
        return difference < 0 ? difference + Math.PI * 2.0 : difference;
    }
}
