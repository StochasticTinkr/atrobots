package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * @author Daniel Pitts
 */
public class ScanResult {
    private final Robot match;
    private final double distance;
    private final AbsoluteAngle angle;
    private final AbsoluteAngle heading;
    private final int throttle;

    public ScanResult(Robot match, double distance, AbsoluteAngle angle) {
        this.match = match;
        this.distance = distance;
        this.angle = angle;
        heading = match.getHeading().getAngle();
        throttle = match.getThrottle().getDesiredPower();
    }

    public ScanResult() {
        distance = Double.POSITIVE_INFINITY;
        match = null;
        angle = null;
        heading = null;
        throttle = 0;
    }

    public Robot getMatch() {
        return match;
    }

    public double getDistance() {
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

    Vector getMatchPositionVector() {
        return successful() ? getMatch().getPosition().getVector() : null;
    }
}
