package net.virtualinfinity.atrobots.simulation.mine;


import net.virtualinfinity.atrobots.simulation.arena.ArenaObject;
import net.virtualinfinity.atrobots.simulation.arena.LinearDamageFunction;
import net.virtualinfinity.atrobots.simulation.arena.Position;
import net.virtualinfinity.atrobots.simulation.atrobot.DamageInflicter;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.MineSnapshot;

/**
 * A motionless but explosive arena object.
 *
 * @author Daniel Pitts
 */
public class Mine extends ArenaObject {
    private double triggerRadius;
    private final DamageInflicter owner;
    private double triggerRadiusSquared;

    public Mine(DamageInflicter owner) {
        this.owner = owner;
    }

    public void setTriggerRadius(double triggerRadius) {
        this.triggerRadius = triggerRadius;
        this.triggerRadiusSquared = triggerRadius * triggerRadius;
    }

    public void setPosition(Position source) {
        position.copyFrom(source);
    }

    public boolean layedBy(Object owner) {
        return this.owner == owner;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final MineSnapshot snapshot = new MineSnapshot();
        snapshot.setTriggerRadius(triggerRadius);
        return snapshot;
    }

    public void checkCollision(ArenaObject arenaObject) {
        if (isDead() || layedBy(arenaObject)) {
            return;
        }
        if (arenaObject.getPosition().getVectorTo(position).getMagnitudeSquared() < triggerRadiusSquared) {
            explode();
        }
    }

    public void explode() {
        if (!isDead()) {
            die();
            getArena().explosion(owner, new LinearDamageFunction(position, 1, 35.0));
        }
    }


}
