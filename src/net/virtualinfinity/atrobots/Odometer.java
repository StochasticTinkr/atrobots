package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Odometer implements Resetable {
    private Distance distance = Distance.fromMeters(0);

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void accumulate(Distance distance) {
        this.distance = distance.plus(distance);
    }

    public void reset() {
        distance = Distance.fromMeters(0);
    }

    public Distance getDistance() {
        return distance;
    }
}
