package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Daniel Pitts
 */
public class Mine extends ArenaObject {
    private Distance triggerRadius;
    private final MineLayer owner;
    private Robot robot;

    public Mine(MineLayer owner, Robot robot) {
        this.owner = owner;
        this.robot = robot;
    }

    public void setTriggerRadius(Distance triggerRadius) {
        this.triggerRadius = triggerRadius;
    }

    public void setPosition(Position source) {
        position.copyFrom(source);
    }

    public boolean layedBy(MineLayer mineLayer) {
        return mineLayer == owner;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final MineSnapshot snapshot = new MineSnapshot();
        snapshot.setTriggerRadius(triggerRadius);
        return snapshot;
    }

    @Override
    public void checkCollision(Robot robot) {
        if (isDead() || layedBy(robot.getMineLayer())) {
            return;
        }
        if (robot.getPosition().getVectorTo(position).getMagnatude().compareTo(triggerRadius) < 0) {
            explode();
        }
    }

    private void explode() {
        if (!isDead()) {
            setDead(true);
            getArena().explosion(robot, new LinearDamageFunction(position, 1, 35.0));
        }
    }

    private static class MineSnapshot extends ArenaObjectSnapshot {
        private Distance triggerRadius;

        public void setTriggerRadius(Distance triggerRadius) {
            this.triggerRadius = triggerRadius;
        }

        public void paint(Graphics2D g2d) {
            final Ellipse2D.Double ellipse = new Ellipse2D.Double();
            ellipse.setFrameFromCenter(getX(), getY(), getX() + triggerRadius.getMeters(), getY() + triggerRadius.getMeters());
            g2d.setPaint(Color.green);
            g2d.draw(ellipse);
            g2d.setPaint(Color.yellow);
            ellipse.setFrameFromCenter(getX(), getY(), getX() + 3, getY() + 3);
            g2d.draw(ellipse);
        }

    }
}
