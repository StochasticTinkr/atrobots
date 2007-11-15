package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.ArenaObjectSnapshot;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public interface SnapshotRenderer<T extends ArenaObjectSnapshot> {
    public void render(Graphics2D g2d, T object);
}
