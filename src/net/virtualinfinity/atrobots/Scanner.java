package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.RelativeAngle;

/**
 * @author Daniel Pitts
 */
public class Scanner implements Resetable {
    private int accuracy;
    private RelativeAngle scanArc = RelativeAngle.fromBygrees(8);
    private Robot robot;
    private Distance maxDistance = Distance.fromMeters(1500);

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(1);
                final Distance distance = scan();
                if (distance == null) {
                    return Short.MAX_VALUE;
                }
                return (short) Math.round(distance.getMeters());
            }
        };
    }

    private Distance scan() {
        AngleBracket angleBracket = getAngleBracket();
        ScanResult scanResult = robot.scan(angleBracket, maxDistance);
        if (scanResult.successful()) {
            final double v = angleBracket.fractionTo(scanResult.getAngle()) - 0.5d;
            setAccuracy(roundAwayFromZero(v * 2));
        }
        return scanResult.getDistance();
    }

    private int roundAwayFromZero(double value) {
        return (int) (value < 0 ? Math.ceil(value - 0.5d) : Math.floor(value + 0.5d));
    }

    private AngleBracket getAngleBracket() {
        return AngleBracket.around(robot.getTurret().getHeading().getAngle(), scanArc);
    }

    public PortHandler getAccuracyPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(1);
                return (short) getAccuracy();
            }
        };
    }

    public int getAccuracy() {
        return accuracy;
    }

    public PortHandler getScanArcLatchPort() {
        return new PortHandler() {
            public short read() {
                return (short) getScanArc().getBygrees();
            }

            public void write(short value) {
                if (value == 0) {
                    setScanArc(RelativeAngle.fromRadians(Math.PI / 1024));
                } else {
                    setScanArc(RelativeAngle.fromBygrees(Math.max(0, Math.min(64, value))));
                }
            }
        };
    }

    public RelativeAngle getScanArc() {
        return scanArc;
    }

    public void setScanArc(RelativeAngle scanArc) {
        this.scanArc = scanArc;
    }

    public void reset() {
        setScanArc(RelativeAngle.fromBygrees(8));
    }

    private void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
