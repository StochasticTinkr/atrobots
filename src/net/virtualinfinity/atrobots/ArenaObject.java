package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class ArenaObject {
    protected final Heading heading = new Heading();
    protected final Speed speed = new Speed();
    protected final Velocity velocity = new Velocity(heading, speed);
    protected final Position position = new Position();
    private boolean dead;

    public final void simulateMovement(Duration duration) {
        position.move(velocity.times(duration));
    }

    public Position getPosition() {
        return position;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void update(Duration duration) {
        simulateMovement(duration);
    }

    public ArenaObjectSnapshot getSnapshot() {
        final ArenaObjectSnapshot objectSnapshot = createSpecificSnapshot();
        objectSnapshot.setPositionVector(position.getVector());
        objectSnapshot.setVelocityVector(velocity.times(Duration.ONE_CYCLE));
        return objectSnapshot;
    }

    protected abstract ArenaObjectSnapshot createSpecificSnapshot();
}
