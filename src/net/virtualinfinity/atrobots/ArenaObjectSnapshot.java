package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ArenaObjectSnapshot {
    private Vector positionVector;
    private Vector velocityVector;

    public void setPositionVector(Vector positionVector) {
        this.positionVector = positionVector;
    }

    public void setVelocityVector(Vector velocityVector) {
        this.velocityVector = velocityVector;
    }
}
