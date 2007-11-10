package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heading {
    private Angle angle = Angle.fromBygrees((int) (Math.random() * 256));
    private boolean absolute = true;
    private Heading relation;

    public Vector times(Distance distance) {
        return getAngle().toVector(distance);
    }

    public Angle getAngle() {
        if (absolute) {
            return angle;
        }
        return angle.counterClockwise(relation.getAngle());
    }

    public PortHandler getCompass() {
        return new PortHandler() {
            public short read() {
                return (short) getAngle().getBygrees();
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
        setAngle(this.getAngle().counterClockwise(angle));
    }

    public void setAngle(Angle angle) {
        if (absolute) {
            this.angle = angle;
            return;
        }
        this.angle = relation.getAngle().clockwise(angle);
    }

    public void moveToward(Heading desiredHeading, Angle maxDelta) {
        if (AngleBracket.around(getAngle(), maxDelta).contains(desiredHeading.getAngle())) {
            setAngle(desiredHeading.getAngle());
        } else if (getAngle().clockwiseIsCloserTo(desiredHeading.getAngle())) {
            setAngle(getAngle().clockwise(maxDelta));
        } else {
            setAngle(getAngle().counterClockwise(maxDelta));
        }

    }

    public void setAbsolute(boolean absolute) {
        Angle angle = getAngle();
        this.absolute = absolute;
        setAngle(angle);
    }

    public void setRelation(Heading relation) {
        this.relation = relation;
    }
}
