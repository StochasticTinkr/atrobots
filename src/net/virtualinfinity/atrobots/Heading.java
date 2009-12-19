package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * @author Daniel Pitts
 */
public class Heading {
    private AbsoluteAngle absoluteAngle = AbsoluteAngle.fromBygrees((int) (Math.random() * 256));
    private RelativeAngle relativeAngle = RelativeAngle.fromBygrees(0);
    private boolean absolute = true;
    private Heading relation;

    public Vector times(double distance) {
        return getAngle().toVector(distance);
    }

    public AbsoluteAngle getAngle() {
        if (absolute) {
            return absoluteAngle;
        }
        return relation.getAngle().clockwise(relativeAngle);
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
        setAngle(getAngle().counterClockwise(angle));
    }

    public void setAngle(AbsoluteAngle angle) {
        if (absolute) {
            this.absoluteAngle = angle;
            return;
        }
        this.relativeAngle = relation.getAngle().getAngleCounterClockwiseTo(angle);
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
