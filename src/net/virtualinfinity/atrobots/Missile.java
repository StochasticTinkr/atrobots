package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Daniel Pitts
 */
public class Missile extends ArenaObject {
    private final Robot robot;

    public Missile(Robot robot, Position position, Angle angle) {
        this.robot = robot;
        this.position.copyFrom(position);
        this.heading.setAngle(angle);
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new MissileSnapshot();
    }

    private static class MissileSnapshot extends ArenaObjectSnapshot {
        public void paint(Graphics2D g2d) {
            g2d.setPaint(Color.white);
            g2d.draw(new Line2D.Double(getX(), getY(), getX() + getVelocityX(), getY() + getVelocityY()));
        }
    }
}
