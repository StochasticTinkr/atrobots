package net.virtualinfinity.atrobots.snapshots;


import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author Daniel Pitts
 */
public class ExplosionSnapshot extends ArenaObjectSnapshot {
    private final double radius;
    private final int frame;

    public ExplosionSnapshot(double radius, int frame) {
        this.radius = radius;
        this.frame = frame;
    }

    public Ellipse2D.Double getExplosionShape() {
        final Ellipse2D.Double circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(getPositionVector().toPoint2D(), new Point2D.Double(getPositionVector().getX() + (getRadius()) - getFrame(), getPositionVector().getY() + (getRadius()) - getFrame()));
        return circle;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptExplosion(this);
    }

    public double getRadius() {
        return radius;
    }

    public int getFrame() {
        return frame;
    }
}
