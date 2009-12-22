package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.ScanSnapshot;

import java.awt.*;
import java.util.Set;

/**
 * @author Daniel Pitts
 */
public class ScanRenderer implements SnapshotRenderer<ScanSnapshot> {
    public void render(Graphics2D g2d, ScanSnapshot scanSnapshot, Set<Integer> selectedRobotIds) {
        g2d.setPaint(scanSnapshot.isSuccessful() ? Color.red : Color.white);
        g2d.draw(scanSnapshot.getScanArea());
    }

}
