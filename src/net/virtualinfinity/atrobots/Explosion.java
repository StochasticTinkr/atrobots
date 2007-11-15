package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.ExplosionSnapshot;

/**
 * @author Daniel Pitts
 */
public class Explosion extends ArenaObject {
    int frame;
    private final Distance radius;

    public Explosion(Vector center, Distance radius) {
        getPosition().copyFrom(new Position(center));
        this.radius = radius;
    }

    public boolean isDead() {
        return frame > radius.getMeters();
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new ExplosionSnapshot(radius, frame);
    }

    public void checkCollision(Robot robot) {
    }

    public void update(Duration duration) {
        frame += duration.getCycles();
    }

}
