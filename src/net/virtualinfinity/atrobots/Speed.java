package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Speed {
    private DistanceOverTime distanceOverTime;

    public Distance times(Duration duration) {
        return distanceOverTime.times(duration);
    }
}
