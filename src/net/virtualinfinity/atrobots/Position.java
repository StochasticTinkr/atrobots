package net.virtualinfinity.atrobots;

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
}
