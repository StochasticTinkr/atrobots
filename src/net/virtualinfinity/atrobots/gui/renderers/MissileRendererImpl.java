package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.snapshots.MissileSnapshot;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Daniel Pitts
 */
public class MissileRendererImpl implements SnapshotRenderer<MissileSnapshot> {
    public void render(Graphics2D g2d, MissileSnapshot missileSnapshot) {
        g2d.setPaint(Color.white);
        g2d.draw(new Line2D.Double(missileSnapshot.getX(), missileSnapshot.getY(), missileSnapshot.getX() + missileSnapshot.getVelocityX(), missileSnapshot.getY() + missileSnapshot.getVelocityY()));
    }
}
