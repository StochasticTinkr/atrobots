package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AngleBracket;


/**
 * @author Daniel Pitts
 */
public class Radar {
    private Robot robot;

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(3);
                final ScanResult scanResult = robot.scan(AngleBracket.all(), Double.POSITIVE_INFINITY, false);
                if (!scanResult.successful()) {
                    return Short.MAX_VALUE;
                }
                return (short) Math.round(scanResult.getDistance());
            }
        };
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
