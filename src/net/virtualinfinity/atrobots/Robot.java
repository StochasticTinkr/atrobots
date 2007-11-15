package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

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
    private static final RelativeAngle STEERING_SPEED = RelativeAngle.fromBygrees(8);
    private final Position oldPosition = new Position();

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
                setTurretOffset(RelativeAngle.fromBygrees(value));
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
                return (short) hardwareBus.getShutdownLevel();
            }

            public void write(short value) {
                hardwareBus.setShutdownLevel(value);
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
        robotSnapshot.setName(getEntrant().getName());
        return robotSnapshot;
    }

    @Override
    public void checkCollision(Robot robot) {
        if (robot.getPosition().getVectorTo(position).getMagnatude().getMeters() < 8) {
            collides();
            robot.collides();
        }
    }

    private void collides() {
        position.copyFrom(oldPosition);
        throttle.setPower(0);
        throttle.setDesiredPower(0);
        // TODO: Collision damage;
    }

    public void inflictDamage(Robot cause, double damageAmount) {
        // TODO:
        armor.inflictDamage(damageAmount);
    }

    public void explode() {
        if (!isDead()) {
            setDead(true);
            getArena().explosion(this, new LinearDamageFunction(position, 1, 25.0));
        }
    }

    public void update(Duration duration) {
        oldPosition.copyFrom(position);
        super.update(duration);
        getThrottle().update(duration);
        getHeading().moveToward(getDesiredHeading(), STEERING_SPEED);
        getComputer().update(duration);
        if (heat.getTemperature().compareTo(Temperature.fromLogScale(500)) >= 0) {
            destruct();
        }
        heat.cool(Temperature.fromLogScale(1));
        if (position.getX().getMeters() < 4 || position.getX().getMeters() > 1000 - 4 ||
                position.getY().getMeters() < 4 || position.getY().getMeters() > 1000 - 4) {
            collides();
        }
    }
}