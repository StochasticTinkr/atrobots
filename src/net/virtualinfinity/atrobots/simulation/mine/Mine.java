package net.virtualinfinity.atrobots.simulation.mine;


import net.virtualinfinity.atrobots.hardware.MineLayer;
import net.virtualinfinity.atrobots.simulation.arena.ArenaObject;
import net.virtualinfinity.atrobots.simulation.arena.LinearDamageFunction;
import net.virtualinfinity.atrobots.simulation.arena.Position;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;
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
    private double triggerRadiusSquared;

    public Mine(MineLayer owner) {
        this.owner = owner;
    }

    public void setTriggerRadius(double triggerRadius) {
        this.triggerRadius = triggerRadius;
        this.triggerRadiusSquared = triggerRadius * triggerRadius;
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

    public void checkCollision(Robot robot) {
        if (isDead() || layedBy(robot.getMineLayer())) {
            return;
        }
        if (robot.getPosition().getVectorTo(position).getMagnitudeSquared() < triggerRadiusSquared) {
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
