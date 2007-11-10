package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * @author Daniel Pitts
 */
public class Robot extends ArenaObject implements Resetable {
    private final Heat heat = new Heat();
    private final Odometer odometer = new Odometer();
    private final Throttle throttle = new Throttle();
    private Computer computer;
    private Turret turret;
    private Transponder transponder;
    private Transceiver transceiver;
    private Duration lastDamageGiven = Duration.fromCycles(0);
    private Duration lastDamageTaken = Duration.fromCycles(0);
    private Entrant entrant;
    private Armor armor;
    private Radar radar;
    private Sonar sonar;
    private Temperature shutdownLevel;
    private MineLayer mineLayer;
    private Shield shield;
    private boolean overburn;
    private HardwareBus hardwareBus;
    private final LastScanResult lastScanResult = new LastScanResult();
    private static final RelativeAngle STEERING_SPEED = RelativeAngle.fromCounterClockwiseBygrees(8);

    {
        position.setOdometer(odometer);
        throttle.setSpeed(speed);
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Computer getComputer() {
        return computer;
    }

    public void destruct() {
        getArmor().setRemaining(0);
    }

    public Turret getTurret() {
        return turret;
    }

    public void setOverburn(boolean overburn) {
        this.overburn = overburn;
    }

    public Transponder getTransponder() {
        return transponder;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Duration getLastDamageGiven() {
        return lastDamageGiven;
    }

    public Duration getLastDamageTaken() {
        return lastDamageTaken;
    }

    public Transceiver getTransceiver() {
        return transceiver;
    }


    public Odometer getOdometer() {
        return odometer;
    }

    public void setEntrant(Entrant entrant) {
        this.entrant = entrant;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public Throttle getThrottle() {
        return throttle;
    }

    public Heat getHeat() {
        return heat;
    }

    public Heading getHeading() {
        return heading;
    }

    public PortHandler getTurretOffsetSensor() {
        return new PortHandler() {
            public short read() {
                return (short) getTurretShift();
            }
        };
    }

    public int getTurretShift() {
        return getTurret().getHeading().getAngle().getAngleClockwiseTo(getHeading().getAngle()).getBygrees();
    }

    public Armor getArmor() {
        return armor;
    }

    public Radar getRadar() {
        return radar;
    }

    public PortHandler getAimTurretPort() {
        return new PortHandler() {
            public void write(short value) {
                setTurretOffset(RelativeAngle.fromCounterClockwiseBygrees(value));
            }
        };
    }

    private void setTurretOffset(RelativeAngle angle) {
        getTurret().getHeading().setAngle(getHeading().getAngle().counterClockwise(angle));
    }

    public Heading getDesiredHeading() {
        return getHardwareBus().getDesiredHeading();
    }

    public Sonar getSonar() {
        return sonar;
    }

    public PortHandler getOverburnLatchPort() {
        return new PortHandler() {
            public short read() {
                return (short) (isOverburn() ? 1 : 0);
            }

            public void write(short value) {
                setOverburn(value != 0);
            }
        };
    }

    public boolean isOverburn() {
        return overburn;
    }

    public PortHandler getShutdownLevelLatchPort() {
        return new PortHandler() {
            public short read() {
                return (short) getShutdownLevel().getLogScale();
            }

            public void write(short value) {
                setShutdownLevel(Temperature.fromLogScale(value));
            }
        };
    }

    public Temperature getShutdownLevel() {
        return shutdownLevel;
    }

    public void setShutdownLevel(Temperature shutdownLevel) {
        this.shutdownLevel = shutdownLevel;
    }

    public MineLayer getMineLayer() {
        return mineLayer;
    }


    public Shield getShield() {
        return shield;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }

    public void setTransponder(Transponder transponder) {
        this.transponder = transponder;
    }

    public void setTransceiver(Transceiver transceiver) {
        this.transceiver = transceiver;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public void setRadar(Radar radar) {
        this.radar = radar;
    }

    public void setSonar(Sonar sonar) {
        this.sonar = sonar;
    }

    public void setMineLayer(MineLayer mineLayer) {
        this.mineLayer = mineLayer;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public HardwareBus getHardwareBus() {
        return hardwareBus;
    }

    public void setHardwareBus(HardwareBus hardwareBus) {
        this.hardwareBus = hardwareBus;
        getComputer().setHardwareBus(hardwareBus);
    }

    public void reset() {
        setOverburn(false);

    }

    public Arena getArena() {
        return getEntrant().getGame().getRound().getArena();
    }

    public ScanResult scan(AngleBracket angleBracket, Distance maxDistance) {
        final ScanResult scanResult = getArena().scan(this, getPosition(), angleBracket, maxDistance);
        lastScanResult.set(scanResult);
        if (scanResult.successful()) {
            getComputer().getRegisters().getTargetId().set((short) scanResult.getMatch().getTransponder().getId());
        }
        return scanResult;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setTemperature(heat.getTemperature());
        robotSnapshot.setArmor(armor.getRemaining());
        robotSnapshot.setOverburn(overburn);
        robotSnapshot.setActiveShield(shield.isActive());
        robotSnapshot.setHeading(heading.getAngle());
        robotSnapshot.setTurretHeading(turret.getHeading().getAngle());
        return robotSnapshot;
    }

    private static class RobotSnapshot extends ArenaObjectSnapshot {
        private Temperature temperature;
        private double armor;
        private boolean overburn;
        private boolean activeShield;
        private AbsoluteAngle heading;
        private AbsoluteAngle turretHeading;

        public void setTemperature(Temperature temperature) {
            this.temperature = temperature;
        }

        public void setArmor(double armor) {
            this.armor = armor;
        }

        public void setOverburn(boolean overburn) {
            this.overburn = overburn;
        }

        public void setActiveShield(boolean activeShield) {
            this.activeShield = activeShield;
        }

        public void paint(Graphics2D g2d) {
            g2d.setPaint(Color.red);
            final GeneralPath path = new GeneralPath();
            path.moveTo(getX() + heading.cosine() * 25, getY() + heading.sine() * 25);
            AbsoluteAngle cc = heading.counterClockwise(AbsoluteAngle.fromRelativeBygrees(160));
            AbsoluteAngle c = heading.clockwise(AbsoluteAngle.fromRelativeBygrees(160));
            path.lineTo(getX() + cc.cosine() * 12, getY() + cc.sine() * 12);
            path.lineTo(getX(), getY());
            path.lineTo(getX() + c.cosine() * 12, getY() + c.sine() * 12);
            path.closePath();
            g2d.fill(path);
            g2d.setPaint(Color.white);
            g2d.draw(new Line2D.Double(getX(), getY(), getX() + turretHeading.cosine() * 30, getY() + turretHeading.sine() * 30));
            // TODO:
        }

        public void setHeading(AbsoluteAngle heading) {
            this.heading = heading;
        }

        public void setTurretHeading(AbsoluteAngle turretHeading) {
            this.turretHeading = turretHeading;
        }
    }

    public void update(Duration duration) {
        super.update(duration);
        getThrottle().update(duration);
        getHeading().moveToward(getDesiredHeading(), STEERING_SPEED);
        // TODO: Handle throttle.
        getComputer().update(duration);
    }
}