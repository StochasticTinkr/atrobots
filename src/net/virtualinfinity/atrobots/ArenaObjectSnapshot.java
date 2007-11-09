package net.virtualinfinity.atrobots;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public abstract class ArenaObjectSnapshot {
    protected Vector positionVector;
    protected Vector velocityVector;

    public void setPositionVector(Vector positionVector) {
        this.positionVector = positionVector;
    }

    public void setVelocityVector(Vector velocityVector) {
        this.velocityVector = velocityVector;
    }

    public abstract void paint(Graphics2D g2d);

    protected double getY() {
        return positionVector.getY().getMeters();
    }

    protected double getX() {
        return positionVector.getX().getMeters();
    }

    protected double getVelocityX() {
        return velocityVector.getX().getMeters();
    }

    protected double getVelocityY() {
        return velocityVector.getY().getMeters();
    }
}
