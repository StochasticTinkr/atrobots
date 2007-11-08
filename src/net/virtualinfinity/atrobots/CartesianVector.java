package net.virtualinfinity.atrobots;

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

    public Angle getAngle() {
        return Angle.fromCartesian(x, y);
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
