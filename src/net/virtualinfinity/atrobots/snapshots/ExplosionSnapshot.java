package net.virtualinfinity.atrobots.snapshots;


/**
 * @author Daniel Pitts
 */
public class ExplosionSnapshot extends ArenaObjectSnapshot {
    private final double radius;
    private final int frame;

    public ExplosionSnapshot(double radius, int frame) {
        this.radius = radius;
        this.frame = frame;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptExplosion(this);
    }

    public double getRadius() {
        return radius;
    }

    public int getFrame() {
        return frame;
    }
}
