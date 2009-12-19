package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
class PolarVector extends Vector {
    private final Distance magnitude;
    private final AbsoluteAngle angle;

    private PolarVector(Distance magnitude, AbsoluteAngle angle) {
        this.magnitude = magnitude;
        this.angle = angle;
    }

    public Distance getMagnitude() {
        return magnitude;
    }

    public AbsoluteAngle getAngle() {
        return angle;
    }

    public Distance getX() {
        return magnitude.times(angle.cosine());
    }

    public Distance getY() {
        return magnitude.times(angle.sine());
    }

    public static PolarVector createPolar(AbsoluteAngle angle, Distance magnitude) {
        return new PolarVector(magnitude, angle);
    }

    public Area getMagnitudeSquared() {
        return magnitude.times(magnitude);
    }

    public Vector times(double v) {
        return createPolar(angle, magnitude.times(v));
    }
}
