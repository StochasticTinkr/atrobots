package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
abstract class Vector {
    public abstract Distance getMagnatude();

    public abstract Angle getAngle();

    public abstract Distance getX();

    public abstract Distance getY();

    public static Vector createPolar(Angle angle, Distance magnatude) {
        return PolarVector.createPolar(angle, magnatude);
    }

    public static Vector createCartesian(Distance x, Distance y) {
        return CartesianVector.fromCartesian(x, y);
    }

    public Vector add(Vector vector) {
        return createCartesian(getX().plus(vector.getX()), getY().plus(vector.getY()));
    }

    public Vector rotate(Angle angle) {
        return createPolar(angle.counterClockwise(angle), getMagnatude());
    }

    public String toString() {
        return "<" + getAngle() + ", " + getMagnatude() + ">:<" + getX() + ", " + getY() + ">";
    }
}
