package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class AngleBracket {
    private final Angle counterClockwiseBound;
    private final double rangeSize;

    private AngleBracket(Angle counterClockwiseBound, Angle clockwiseBound) {
        this.counterClockwiseBound = counterClockwiseBound;
        rangeSize = counterClockwiseBound.getNormalizedRadiansClockwiseTo(clockwiseBound);
    }

    public AngleBracket() {
        counterClockwiseBound = null;
        rangeSize = Math.PI * 2;
    }

    static AngleBracket around(Angle center, Angle width) {
        return between(center.counterClockwise(width), center.clockwise(width));
    }

    public static AngleBracket between(Angle counterClockwiseBound, Angle clockwiseBound) {
        return new AngleBracket(counterClockwiseBound, clockwiseBound);
    }

    public static AngleBracket all() {
        return new AngleBracket() {
            @Override
            public boolean contains(Angle angle) {
                return true;
            }
        };
    }

    public boolean contains(Angle angle) {
        return Math.abs(counterClockwiseBound.getNormalizedRadiansClockwiseTo(angle)) <= rangeSize;
    }

    public double fractionTo(Angle angle) {
        return rangeSize / counterClockwiseBound.getNormalizedRadiansClockwiseTo(angle);
    }

    public Angle randomAngleBetween() {
        return Angle.fromRadians(counterClockwiseBound.getNormalizedRadians() + Math.random() * rangeSize);
    }
}
