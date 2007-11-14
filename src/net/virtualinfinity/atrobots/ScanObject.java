package net.virtualinfinity.atrobots;

import java.awt.*;

/**
 * @author Daniel Pitts
 */
public class ScanObject extends ArenaObject {
    private AngleBracket angleBracket;
    private Distance maxDistance;
    private boolean successful;
    private Vector matchPositionVector;
    private int frame;

    public ScanObject(AngleBracket angleBracket, Distance maxDistance, boolean successful, Vector matchPositionVector) {
        this.angleBracket = angleBracket;
        this.maxDistance = maxDistance;
        this.successful = successful;
        this.matchPositionVector = matchPositionVector;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        ScanSnapshot objectSnapshot = new ScanSnapshot(angleBracket, maxDistance, successful,
                matchPositionVector);
        objectSnapshot.setPositionVector(position.getVector());
        return objectSnapshot;
    }

    public void checkCollision(Robot robot) {
    }

    public void update(Duration duration) {
        if (frame++ > 1) {
            setDead(true);
        }
    }

    private static class ScanSnapshot extends ArenaObjectSnapshot {
        private final AngleBracket angleBracket;
        private final Distance maxDistance;
        private Vector matchVector;
        private boolean successful;

        public ScanSnapshot(AngleBracket angleBracket, Distance maxDistance, boolean successful, Vector matchVector) {
            this.angleBracket = angleBracket;
            this.maxDistance = maxDistance;
            this.successful = successful;
            this.matchVector = matchVector;
        }

        public void paint(Graphics2D g2d) {
            g2d.setPaint(successful ? Color.red : Color.white);
            final Shape shape = angleBracket.toShape(positionVector.getX(), positionVector.getY(), maxDistance);
            g2d.draw(shape);
            final Composite composite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            if (successful) {
                g2d.setPaint(new RadialGradientPaint(matchVector.toPoint2D(), 50,
                        new float[]{0f, 1f}, new Color[]{new Color(1f, 0f, 0f, 1f), new Color(1f, 0f, 0f, .25f)}));
            }
            g2d.fill(shape);
            g2d.setComposite(composite);
        }
    }
}
