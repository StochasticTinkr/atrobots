package net.virtualinfinity.atrobots.simulation.arena;


/**
 * @author Daniel Pitts
 */
public class Odometer implements Resetable {
    private double distance = (0);

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void accumulate(double distance) {
        this.distance += distance;
    }

    public void reset() {
        distance = (0);
    }

    public double getDistance() {
        return distance;
    }
}
