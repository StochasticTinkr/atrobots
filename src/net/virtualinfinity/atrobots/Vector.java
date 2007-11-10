package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
abstract class Vector {
    public abstract Distance getMagnatude();

    public abstract AbsoluteAngle getAngle();

    public abstract Distance getX();

    public abstract Distance getY();

    public static Vector createPolar(AbsoluteAngle angle, Distance magnatude) {
        return PolarVector.createPolar(angle, magnatude);
    }

    public static Vector createCartesian(Distance x, Distance y) {
        return CartesianVector.fromCartesian(x, y);
    }

    public Vector add(Vector vector) {
        return createCartesian(getX().plus(vector.getX()), getY().plus(vector.getY()));
    }

    public String toString() {
        return "<" + getAngle() + ", " + getMagnatude() + ">:<" + getX() + ", " + getY() + ">";
    }
}
