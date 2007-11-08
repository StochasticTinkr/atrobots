package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ScanResult {
    private final Robot match;
    private final Distance distance;
    private final Angle angle;
    private Angle heading;
    private int throttle;

    public ScanResult(Robot match, Distance distance, Angle angle) {
        this.match = match;
        this.distance = distance;
        this.angle = angle;
        heading = match.getHeading().getAngle();
        throttle = match.getThrottle().getPower();
    }

    public ScanResult() {
        match = null;
        distance = null;
        angle = null;
    }

    public Robot getMatch() {
        return match;
    }

    public Distance getDistance() {
        return distance;
    }

    public Angle getAngle() {
        return angle;
    }

    public Angle getHeading() {
        return heading;
    }

    public int getThrottle() {
        return throttle;
    }

    public boolean successful() {
        return match != null;
    }
}
