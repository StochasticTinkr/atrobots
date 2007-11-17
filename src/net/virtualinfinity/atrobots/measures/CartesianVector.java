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

    public Distance getMagnatude() {
        return x.times(x).plus(y.times(y)).squareRoot();
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

    public static Vector fromCartesian(Distance x, Distance y) {
        return new CartesianVector(x, y);
    }
}