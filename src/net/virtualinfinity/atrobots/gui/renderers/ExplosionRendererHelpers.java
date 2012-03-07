package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ExplosionSnapshot;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public class ExplosionRendererHelpers {
    public static Ellipse2D.Double getShapeOf(ExplosionSnapshot explosionSnapshot) {
        final Ellipse2D.Double circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(explosionSnapshot.getPositionVector().toPoint2D(), new Point2D.Double(explosionSnapshot.getPositionVector().getX() + (explosionSnapshot.getRadius()) - explosionSnapshot.getFrame(), explosionSnapshot.getPositionVector().getY() + (explosionSnapshot.getRadius()) - explosionSnapshot.getFrame()));
        return circle;
    }
}
