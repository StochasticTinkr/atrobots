package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
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
            g2d.scale(getWidth() / 1000.0, getHeight() / 1000.0);
            final Paint paint = g2d.getPaint();
            final Stroke stroke = g2d.getStroke();
            final AffineTransform transform = g2d.getTransform();
            final SnapshotVisitor visitor = new SnapshotPainter(g2d);
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
        toPaint = currentFrame;
        currentFrame = null;
    }

    private boolean hasFullFrame() {
        return toPaint != null;
    }

    private boolean hasNewFrame() {
        return currentFrame != null;
    }

    private static class SnapshotPainter implements SnapshotVisitor {
        private final Graphics2D g2d;

        public SnapshotPainter(Graphics2D g2d) {
            this.g2d = g2d;
        }

        public void acceptRobot(RobotSnapshot robotSnapshot) {
            g2d.setPaint(Color.red);
            final GeneralPath path = new GeneralPath();
            path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15);
            AbsoluteAngle cc = robotSnapshot.getHeading().counterClockwise(AbsoluteAngle.fromRelativeBygrees(160));
            AbsoluteAngle c = robotSnapshot.getHeading().clockwise(AbsoluteAngle.fromRelativeBygrees(160));
            path.lineTo(robotSnapshot.getX() + cc.cosine() * 9, robotSnapshot.getY() + cc.sine() * 9);
            path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
            path.lineTo(robotSnapshot.getX() + c.cosine() * 9, robotSnapshot.getY() + c.sine() * 9);
            path.closePath();
            g2d.fill(path);
            g2d.setPaint(Color.white);
            g2d.draw(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5));
            g2d.setPaint(new Color(0f, 0f, 1f, 0.6f));
            g2d.fill(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 20, robotSnapshot.getArmor(), 10));
            g2d.setPaint(new Color(0f, 1f, 0f, 0.6f));
            g2d.draw(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 20, 100, 10));
            final Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 35, 100, 10);
            g2d.setPaint(new GradientPaint((float) rect.getMinX(), (float) rect.getMinY(), new Color(1f, 0f, 0f, 0.1f),
                    (float) rect.getMaxX(), (float) rect.getMinY(), new Color(1f, 1f, 0f, 1f)));
            ;
            g2d.fill(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 35, robotSnapshot.getTemperature().getLogScale() * .2, 10));
            g2d.draw(rect);
            g2d.setPaint(Color.yellow);
            g2d.drawString(robotSnapshot.getName(), (float) (robotSnapshot.getX() - 10), (float) (robotSnapshot.getY() - 10));
        }

        public void acceptMissile(MissileSnapshot missileSnapshot) {
            g2d.setPaint(Color.white);
            g2d.draw(new Line2D.Double(missileSnapshot.getX(), missileSnapshot.getY(), missileSnapshot.getX() + missileSnapshot.getVelocityX(), missileSnapshot.getY() + missileSnapshot.getVelocityY()));
        }

        public void acceptMine(MineSnapshot mineSnapshot) {
            final Ellipse2D.Double ellipse = new Ellipse2D.Double();
            ellipse.setFrameFromCenter(mineSnapshot.getX(), mineSnapshot.getY(), mineSnapshot.getX() + mineSnapshot.getTriggerRadius().getMeters(), mineSnapshot.getY() + mineSnapshot.getTriggerRadius().getMeters());
            g2d.setPaint(Color.green);
            g2d.draw(ellipse);
            g2d.setPaint(Color.yellow);
            ellipse.setFrameFromCenter(mineSnapshot.getX(), mineSnapshot.getY(), mineSnapshot.getX() + 3, mineSnapshot.getY() + 3);
            g2d.draw(ellipse);
        }

        public void acceptExplosion(ExplosionSnapshot explosionSnapshot) {
            g2d.setPaint(new RadialGradientPaint(explosionSnapshot.getPositionVector().toPoint2D(), (float) explosionSnapshot.getRadius().getMeters(), new float[]{0, 1}, new Color[]{Color.yellow, Color.red}));
            final Ellipse2D.Double circle = new Ellipse2D.Double();
            final Point2D.Double corner = new Point2D.Double(explosionSnapshot.getPositionVector().getX().plus(explosionSnapshot.getRadius()).getMeters() - explosionSnapshot.getFrame(), explosionSnapshot.getPositionVector().getY().plus(explosionSnapshot.getRadius()).getMeters() - explosionSnapshot.getFrame());
            circle.setFrameFromCenter(explosionSnapshot.getPositionVector().toPoint2D(), corner);
            final Composite composite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.85f));
            g2d.fill(circle);
            g2d.setComposite(composite);
        }

        public void acceptScan(ScanSnapshot scanSnapshot) {
            g2d.setPaint(scanSnapshot.isSuccessful() ? Color.red : Color.white);
            final Shape shape = scanSnapshot.getAngleBracket().toShape(scanSnapshot.getPositionVector().getX(), scanSnapshot.getPositionVector().getY(), scanSnapshot.getMaxDistance());
            g2d.draw(shape);
//            final Composite composite = g2d.getComposite();
//            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
//            if (scanSnapshot.isSuccessful()) {
//                g2d.setPaint(new RadialGradientPaint(scanSnapshot.getMatchVector().toPoint2D(), 50,
//                        new float[]{0f, 1f}, new Color[]{new Color(1f, 0f, 0f, 1f), new Color(1f, 0f, 0f, .25f)}));
//            }
//            g2d.fill(shape);
//            g2d.setComposite(composite);
        }
    }
}
