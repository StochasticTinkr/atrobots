package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.Resetable;
import net.virtualinfinity.atrobots.simulation.arena.ScanResult;

/**
 * @author Daniel Pitts
 */
public class Scanner implements Resetable {
    private int accuracy;
    private RelativeAngle scanArc = RelativeAngle.fromBygrees(8);
    private Robot robot;
    private double maxDistance = (1500);

    public Scanner(double maxDistance) {
        this.maxDistance = (maxDistance);
    }

    public PortHandler getScanPort() {
        return new PortHandler() {
            public short read() {
                getComputer().consumeCycles(1);
                final ScanResult result = scan();
                if (!result.successful()) {
                    return Short.MAX_VALUE;
                }
                return (short) Math.round(result.getDistance());
            }
        };
    }

    private ScanResult scan() {
        AngleBracket angleBracket = getAngleBracket();
        ScanResult scanResult = robot.scan(angleBracket, maxDistance, true);
        if (scanResult.successful()) {
            setAccuracy(scanResult.getAccuracy());
        }
        return scanResult;
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
                setScanArc(RelativeAngle.fromBygrees(Math.max(0, Math.min(64, value))));
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
