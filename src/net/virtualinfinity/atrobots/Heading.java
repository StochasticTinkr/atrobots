package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heading {
    private Angle angle;

    public Vector times(Distance distance) {
        return getAngle().toVector(distance);
    }

    public Angle getAngle() {
        return angle;
    }

    public PortHandler getCompass() {
        return new PortHandler() {
            public short read() {
                return (short) angle.getBygrees();
            }
        };
    }

    public PortHandler getRotationPort() {
        return new PortHandler() {
            public void write(short value) {
                rotate(Angle.fromBygrees(value));
            }
        };
    }

    private void rotate(Angle angle) {
        setAngle(this.angle.counterClockwise(angle));
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }
}
