package net.virtualinfinity.atrobots;


import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.ExplosionSnapshot;

/**
 * Represents an explosion in the arena.
 *
 * @author Daniel Pitts
 */
public class Explosion extends ArenaObject {
    int frame;
    private final double radius;

    /**
     * Create an explosing at the given
     *
     * @param center the center of the explosion.
     * @param radius the radius of the explosion.
     */
    public Explosion(Vector center, double radius) {
        super(center);
        this.radius = radius;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new ExplosionSnapshot(radius, frame);
    }

    @Override
    public void checkCollision(Robot robot) {
    }

    @Override
    public void update(Duration duration) {
        if (!isDead()) {
            frame += duration.getCycles();
            if (frame > radius) {
                frame = (int) Math.rint(radius);
                die();
            }
        }
    }

}
