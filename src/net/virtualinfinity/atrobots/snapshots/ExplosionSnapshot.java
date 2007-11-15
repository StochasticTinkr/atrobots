package net.virtualinfinity.atrobots.snapshots;

import net.virtualinfinity.atrobots.measures.Distance;

/**
 * @author Daniel Pitts
 */
public class ExplosionSnapshot extends ArenaObjectSnapshot {
    private final Distance radius;
    private final int frame;

    public ExplosionSnapshot(Distance radius, int frame) {
        this.radius = radius;
        this.frame = frame;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptExplosion(this);
    }

    public Distance getRadius() {
        return radius;
    }

    public int getFrame() {
        return frame;
    }
}
