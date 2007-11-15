package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.ScanSnapshot;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public class ScanRendererImpl implements SnapshotRenderer<ScanSnapshot> {
    public void render(Graphics2D g2d, ScanSnapshot scanSnapshot) {
        g2d.setPaint(scanSnapshot.isSuccessful() ? Color.red : Color.white);
        final Shape shape = scanSnapshot.getAngleBracket().toShape(scanSnapshot.getPositionVector().getX(), scanSnapshot.getPositionVector().getY(), scanSnapshot.getMaxDistance());
        g2d.draw(shape);
        final Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        if (scanSnapshot.isSuccessful()) {
            g2d.setPaint(new RadialGradientPaint(scanSnapshot.getMatchVector().toPoint2D(), 50,
                    new float[]{0f, 1f}, new Color[]{new Color(1f, 0f, 0f, 1f), new Color(1f, 0f, 0f, .25f)}));
        }
        g2d.fill(shape);
        g2d.setComposite(composite);
    }
}
