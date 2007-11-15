package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ScanObject extends ArenaObject {
    private AngleBracket angleBracket;
    private Distance maxDistance;
    private boolean successful;
    private Vector matchPositionVector;
    private int frame;

    public ScanObject(AngleBracket angleBracket, Distance maxDistance, boolean successful, Vector matchPositionVector) {
        this.angleBracket = angleBracket;
        this.maxDistance = maxDistance;
        this.successful = successful;
        this.matchPositionVector = matchPositionVector;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        ScanSnapshot objectSnapshot = new ScanSnapshot(angleBracket, maxDistance, successful,
                matchPositionVector);
        objectSnapshot.setPositionVector(position.getVector());
        return objectSnapshot;
    }

    public void checkCollision(Robot robot) {
    }

    public void update(Duration duration) {
        if (frame++ > 1) {
            setDead(true);
        }
    }

}
