package net.virtualinfinity.atrobots;

import java.awt.geom.Point2D;

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

    public Vector plus(Vector vector) {
        return createCartesian(getX().plus(vector.getX()), getY().plus(vector.getY()));
    }

    public String toString() {
        return "<" + getAngle() + ", " + getMagnatude() + ">:<" + getX() + ", " + getY() + ">";
    }

    public Point2D toPoint2D() {
        return new Point2D.Double(getX().getMeters(), getY().getMeters());
    }

    public Vector minus(Vector vector) {
        return createCartesian(getX().minus(vector.getX()), getY().minus(vector.getY()));
    }
}
