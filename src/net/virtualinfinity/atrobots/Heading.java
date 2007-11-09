package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heading {
    private Angle angle = Angle.fromBygrees((int) (Math.random() * 256));

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
                rotate(Angle.fromRelativeBygrees(value));
            }
        };
    }

    private void rotate(Angle angle) {
        setAngle(this.angle.counterClockwise(angle));
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public void moveToward(Heading desiredHeading, Angle maxDelta) {
        if (AngleBracket.around(angle, maxDelta).contains(desiredHeading.getAngle())) {
            angle = desiredHeading.getAngle();
        } else if (angle.clockwiseIsCloserTo(desiredHeading.getAngle())) {
            angle = angle.clockwise(maxDelta);
        } else {
            angle = angle.counterClockwise(maxDelta);
        }

    }

}
