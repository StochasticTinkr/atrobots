package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;

/**
 * @author Daniel Pitts
 */
public class RobotRenderer implements SnapshotRenderer<RobotSnapshot> {
    private boolean showStatusBars = true;

    public void render(Graphics2D g2d, RobotSnapshot robotSnapshot, Set<Integer> selectedRobotIds) {
        paintBody(g2d, robotSnapshot);
        if (!robotSnapshot.isDead()) {
            paintShield(g2d, robotSnapshot);
            paintTurret(g2d, robotSnapshot);
            if (showStatusBars) {
                paintArmor(g2d, robotSnapshot);
                paintHeat(g2d, robotSnapshot);
            }
            paintName(g2d, robotSnapshot);
        }
        if (selectedRobotIds.contains(robotSnapshot.getId())) {
            paintSelection(g2d, robotSnapshot);
        }

    }

    private void paintSelection(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        g2d.setPaint(new Color(1f, 1f, 0f, .25f));
        final Ellipse2D.Double s = new Ellipse2D.Double();
        s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30, robotSnapshot.getY() + 30);
        g2d.fill(s);
    }


    private void paintShield(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        if (robotSnapshot.isActiveShield()) {
            g2d.setPaint(new RadialGradientPaint(robotSnapshot.getPositionVector().toPoint2D(), 15, new float[]{0, .75f, 1f},
                    new Color[]{new Color(.5f, .5f, 0f, .8f), new Color(0, 0, .5f, .1f), new Color(1f, 1f, 1f, 1f)}));
            final Ellipse2D.Double s = new Ellipse2D.Double();
            s.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15, robotSnapshot.getY() + 15);
            g2d.fill(s);
            g2d.fill(s);
        }
    }

    private void paintName(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        g2d.setPaint(Color.yellow);
        g2d.drawString(robotSnapshot.getName(), (float) (robotSnapshot.getX() - g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth() / 2), (float) (robotSnapshot.getY() - 10));
    }

    private void paintHeat(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        final Rectangle2D.Double rect = new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 35, 100, 10);
        g2d.setPaint(new GradientPaint((float) rect.getMinX(), (float) rect.getMinY(), new Color(1f, 0f, 0f, 0.1f),
                (float) rect.getMaxX(), (float) rect.getMinY(), new Color(1f, 1f, 0f, 1f)));
        g2d.fill(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 35, robotSnapshot.getTemperature().getLogScale() * .2, 10));
        g2d.draw(rect);
    }

    private void paintArmor(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        g2d.setPaint(new Color(0f, 0f, 1f, 0.6f));
        g2d.fill(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 20, robotSnapshot.getArmor(), 10));
        g2d.setPaint(new Color(0f, 1f, 0f, 0.6f));
        g2d.draw(new Rectangle2D.Double(robotSnapshot.getX() - 50, robotSnapshot.getY() + 20, 100, 10));
    }

    private void paintTurret(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        g2d.setPaint(Color.white);
        g2d.draw(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5));
    }

    private void paintBody(Graphics2D g2d, RobotSnapshot robotSnapshot) {
        final Stroke stroke = g2d.getStroke();
        if (robotSnapshot.isDead()) {
            final BasicStroke basicStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10, new float[]{5f, 5f}, 0f);
            g2d.setStroke(basicStroke);
            g2d.setPaint(Color.orange.darker().darker());
        } else {
            g2d.setPaint(Color.red);
        }
        final GeneralPath path = new GeneralPath();
        path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15);
        AbsoluteAngle cc = robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160));
        AbsoluteAngle c = robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160));
        path.lineTo(robotSnapshot.getX() + cc.cosine() * 9, robotSnapshot.getY() + cc.sine() * 9);
        path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
        path.lineTo(robotSnapshot.getX() + c.cosine() * 9, robotSnapshot.getY() + c.sine() * 9);
        path.closePath();
        if (robotSnapshot.isDead()) {
            g2d.draw(path);
        } else {
            g2d.fill(path);
        }
        g2d.setStroke(stroke);

    }
}
