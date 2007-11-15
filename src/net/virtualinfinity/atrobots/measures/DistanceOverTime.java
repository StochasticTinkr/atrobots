package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public final class DistanceOverTime {
    private final Distance distance;
    private final Duration duration;

    public DistanceOverTime(Distance distance, Duration duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public Distance times(Duration duration) {
        return distance.times(this.duration.divided(duration));
    }

    public String toString() {
        return distance + "/" + duration;
    }
}
