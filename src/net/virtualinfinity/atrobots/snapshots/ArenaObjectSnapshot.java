package net.virtualinfinity.atrobots.snapshots;

import net.virtualinfinity.atrobots.Vector;

/**
 * @author Daniel Pitts
 */
public abstract class ArenaObjectSnapshot {
    private Vector positionVector;
    private Vector velocityVector;

    public void setPositionVector(Vector positionVector) {
        this.positionVector = positionVector;
    }

    public void setVelocityVector(Vector velocityVector) {
        this.velocityVector = velocityVector;
    }

    public abstract void visit(SnapshotVisitor visitor);

    public double getY() {
        return getPositionVector().getY().getMeters();
    }

    public double getX() {
        return getPositionVector().getX().getMeters();
    }

    public double getVelocityX() {
        return getVelocityVector().getX().getMeters();
    }

    public double getVelocityY() {
        return getVelocityVector().getY().getMeters();
    }

    public Vector getPositionVector() {
        return positionVector;
    }

    public Vector getVelocityVector() {
        return velocityVector;
    }
}
