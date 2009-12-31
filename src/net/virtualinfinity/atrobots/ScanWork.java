package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * TODO: Describe this class.
 *
 * @author <a href="mailto:daniel.pitts@cbs.com">Daniel Pitts</a>
 */
public class ScanWork {
    private final double maxDistanceSquared;
    private final Position position;
    private final AngleBracket angleBracket;
    private final boolean calculateAccuracy;

    private Vector vectorToClosest = null;
    private double closestDistanceSquared;
    private Robot closest = null;

    public ScanWork(Position position, AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy) {
        this.position = position;
        this.angleBracket = angleBracket;
        this.calculateAccuracy = calculateAccuracy;
        maxDistanceSquared = maxDistance * maxDistance;
        closestDistanceSquared = maxDistanceSquared;
    }

    public void visit(Robot arenaObject) {
        final Vector vector = arenaObject.getPosition().getVectorTo(position);
        final double distanceSquared = vector.getMagnitudeSquared();
        if (distanceSquared < closestDistanceSquared && angleBracket.contains(vector.getAngle())) {
            closest = arenaObject;
            closestDistanceSquared = distanceSquared;
            vectorToClosest = vector;
        }
    }

    public ScanResult toScanResult() {
        if (closest != null && closestDistanceSquared <= maxDistanceSquared) {
            final AbsoluteAngle angleToClosest = vectorToClosest.getAngle();
            final int accuracy;
            if (calculateAccuracy) {
                final double v = 0.5d - angleBracket.fractionTo(angleToClosest);
                if (angleBracket.getRangeSize().getBygrees() < 2) {
                    accuracy = roundAwayFromZero(v * 2) * 2;
                } else {
                    accuracy = roundAwayFromZero(v * 4);
                }
            } else {
                accuracy = 0;
            }
            return new ScanResult(closest, Math.sqrt(closestDistanceSquared), angleToClosest, accuracy);
        }
        return new ScanResult();
    }

    private static int roundAwayFromZero(double value) {
        return (int) (value < 0 ? Math.ceil(value - 0.5d) : Math.floor(value + 0.5d));
    }
}
