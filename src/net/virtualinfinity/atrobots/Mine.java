package net.virtualinfinity.atrobots;


import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.MineSnapshot;

/**
 * A motionless but explosive arena object.
 *
 * @author Daniel Pitts
 */
public class Mine extends ArenaObject {
    private double triggerRadius;
    private final MineLayer owner;

    public Mine(MineLayer owner) {
        this.owner = owner;
    }

    public void setTriggerRadius(double triggerRadius) {
        this.triggerRadius = triggerRadius;
    }

    public void setPosition(Position source) {
        position.copyFrom(source);
    }

    public boolean layedBy(MineLayer mineLayer) {
        return mineLayer == owner;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final MineSnapshot snapshot = new MineSnapshot();
        snapshot.setTriggerRadius(triggerRadius);
        return snapshot;
    }

    @Override
    public void checkCollision(Robot robot) {
        if (isDead() || layedBy(robot.getMineLayer())) {
            return;
        }
        if (robot.getPosition().getVectorTo(position).getMagnitude() < triggerRadius) {
            explode();
        }
    }

    private void explode() {
        if (!isDead()) {
            die();
            getArena().explosion(getRobot(), new LinearDamageFunction(position, 1, 35.0));
        }
    }

    private Robot getRobot() {
        return owner.getRobot();
    }

}
