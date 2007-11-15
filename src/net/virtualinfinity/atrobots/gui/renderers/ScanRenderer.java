package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ScanSnapshot;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public class ScanRenderer implements SnapshotRenderer<ScanSnapshot> {
    public void render(Graphics2D g2d, ScanSnapshot scanSnapshot) {
        g2d.setPaint(scanSnapshot.isSuccessful() ? Color.red : Color.white);
        g2d.draw(scanSnapshot.getScanArea());
    }

}
