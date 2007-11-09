package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Mine extends ArenaObject {
    private Distance triggerRadius;
    private final MineLayer owner;

    public Mine(MineLayer owner) {
        this.owner = owner;
    }

    public void setTriggerRadius(Distance triggerRadius) {
        this.triggerRadius = triggerRadius;
    }

    public void setPosition(Position source) {
        position.copyFrom(source);
    }

    public boolean layedBy(MineLayer mineLayer) {
        return false;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final MineSnapshot snapshot = new MineSnapshot();
        snapshot.setTriggerRadius(triggerRadius);
        return snapshot;
    }

    private static class MineSnapshot extends ArenaObjectSnapshot {
        private Distance triggerRadius;

        public void setTriggerRadius(Distance triggerRadius) {
            this.triggerRadius = triggerRadius;
        }
    }
}
