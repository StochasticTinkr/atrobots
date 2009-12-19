package net.virtualinfinity.atrobots.measures;

import java.awt.geom.Point2D;

/**
 * This is a 2d vector class. Use {@link #createPolar(AbsoluteAngle, Distance)} or
 * {@link #createCartesian(Distance, Distance)} } to create an instance of this class.
 *
 * @author Daniel Pitts
 */
public abstract class Vector {
    /**
     * Get the magnitude of his vector
     *
     * @return the magnitude of this vector
     */
    public abstract Distance getMagnitude();

    /**
     * Get the direction of this vector.
     *
     * @return the theta of this angle.
     */
    public abstract AbsoluteAngle getAngle();

    /**
     * Get the X component of this vector
     *
     * @return the x component.
     */
    public abstract Distance getX();

    /**
     * Get the Y component of this vector
     *
     * @return the y component.
     */
    public abstract Distance getY();

    /**
     * Create a new Vector instance based on polar coordinates
     *
     * @param angle     the direction of the vector
     * @param magnitude the magnitude of the vector
     * @return a vector with the given direction and magnitude
     */
    public static Vector createPolar(AbsoluteAngle angle, Distance magnitude) {
        return PolarVector.createPolar(angle, magnitude);
    }

    /**
     * Create a new Vector instance based on cartesian (rectangular) coordinates
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @return a vector with the given <x,y> components
     */
    public static Vector createCartesian(Distance x, Distance y) {
        return CartesianVector.fromCartesian(x, y);
    }

    /**
     * Add this vector with the given vector
     *
     * @param vector the summand.
     * @return a new vector which is the sum.
     */
    public Vector plus(Vector vector) {
        return createCartesian(getX().plus(vector.getX()), getY().plus(vector.getY()));
    }

    public String toString() {
        return "<" + getAngle() + ", " + getMagnitude() + ">:<" + getX() + ", " + getY() + ">";
    }

    /**
     * Convert this vector to a {@link java.awt.geom.Point2D}
     *
     * @return a point.
     */
    public Point2D toPoint2D() {
        return new Point2D.Double(getX().getMeters(), getY().getMeters());
    }

    /**
     * Subtract the given vector from this one.
     *
     * @param vector the vector to subtract.
     * @return the difference between the vectors.
     */
    public Vector minus(Vector vector) {
        return createCartesian(getX().minus(vector.getX()), getY().minus(vector.getY()));
    }

    /**
     * Calculate the dot product of this vector with another vector.
     *
     * @param vector the other vector.
     * @return the dot product.
     */
    public Area dot(Vector vector) {
        return getX().times(vector.getX()).plus(getY().times(vector.getY()));
    }

    /**
     * Find the intersection of the given line segment with the line that is perpendicular to the given line and goes
     * through the point represented by this vector.
     *
     * @param linePoint     the start of the line segment.
     * @param lineSlope     the angle of the line segment.
     * @param segmentLength the length of the segment
     * @return the point where the segment intersects the line.
     */
    public Vector perpendicularIntersectionFrom(Vector linePoint, AbsoluteAngle lineSlope, Distance segmentLength) {
        final Vector intersectionVector = lineSlope.projectAngle(minus(linePoint));
        if (intersectionVector.getMagnitude().compareTo(segmentLength) < 0)
            return intersectionVector.plus(linePoint);
        else {
            return null;
        }
    }

    /**
     * Scale this vector.
     *
     * @param scalar the scale.
     * @return a vector who's direction is the same as this vector, but whos magnitude is scaled.
     */
    public abstract Vector times(double scalar);

    /**
     * Get the square of the magnitude of this.
     *
     * @return the square of the magnitude.
     */
    public abstract Area getMagnitudeSquared();

    public boolean equals(Object o) {
        if (!(o instanceof Vector)) {
            return false;
        }
        Vector vector = (Vector) o;
        return getX().equals(vector.getX()) && getY().equals(vector.getY());
    }

    /**
     * Project this vector onto the given vector.
     *
     * @param v the vector to be projected upon.
     * @return the vector projection.
     */
    public Vector projectOnto(Vector v) {
        return v.times(v.dot(this).divide(v.getMagnitudeSquared()));
    }
}
