package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ScanResult {
    private final Robot match;
    private final Distance distance;
    private final AbsoluteAngle angle;
    private AbsoluteAngle heading;
    private int throttle;

    public ScanResult(Robot match, Distance distance, AbsoluteAngle angle) {
        this.match = match;
        this.distance = distance;
        this.angle = angle;
        heading = match.getHeading().getAngle();
        throttle = match.getThrottle().getDesiredPower();
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

    public AbsoluteAngle getAngle() {
        return angle;
    }

    public AbsoluteAngle getHeading() {
        return heading;
    }

    public int getThrottle() {
        return throttle;
    }

    public boolean successful() {
        return match != null;
    }
}
