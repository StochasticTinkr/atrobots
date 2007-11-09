package net.virtualinfinity.atrobots;

import java.util.Random;

/**
 * @author Daniel Pitts
 */
public class Position {
    private Vector vector;
    private Odometer odometer;

    public Distance getX() {
        return vector.getX();
    }

    public Distance getY() {
        return vector.getY();
    }

    public void move(Vector delta) {
        if (odometer != null) {
            odometer.accumulate(delta.getMagnatude());
        }
        vector = vector.add(delta);
    }

    public void setOdometer(Odometer odometer) {
        this.odometer = odometer;
    }

    public void copyFrom(Position source) {
        vector = source.vector;
    }

    public Vector getVectorTo(Position position) {
        return Vector.createCartesian(getX().minus(position.getX()), getY().minus(position.getY()));
    }

    Vector getVector() {
        return vector;
    }

    public static Position random(double lowerX, double lowerY, double higherX, double higherY) {
        final Position position = new Position();
        Random random = new Random();
        position.vector = Vector.createCartesian(
                Distance.fromMeters(lowerX + random.nextDouble() * (higherX - lowerX)),
                Distance.fromMeters(lowerY + random.nextDouble() * (higherY - lowerY)));
        return position;
    }
}
