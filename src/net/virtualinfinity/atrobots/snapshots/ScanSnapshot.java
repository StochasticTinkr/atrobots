package net.virtualinfinity.atrobots.snapshots;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Vector;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public class ScanSnapshot extends ArenaObjectSnapshot {
    private final AngleBracket angleBracket;
    private final double maxDistance;
    private Vector matchVector;
    private boolean successful;

    public ScanSnapshot(AngleBracket angleBracket, double maxDistance, boolean successful, Vector matchVector) {
        this.angleBracket = angleBracket;
        this.maxDistance = maxDistance;
        this.setSuccessful(successful);
        this.setMatchVector(matchVector);
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptScan(this);
    }

    public AngleBracket getAngleBracket() {
        return angleBracket;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public Vector getMatchVector() {
        return matchVector;
    }

    public void setMatchVector(Vector matchVector) {
        this.matchVector = matchVector;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Shape getScanArea() {
        return getAngleBracket().toShape(getPositionVector().getX(), getPositionVector().getY(), getMaxDistance());
    }
}
