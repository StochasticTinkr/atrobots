package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
class PolarVector extends Vector {
    private final Distance magnatude;
    private final AbsoluteAngle angle;

    private PolarVector(Distance magnatude, AbsoluteAngle angle) {
        this.magnatude = magnatude;
        this.angle = angle;
    }

    public Distance getMagnatude() {
        return magnatude;
    }

    public AbsoluteAngle getAngle() {
        return angle;
    }

    public Distance getX() {
        return magnatude.times(angle.cosine());
    }

    public Distance getY() {
        return magnatude.times(angle.sine());
    }

    public static PolarVector createPolar(AbsoluteAngle angle, Distance magnatude) {
        return new PolarVector(magnatude, angle);
    }

    public Area getMagnatudeSquared() {
        return magnatude.times(magnatude);
    }

    public Vector times(double v) {
        return createPolar(angle, magnatude.times(v));
    }
}
