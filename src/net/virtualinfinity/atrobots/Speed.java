package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Speed {
    private DistanceOverTime distanceOverTime = new DistanceOverTime(Distance.fromMeters(0), Duration.ONE_CYCLE);

    public Distance times(Duration duration) {
        return distanceOverTime.times(duration);
    }

    public void setDistanceOverTime(Distance distance, Duration duration) {
        distanceOverTime = new DistanceOverTime(distance, duration);
    }

    public String toString() {
        return distanceOverTime.toString();
    }

}
