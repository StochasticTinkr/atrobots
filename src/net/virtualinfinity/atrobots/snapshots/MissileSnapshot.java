package net.virtualinfinity.atrobots.snapshots;

/**
 * @author Daniel Pitts
 */
public class MissileSnapshot extends ArenaObjectSnapshot {
    private final boolean overburn;

    public MissileSnapshot(boolean overburn) {
        this.overburn = overburn;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptMissile(this);
    }

    public boolean isOverburn() {
        return overburn;
    }
}
