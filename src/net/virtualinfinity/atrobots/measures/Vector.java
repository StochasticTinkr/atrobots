package net.virtualinfinity.atrobots.measures;

import java.awt.geom.Point2D;

/**
 * @author Daniel Pitts
 */
public abstract class Vector {
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

    public Area dot(Vector vector) {
        return getX().times(vector.getX()).plus(getY().times(vector.getY()));
    }

    public Vector project(Vector vector) {
        return times(dot(vector).divide(getMagnatudeSquared()));
    }

    public Vector perpendicularIntersectionFrom(Vector lineStart, AbsoluteAngle lineAngle, Distance segmentLength) {
        final Vector intersectionVector = lineAngle.projectAngle(minus(lineStart));
        if (intersectionVector.getMagnatude().compareTo(segmentLength) < 0)
            return intersectionVector.plus(lineStart);
        else {
            return null;
        }
    }

    public abstract Vector times(double v);

    public abstract Area getMagnatudeSquared();

    public boolean equals(Object o) {
        if (!(o instanceof Vector)) {
            return false;
        }
        Vector vector = (Vector) o;
        return getX().equals(vector.getX()) && getY().equals(vector.getY());
    }
}
