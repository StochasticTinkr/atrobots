package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heading {
    private AbsoluteAngle angle = AbsoluteAngle.fromBygrees((int) (Math.random() * 256));
    private boolean absolute = true;
    private Heading relation;

    public Vector times(Distance distance) {
        return getAngle().toVector(distance);
    }

    public AbsoluteAngle getAngle() {
        if (absolute) {
            return angle;
        }
        return angle.counterClockwise(relation.getAngle().counterClockwiseFromStandardOrigin());
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
                rotate(RelativeAngle.fromBygrees(value));
            }
        };
    }

    void rotate(RelativeAngle angle) {
        this.angle = this.angle.clockwise(angle);
    }

    public void setAngle(AbsoluteAngle angle) {
        if (absolute) {
            this.angle = angle;
            return;
        }
        this.angle = relation.getAngle().clockwise(angle.counterClockwiseFromStandardOrigin());
    }

    public void moveToward(Heading desiredHeading, RelativeAngle maxDelta) {
        if (AngleBracket.around(getAngle(), maxDelta).contains(desiredHeading.getAngle())) {
            setAngle(desiredHeading.getAngle());
        } else if (getAngle().clockwiseIsCloserTo(desiredHeading.getAngle())) {
            setAngle(getAngle().clockwise(maxDelta));
        } else {
            setAngle(getAngle().counterClockwise(maxDelta));
        }

    }

    public void setAbsolute(boolean absolute) {
        AbsoluteAngle angle = getAngle();
        this.absolute = absolute;
        setAngle(angle);
    }

    public void setRelation(Heading relation) {
        this.relation = relation;
    }

    public String toString() {
        return String.valueOf(getAngle());
    }
}
