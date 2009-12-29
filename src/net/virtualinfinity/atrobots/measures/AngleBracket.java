package net.virtualinfinity.atrobots.measures;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * @author Daniel Pitts
 */
public class AngleBracket {
    private final AbsoluteAngle counterClockwiseBound;
    private final RelativeAngle rangeSize;

    private AngleBracket(AbsoluteAngle counterClockwiseBound, AbsoluteAngle clockwiseBound) {
        this.counterClockwiseBound = counterClockwiseBound;
        rangeSize = counterClockwiseBound.getAngleCounterClockwiseTo(clockwiseBound);
    }

    public AngleBracket() {
        counterClockwiseBound = null;
        rangeSize = RelativeAngle.FULL_CIRCLE;
    }

    public static AngleBracket around(AbsoluteAngle center, RelativeAngle width) {
        return between(center.counterClockwise(width), center.clockwise(width));
    }

    public static AngleBracket between(AbsoluteAngle counterClockwiseBound, AbsoluteAngle clockwiseBound) {
        return new AngleBracket(counterClockwiseBound, clockwiseBound);
    }

    public static AngleBracket all() {
        return new AngleBracket() {
            @Override
            public boolean contains(AbsoluteAngle angle) {
                return true;
            }

            @Override

            public Shape toShape(double x, double y, double radius) {
                final Ellipse2D ellipse2D = new Ellipse2D.Double();
                ellipse2D.setFrameFromCenter(x, y, x + (radius), y + (radius));
                return ellipse2D;
            }
        };
    }

    public boolean contains(AbsoluteAngle angle) {
        return counterClockwiseBound.getAngleCounterClockwiseTo(angle).compareTo(rangeSize) <= 0;
    }

    public double fractionTo(AbsoluteAngle angle) {
        return counterClockwiseBound.getAngleCounterClockwiseTo(angle).normalize().dividedBy(rangeSize);
    }

    public AbsoluteAngle randomAngleBetween() {
        return AbsoluteAngle.fromRadians(counterClockwiseBound.getNormalizedRadians() - Math.random() * rangeSize.getRadians());
    }

    public Shape toShape(double x, double y, double radius) {
        final Arc2D.Double arc = new Arc2D.Double();
        arc.setArcByCenter(x, y, radius, -counterClockwiseBound.getDegrees(),
                rangeSize.getDegrees(), Arc2D.PIE);
        return arc;
    }

    public RelativeAngle getRangeSize() {
        return rangeSize;
    }
}
