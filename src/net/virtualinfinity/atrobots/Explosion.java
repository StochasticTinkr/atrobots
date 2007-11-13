package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author Daniel Pitts
 */
public class Explosion extends ArenaObject {
    int frame;
    private final Vector center;
    private final Distance radius;

    public Explosion(Vector center, Distance radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean isDead() {
        return frame > radius.getMeters();
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new ArenaObjectSnapshot() {
            public void paint(Graphics2D g2d) {
                g2d.setPaint(new RadialGradientPaint(center.toPoint2D(), (float) radius.getMeters(), new float[]{0, 1}, new Color[]{Color.yellow, Color.red}));
                final Ellipse2D.Double circle = new Ellipse2D.Double();
                final Point2D.Double corner = new Point2D.Double(center.getX().plus(radius).getMeters() - frame, center.getY().plus(radius).getMeters() - frame);
                circle.setFrameFromCenter(center.toPoint2D(), corner);
                final Composite composite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.85f));
                g2d.fill(circle);
                g2d.setComposite(composite);
            }
        };
    }

    public void checkCollision(Robot robot) {
    }

    public void update(Duration duration) {
        frame += duration.getCycles();
    }
}
