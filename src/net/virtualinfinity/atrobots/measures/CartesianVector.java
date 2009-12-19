package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
class CartesianVector extends Vector {
    private final Distance x;
    private final Distance y;

    private CartesianVector(Distance x, Distance y) {
        this.x = x;
        this.y = y;
    }

    public Distance getMagnitude() {
        return getMagnitudeSquared().squareRoot();
    }

    public Area getMagnitudeSquared() {
        return x.times(x).plus(y.times(y));
    }

    public AbsoluteAngle getAngle() {
        return AbsoluteAngle.fromCartesian(x, y);
    }

    public Distance getX() {
        return x;
    }

    public Distance getY() {
        return y;
    }

    public Vector times(double v) {
        return fromCartesian(x.times(v), y.times(v));
    }

    public static Vector fromCartesian(Distance x, Distance y) {
        return new CartesianVector(x, y);
    }
}
