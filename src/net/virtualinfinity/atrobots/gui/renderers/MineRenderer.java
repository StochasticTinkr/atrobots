package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.MineSnapshot;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Daniel Pitts
 */
public class MineRenderer implements SnapshotRenderer<MineSnapshot> {
    public void render(Graphics2D g2d, MineSnapshot mineSnapshot) {
        final Ellipse2D.Double ellipse = new Ellipse2D.Double();
        ellipse.setFrameFromCenter(mineSnapshot.getX(), mineSnapshot.getY(), mineSnapshot.getX() + mineSnapshot.getTriggerRadius().getMeters(), mineSnapshot.getY() + mineSnapshot.getTriggerRadius().getMeters());
        g2d.setPaint(Color.green);
        g2d.draw(ellipse);
        g2d.setPaint(Color.yellow);
        ellipse.setFrameFromCenter(mineSnapshot.getX(), mineSnapshot.getY(), mineSnapshot.getX() + 3, mineSnapshot.getY() + 3);
        g2d.draw(ellipse);
    }
}
