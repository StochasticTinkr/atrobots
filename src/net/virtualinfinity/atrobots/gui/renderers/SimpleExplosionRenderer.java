package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ExplosionSnapshot;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author Daniel Pitts
 */
public class SimpleExplosionRenderer implements SnapshotRenderer<ExplosionSnapshot> {
    public void render(Graphics2D g2d, ExplosionSnapshot explosionSnapshot) {
        g2d.setPaint(Color.yellow);
        final Ellipse2D.Double circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(explosionSnapshot.getPositionVector().toPoint2D(), new Point2D.Double(explosionSnapshot.getPositionVector().getX() + (explosionSnapshot.getRadius()) - explosionSnapshot.getFrame(), explosionSnapshot.getPositionVector().getY() + (explosionSnapshot.getRadius()) - explosionSnapshot.getFrame()));
        g2d.draw(circle);
    }
}