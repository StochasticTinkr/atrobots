package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ExplosionSnapshot;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author Daniel Pitts
 */
public class ExplosionRendererImpl implements SnapshotRenderer<ExplosionSnapshot> {
    public void render(Graphics2D g2d, ExplosionSnapshot explosionSnapshot) {
        g2d.setPaint(new RadialGradientPaint(explosionSnapshot.getPositionVector().toPoint2D(), (float) explosionSnapshot.getRadius().getMeters(), new float[]{0, 1}, new Color[]{Color.yellow, Color.red}));
        final Ellipse2D.Double circle = new Ellipse2D.Double();
        final Point2D.Double corner = new Point2D.Double(explosionSnapshot.getPositionVector().getX().plus(explosionSnapshot.getRadius()).getMeters() - explosionSnapshot.getFrame(), explosionSnapshot.getPositionVector().getY().plus(explosionSnapshot.getRadius()).getMeters() - explosionSnapshot.getFrame());
        circle.setFrameFromCenter(explosionSnapshot.getPositionVector().toPoint2D(), corner);
        final Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.85f));
        g2d.fill(circle);
        g2d.setComposite(composite);

    }
}
