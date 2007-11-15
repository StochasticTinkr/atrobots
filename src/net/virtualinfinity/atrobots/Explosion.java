package net.virtualinfinity.atrobots;

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
