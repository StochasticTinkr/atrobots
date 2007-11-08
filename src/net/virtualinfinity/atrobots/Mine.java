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
}
