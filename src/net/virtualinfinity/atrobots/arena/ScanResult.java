package net.virtualinfinity.atrobots.arena;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * @author Daniel Pitts
 */
public class ScanResult {
    private final double distance;
    private final AbsoluteAngle angle;
    private final int accuracy;
    private final Vector matchPositionVector;
    private final int matchTransponderId;
    private final boolean successful;

    public ScanResult(double distance, AbsoluteAngle angle, int accuracy, Vector matchPositionVector, int matchTransponderId) {
        this.distance = distance;
        this.angle = angle;
        this.accuracy = accuracy;
        this.matchPositionVector = matchPositionVector;
        this.matchTransponderId = matchTransponderId;
        this.successful = true;
    }

    public ScanResult() {
        distance = Double.POSITIVE_INFINITY;
        angle = null;
        matchPositionVector = null;
        accuracy = 0;
        matchTransponderId = 0;
        successful = false;
    }


    public double getDistance() {
        return distance;
    }

    public AbsoluteAngle getAngle() {
        return angle;
    }

    public boolean successful() {
        return successful;
    }

    public int getAccuracy() {
        return accuracy;
    }

    Vector getMatchPositionVector() {
        return matchPositionVector;
    }

    public int getMatchTransponderId() {
        return matchTransponderId;
    }
}
