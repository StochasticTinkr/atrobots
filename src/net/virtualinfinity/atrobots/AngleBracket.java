package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Arc2D;

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

    static AngleBracket around(AbsoluteAngle center, RelativeAngle width) {
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
        };
    }

    public boolean contains(AbsoluteAngle angle) {
        return Math.abs(counterClockwiseBound.getNormalizedRadiansClockwiseTo(angle)) <= rangeSize;
    }

    public double fractionTo(AbsoluteAngle angle) {
        return rangeSize / counterClockwiseBound.getNormalizedRadiansClockwiseTo(angle);
    }

    public AbsoluteAngle randomAngleBetween() {
        return AbsoluteAngle.fromRadians(counterClockwiseBound.getNormalizedRadians() + Math.random() * rangeSize);
    }

    public Shape toShape(Distance x, Distance y, Distance radius) {
        final Arc2D.Double arc = new Arc2D.Double();
        arc.setArcByCenter(x.getMeters(), y.getMeters(), radius.getMeters(), counterClockwiseBound.getDegrees(),
                RelativeAngle.fromCounterClockwiseRadians(rangeSize).getDegrees(), Arc2D.PIE);
        return arc;
    }
}
