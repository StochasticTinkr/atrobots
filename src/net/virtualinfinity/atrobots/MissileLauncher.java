package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MissileLauncher {
    private final Heading heading;
    private final Position position;
    private final Robot robot;

    public MissileLauncher(Robot robot, Position position, Heading heading) {
        this.robot = robot;
        this.position = position;
        this.heading = heading;
    }

    public PortHandler getActuator() {
        return new PortHandler() {
            public void write(short value) {
                getComputer().consumeCycles(3);
                fireMissile(RelativeAngle.fromBygrees(value));
            }
        };
    }

    private void fireMissile(RelativeAngle shift) {
        final byte value = shift.getSignedBygrees();
        int bygrees = Math.max(-4, Math.min(value, 4));
        AbsoluteAngle angle = heading.getAngle().counterClockwise(RelativeAngle.fromBygrees(bygrees));
        final Missile missile = new Missile(robot, position, angle);
        missile.getSpeed().setDistanceOverTime(Distance.fromMeters(32), Duration.ONE_CYCLE);
        getArena().fireMissile(missile);
        robot.getHeat().warm(Temperature.fromLogScale(50));
    }

    public Arena getArena() {
        return robot.getEntrant().getGame().getRound().getArena();
    }
}
