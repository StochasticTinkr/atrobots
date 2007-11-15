package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class ArenaPane extends JComponent implements SimulationObserver {
    private Collection<ArenaObjectSnapshot> currentFrame;
    private Collection<ArenaObjectSnapshot> toPaint;
    private VolatileImage volatileImage;
    private static final boolean SHOULD_ACCELERATE = false;

    public ArenaPane() {
    }

    public void frameAvailable(SimulationFrameBuffer buffer) {
        repaint();
        currentFrame = buffer.getCurrentFrame();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        innerPaint(g2d);
    }

    private void innerPaint(Graphics2D g2d) {
        final RenderingHints renderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        if (isOpaque()) {
            final Paint paint = g2d.getPaint();
            g2d.setPaint(getBackground());
            g2d.fill(getBounds());
            g2d.setPaint(paint);
        }
        if (hasNewFrame()) {
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
        g2d.setRenderingHints(renderingHints);
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
}
