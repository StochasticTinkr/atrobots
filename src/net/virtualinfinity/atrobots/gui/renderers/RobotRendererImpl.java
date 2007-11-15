package net.virtualinfinity.atrobots.gui.renderers;

import net.virtualinfinity.atrobots.AbsoluteAngle;
import net.virtualinfinity.atrobots.RobotSnapshot;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Daniel Pitts
 */
public class RobotRendererImpl implements SnapshotRenderer<RobotSnapshot> {
    public void render(Graphics2D g2d, RobotSnapshot robotSnapshot) {
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
}
