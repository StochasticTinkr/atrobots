package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Velocity {
    private final Heading heading;
    private final Speed speed;

    public Velocity(Heading heading, Speed speed) {
        this.heading = heading;
        this.speed = speed;
    }

    public Vector times(Duration duration) {
        return heading.times(speed.times(duration));
    }

    public String toString() {
        return heading + "*" + speed;
    }
}
