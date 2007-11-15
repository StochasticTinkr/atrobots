package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ScanSnapshot extends ArenaObjectSnapshot {
    private final AngleBracket angleBracket;
    private final Distance maxDistance;
    private Vector matchVector;
    private boolean successful;

    public ScanSnapshot(AngleBracket angleBracket, Distance maxDistance, boolean successful, Vector matchVector) {
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

    public Distance getMaxDistance() {
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
}
