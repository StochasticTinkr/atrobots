package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MissileSnapshot extends ArenaObjectSnapshot {

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptMissile(this);
    }
}
