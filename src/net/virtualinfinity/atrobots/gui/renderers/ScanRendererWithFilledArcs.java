package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ScanSnapshot;

import java.awt.*;
import java.util.Set;

/**
 * @author Daniel Pitts
 */
public class ScanRendererWithFilledArcs extends ScanRenderer {
    public void render(Graphics2D g2d, ScanSnapshot scanSnapshot, Set<Integer> selectedRobotIds) {
        super.render(g2d, scanSnapshot, selectedRobotIds);
        if (scanSnapshot.isSuccessful()) {
            g2d.setPaint(new RadialGradientPaint(scanSnapshot.getMatchVector().toPoint2D(), 50,
                    new float[]{0f, 1f}, new Color[]{new Color(1f, 0f, 0f, 1f), new Color(1f, 0f, 0f, .25f)}));
        } else {
            g2d.setPaint(new RadialGradientPaint(scanSnapshot.getPositionVector().toPoint2D(),
                    (float) scanSnapshot.getMaxDistance(), new float[]{0, 1},
                    new Color[]{new Color(1f, 1f, 1f, .75f), new Color(0f, 0f, 0f, 0f)}));
        }
        g2d.fill(scanSnapshot.getScanArea());

    }
}
