package net.virtualinfinity.atrobots.arena;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class ScanWork {
    private final double maxDistanceSquared;
    private final Position position;
    private final AngleBracket angleBracket;
    private final boolean calculateAccuracy;

    private Vector vectorToClosest = null;
    private double closestDistanceSquared;
    private TangibleArenaObject closest = null;
    private Vector counterClockwiseBound;
    private Vector clockwiseBound;
    private static final int ROBOT_RADIUS_SQUARED = 16;

    public ScanWork(Position position, AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy) {
        this.position = position;
        this.angleBracket = angleBracket;
        this.calculateAccuracy = calculateAccuracy;
        this.counterClockwiseBound = getUnit(angleBracket.getCounterClockwiseBound());
        this.clockwiseBound = getUnit(angleBracket.getClockwiseBound());
        this.maxDistanceSquared = maxDistance * maxDistance;
        this.closestDistanceSquared = maxDistanceSquared;
    }

    private static Vector getUnit(AbsoluteAngle absoluteAngle) {
        if (absoluteAngle == null) {
            return null;
        }
        return absoluteAngle.toUnitVector();
    }

    public void visit(TangibleArenaObject arenaObject) {
        final Vector position = arenaObject.getPosition().getVectorTo(this.position);
        final double distanceSquared = position.getMagnitudeSquared();
        if (distanceSquared < closestDistanceSquared && withinArc(position, distanceSquared)) {
            closest = arenaObject;
            closestDistanceSquared = distanceSquared;
            vectorToClosest = position;
        }
    }

    private boolean withinArc(Vector position, double distanceSquared) {
        return angleBracket.contains(position.getAngle()) || rayIntersection(position, distanceSquared, counterClockwiseBound) || rayIntersection(position, distanceSquared, clockwiseBound);
    }

    private boolean rayIntersection(Vector circleCenter, double distanceSquared, Vector direction) {
        final double a = direction.dot(circleCenter);
        return a > 0 && a * a > distanceSquared - ROBOT_RADIUS_SQUARED;
    }

    public ScanResult toScanResult() {
        if (closest == null || closestDistanceSquared > maxDistanceSquared) {
            return new ScanResult();
        }
        final AbsoluteAngle angleToClosest = vectorToClosest.getAngle();
        final int accuracy;
        if (calculateAccuracy) {
            final double v = 0.5d - angleBracket.fractionTo(angleToClosest);
            if (angleBracket.getRangeSize().getBygrees() <= 4) {
                accuracy = roundAwayFromZero(v * 2) * 2;
            } else {
                accuracy = roundAwayFromZero(v * 4);
            }
        } else {
            accuracy = 0;
        }
        return new ScanResult(Math.sqrt(closestDistanceSquared), angleToClosest, Math.min(2, Math.max(-2, accuracy)), closest.getPosition().getVector(), closest.getTransponder().getId());
    }

    private static int roundAwayFromZero(double value) {
        return (int) (value < 0 ? Math.ceil(value - 0.5d) : Math.floor(value + 0.5d));
    }
}
