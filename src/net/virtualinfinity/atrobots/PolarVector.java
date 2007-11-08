package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
*/
class PolarVector extends Vector {
    private final Distance magnatude;
    private final Angle angle;

    private PolarVector(Distance magnatude, Angle angle) {
        this.magnatude = magnatude;
        this.angle = angle;
    }

    public Distance getMagnatude() {
        return magnatude;
    }

    public Angle getAngle() {
        return angle;
    }

    public Distance getX() {
        return magnatude.times(angle.cosine());
    }

    public Distance getY() {
        return magnatude.times(angle.sine());
    }

    public static PolarVector createPolar(Angle angle, Distance magnatude) {
        return new PolarVector(magnatude, angle);
    }
}
