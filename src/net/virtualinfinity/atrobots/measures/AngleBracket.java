package net.virtualinfinity.atrobots.measures;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * @author Daniel Pitts
 */
public class AngleBracket {
    private final AbsoluteAngle counterClockwiseBound;
    private final double rangeSize;

    private AngleBracket(AbsoluteAngle counterClockwiseBound, AbsoluteAngle clockwiseBound) {
        this.counterClockwiseBound = counterClockwiseBound;
        rangeSize = counterClockwiseBound.getNormalizedRadiansClockwiseTo(clockwiseBound);
    }

    public AngleBracket() {
        counterClockwiseBound = null;
        rangeSize = Math.PI * 2;
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

            public Shape toShape(Distance x, Distance y, Distance radius) {
                final Ellipse2D ellipse2D = new Ellipse2D.Double();
                ellipse2D.setFrameFromCenter(x.getMeters(), y.getMeters(), x.plus(radius).getMeters(), y.plus(radius).getMeters());
                return ellipse2D;
            }
        };


    }

    public boolean contains(AbsoluteAngle angle) {
        return Math.abs(counterClockwiseBound.getNormalizedRadiansClockwiseTo(angle)) <= rangeSize;
    }

    public double fractionTo(AbsoluteAngle angle) {
        return counterClockwiseBound.getAngleCounterClockwiseTo(angle).normalize().getRadians() / rangeSize;
    }

    public AbsoluteAngle randomAngleBetween() {
        return AbsoluteAngle.fromRadians(counterClockwiseBound.getNormalizedRadians() - Math.random() * rangeSize);
    }

    public Shape toShape(Distance x, Distance y, Distance radius) {
        final Arc2D.Double arc = new Arc2D.Double();
        arc.setArcByCenter(x.getMeters(), y.getMeters(), radius.getMeters(), -counterClockwiseBound.getDegrees(),
                RelativeAngle.fromRadians(rangeSize).getDegrees(), Arc2D.PIE);
        return arc;
    }
}
