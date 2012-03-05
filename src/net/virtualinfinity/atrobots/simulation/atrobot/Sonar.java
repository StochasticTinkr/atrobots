package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.ScanResult;

/**
 * @author Daniel Pitts
 */
public class Sonar {
    private Robot robot;
    private double maxDistance;

    public Sonar() {
        maxDistance = (250.0);
    }

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                consumeCycles(40);
                final AbsoluteAngle angle = scan();
                if (angle == null) {
                    return Short.MIN_VALUE;
                }
                return (short) AngleBracket.around(angle, RelativeAngle.fromBygrees(16)).randomAngleBetween().getBygrees();
            }
        };
    }

    private AbsoluteAngle scan() {
        final ScanResult scanResult = robot.scan(AngleBracket.all(), maxDistance, false);
        return scanResult.getAngle();
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
