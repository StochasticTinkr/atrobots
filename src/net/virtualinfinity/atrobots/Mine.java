package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Daniel Pitts
 */
public class Mine extends ArenaObject {
    private Distance triggerRadius;
    private final MineLayer owner;

    public Mine(MineLayer owner) {
        this.owner = owner;
    }

    public void setTriggerRadius(Distance triggerRadius) {
        this.triggerRadius = triggerRadius;
    }

    public void setPosition(Position source) {
        position.copyFrom(source);
    }

    public boolean layedBy(MineLayer mineLayer) {
        return false;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final MineSnapshot snapshot = new MineSnapshot();
        snapshot.setTriggerRadius(triggerRadius);
        return snapshot;
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
        }

    }
}
