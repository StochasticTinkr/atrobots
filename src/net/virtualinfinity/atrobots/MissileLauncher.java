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
                fireMissile(Angle.fromBygrees(value));
            }
        };
    }

    private void fireMissile(Angle shift) {
        final byte value = shift.getSignedBygrees();
        Angle angle = heading.getAngle().counterClockwise(Angle.fromBygrees(Math.max(-4, Math.min(value, 4))));
//TODO://        getArena().fireMissile(new Missile(robot, position, angle));
    }

    public Arena getArena() {
        return robot.getEntrant().getGame().getRound().getArena();
    }
}
