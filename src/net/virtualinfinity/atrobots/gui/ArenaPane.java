package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;
import net.virtualinfinity.atrobots.gui.renderers.*;
import net.virtualinfinity.atrobots.snapshots.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Set;

/**
 * A GUI component which renders the game arena.
 * This object should be registered as a {@link net.virtualinfinity.atrobots.SimulationObserver} in the
 * {@link net.virtualinfinity.atrobots.SimulationFrameBuffer}
 *
 * @author Daniel Pitts
 */
public class ArenaPane extends JComponent implements SimulationObserver {
    private SimulationFrameBuffer.SimulationFrame currentFrame;
    private Collection<ArenaObjectSnapshot> toPaint;
    private static final Rectangle2D.Double ARENA_SIZE = new Rectangle2D.Double(0, 0, 1000, 1000);
    private RobotStatusPane robotStatusPane;

    public ArenaPane() {
    }

    public void frameAvailable(SimulationFrameBuffer buffer) {
        EventQueue.invokeLater(new UpdateFrame(buffer));
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        innerPaint(g2d);
    }

    private void innerPaint(final Graphics2D g2d) {
        final RenderingHints renderingHints = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
//        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
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
            g2d.setPaint(Color.green);
            g2d.scale(getWidth() / 1100.0, getHeight() / 1100.0);
            g2d.translate(50, 50);
            g2d.draw(ARENA_SIZE);
            final Paint paint = g2d.getPaint();
            final Stroke stroke = g2d.getStroke();
            final AffineTransform transform = g2d.getTransform();
            final SnapshotVisitor visitor = new SnapshotPainter(g2d, robotStatusPane.getSelectedRobotIds());
            for (ArenaObjectSnapshot snapshot : toPaint) {
                snapshot.visit(visitor);
                g2d.setTransform(transform);
                g2d.setPaint(paint);
                g2d.setStroke(stroke);
            }
            g2d.setTransform(originalTransform);
        }
        g2d.setRenderingHints(renderingHints);
    }

    private void makeNewFrameCurrentFrame() {
        toPaint = currentFrame.getAllObjects();
        currentFrame = null;
    }

    private boolean hasFullFrame() {
        return toPaint != null;
    }

    private boolean hasNewFrame() {
        return currentFrame != null;
    }

    public void setRobotStatusPane(RobotStatusPane robotStatusPane) {
        this.robotStatusPane = robotStatusPane;
    }

    private static class SnapshotPainter implements SnapshotVisitor {
        private final Graphics2D g2d;
        private final SnapshotRenderer<RobotSnapshot> robotRenderer;
        private final SnapshotRenderer<MissileSnapshot> missileRenderer;
        private final SnapshotRenderer<MineSnapshot> mineRenderer;
        private final SnapshotRenderer<ExplosionSnapshot> explosionRenderer;
        private final SnapshotRenderer<ScanSnapshot> scanRenderer;
        private final Set<Integer> selectedRobotIds;

        public SnapshotPainter(Graphics2D g2d, Set<Integer> selectedRobotIds) {
            this.g2d = g2d;
            this.selectedRobotIds = selectedRobotIds;
            robotRenderer = new RobotRenderer();
            missileRenderer = new MissileRenderer();
            mineRenderer = new MineRenderer();
            explosionRenderer = new GradientExplosionRenderer();
            scanRenderer = new ScanRendererWithFilledArcs();
        }

        public void acceptRobot(RobotSnapshot robotSnapshot) {
            robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        }

        public void acceptMissile(MissileSnapshot missileSnapshot) {
            missileRenderer.render(g2d, missileSnapshot, selectedRobotIds);
        }

        public void acceptMine(MineSnapshot mineSnapshot) {
            mineRenderer.render(g2d, mineSnapshot, selectedRobotIds);
        }

        public void acceptExplosion(ExplosionSnapshot explosionSnapshot) {
            explosionRenderer.render(g2d, explosionSnapshot, selectedRobotIds);
        }


        public void acceptScan(ScanSnapshot scanSnapshot) {
            scanRenderer.render(g2d, scanSnapshot, selectedRobotIds);
        }
    }

    private class UpdateFrame implements Runnable {
        private final SimulationFrameBuffer buffer;

        public UpdateFrame(SimulationFrameBuffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            repaint();
            currentFrame = buffer.getCurrentFrame();
        }
    }
}
