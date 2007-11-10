package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Sonar {
    private Robot robot;
    private Distance maxDistance;

    public Sonar() {
        maxDistance = Distance.fromMeters(250.0);
    }

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(40);
                final Angle angle = scan();
                if (angle == null) {
                    return Short.MIN_VALUE;
                }
                return (short) AngleBracket.around(angle, Angle.fromBygrees(64)).randomAngleBetween().getBygrees();
            }
        };
    }

    private Angle scan() {
        final ScanResult scanResult = robot.scan(AngleBracket.all(), maxDistance);
        return scanResult.getAngle();
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
