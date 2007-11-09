package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class ArenaPane extends JComponent implements SimulationObserver {
    private Collection<ArenaObjectSnapshot> currentFrame;
    private Collection<ArenaObjectSnapshot> toPaint;

    public ArenaPane() {
    }

    public void frameAvailable(SimulationFrameBuffer buffer) {
        repaint();
        currentFrame = buffer.getCurrentFrame();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (isOpaque()) {
            final Paint paint = g2d.getPaint();
            g2d.setPaint(getBackground());
            g2d.fill(g2d.getClip());
            g2d.setPaint(paint);
        }
        if ((isFullRepaint(g2d) || !hasFullFrame()) && hasNewFrame()) {
            makeNewFrameCurrentFrame();
        }
        if (hasFullFrame()) {
            final AffineTransform originalTransform = g2d.getTransform();
            g2d.scale(getWidth() / 1000.0, getHeight() / 1000.0);
            final Paint paint = g2d.getPaint();
            final Stroke stroke = g2d.getStroke();
            final AffineTransform transform = g2d.getTransform();
            for (ArenaObjectSnapshot snapshot : toPaint) {
                snapshot.paint(g2d);
                g2d.setTransform(transform);
                g2d.setPaint(paint);
                g2d.setStroke(stroke);
            }
            g2d.setTransform(originalTransform);
        }
    }

    private void makeNewFrameCurrentFrame() {
        toPaint = currentFrame;
        currentFrame = null;
    }

    private boolean hasFullFrame() {
        return toPaint != null;
    }

    private boolean hasNewFrame() {
        return currentFrame != null;
    }

    private boolean isFullRepaint(Graphics2D g2d) {
        return true;
    }
}
