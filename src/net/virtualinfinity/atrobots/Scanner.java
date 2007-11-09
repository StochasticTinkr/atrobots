package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Scanner implements Resetable {
    private int accuracy;
    private Angle scanArc;
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
            setAccuracy(roundTowardZero(v * 5));
        }
        return scanResult.getDistance();
    }

    private int roundTowardZero(double value) {
        return (int) (value < 0 ? Math.ceil(value + 0.5d) : Math.floor(value - 0.5d));
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
                setScanArc(Angle.fromRelativeBygrees(Math.max(0, Math.min(64, value))));
            }
        };
    }

    public Angle getScanArc() {
        return scanArc;
    }

    public void setScanArc(Angle scanArc) {
        this.scanArc = scanArc;
    }

    public void reset() {
        setScanArc(Angle.fromBygrees(8));
    }

    private void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
