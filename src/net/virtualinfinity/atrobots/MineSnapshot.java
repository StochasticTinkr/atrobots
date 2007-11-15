package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MineSnapshot extends ArenaObjectSnapshot {
    private Distance triggerRadius;

    public void setTriggerRadius(Distance triggerRadius) {
        this.triggerRadius = triggerRadius;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptMine(this);
    }

    public Distance getTriggerRadius() {
        return triggerRadius;
    }
}
