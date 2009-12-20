package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public class AbsoluteAngle {
    private static final AbsoluteAngle[] bygreeTable = new AbsoluteAngle[256];

    static {
        for (int bygrees = 0; bygrees < bygreeTable.length; ++bygrees) {
            final AbsoluteAngle value = AbsoluteAngle.fromRadians(bygreeToRadians(bygrees));
            bygreeTable[bygrees] = new AbsoluateBygreeAngle(bygrees, value);
        }
    }

    protected boolean isExactBygrees() {
        return false;
    }

    private final double radians;

    private AbsoluteAngle(double radians) {
        this.radians = radians;
    }

    public double cosine() {
        return Math.cos(radians);
    }

    public double sine() {
        return Math.sin(radians);
    }

    public AbsoluteAngle counterClockwise(RelativeAngle angle) {
        return fromRadians(getRadians() + angle.getRadians());
    }

    public double getRadians() {
        return radians;
    }

    public double getNormalizedRadians() {
        return Math.atan2(sine(), cosine());
    }

    public Vector toUnitVector() {
        return toVector(1);
    }

    public Vector toVector(double magnitude) {
        return Vector.createPolar(this, magnitude);
    }

    public int getBygrees() {
        return radiansToBygrees(getRadians());
    }

    public AbsoluteAngle clockwise(RelativeAngle angle) {
        return fromRadians(getRadians() - angle.getRadians());
    }

    public byte getSignedBygrees() {
        return (byte) getBygrees();
    }

    public boolean isClockwiseCloser(AbsoluteAngle angle) {
        return getAngleCounterClockwiseTo(angle).compareTo(RelativeAngle.HALF_CIRCLE) < 0;
    }

    public String toString() {
        return getNormalizedRadians() + "r/" + getBygrees();
    }

    public RelativeAngle counterClockwiseFromStandardOrigin() {
        return RelativeAngle.fromRadians(getRadians());
    }

    public double getDegrees() {
        return getRadians() / Math.PI * 180;
    }

    public RelativeAngle getAngleCounterClockwiseTo(AbsoluteAngle counterClockwiseValue) {
        final double difference = getNormalizedRadians() - counterClockwiseValue.getNormalizedRadians();
        return RelativeAngle.fromRadians(difference < 0 ? difference + Math.PI * 2.0 : difference);
    }

    public Vector projectAngle(Vector vector) {
        final Vector unit = toVector(1);
        return unit.times(unit.dot(vector));
    }

    public static AbsoluteAngle fromBygrees(int value) {
        return bygreeTable[value & 255];
    }

    public static AbsoluteAngle fromCartesian(double x, double y) {
        return fromRadians(Math.atan2(y, x));
    }

    public static AbsoluteAngle fromRadians(double radians) {
        return new AbsoluteAngle(radians);
    }

    private static int radiansToBygrees(double radians) {
        return (int) Math.round(64 + (radians * 128 / Math.PI)) & 255;
    }

    private static double bygreeToRadians(int bygrees) {
        return (bygrees - 64) * Math.PI / 128;
    }

    private static class AbsoluateBygreeAngle extends AbsoluteAngle {

        private final double cosine;

        private final double sine;
        private final int bygrees;
        private final byte signedBygrees;
        private final double normalizedRadians;

        public AbsoluateBygreeAngle(int bygrees, AbsoluteAngle template) {
            super(bygreeToRadians(bygrees));
            this.cosine = template.cosine();
            this.sine = template.sine();
            this.bygrees = bygrees;
            this.signedBygrees = template.getSignedBygrees();
            this.normalizedRadians = template.getNormalizedRadians();

        }

        @Override
        public double cosine() {
            return cosine;
        }

        @Override
        public double sine() {
            return sine;
        }

        @Override
        public int getBygrees() {
            return bygrees;
        }

        @Override
        public byte getSignedBygrees() {
            return signedBygrees;
        }

        @Override
        protected boolean isExactBygrees() {
            return true;
        }

        @Override
        public double getNormalizedRadians() {
            return normalizedRadians;
        }
    }
}
