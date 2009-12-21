package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.measures.AngleBracket;
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
    private Throttle throttle;
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
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Computer getComputer() {
        return computer;
    }

    public void destruct() {
        getArmor().destruct();
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
        return getTurret().getHeading().getAngle().getAngleCounterClockwiseTo(getHeading().getAngle()).getBygrees();
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
    }

    public void reset() {
        setOverburn(false);

    }

    public ScanResult scan(AngleBracket angleBracket, double maxDistance) {
        final ScanResult scanResult = getArena().scan(this, getPosition(), angleBracket, maxDistance);
        lastScanResult.set(scanResult);
        if (scanResult.successful()) {
            getComputer().getRegisters().getTargetId().set((short) scanResult.getMatch().getTransponder().getId());
        }
        return scanResult;
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setTemperature(getHeat().getTemperature());
        robotSnapshot.setArmor(getArmor().getRemaining());
        robotSnapshot.setOverburn(isOverburn());
        robotSnapshot.setActiveShield(getShield().isActive());
        robotSnapshot.setHeading(getHeading().getAngle());
        robotSnapshot.setTurretHeading(getTurret().getHeading().getAngle());
        robotSnapshot.setName(getEntrant().getName());
        robotSnapshot.setId(getEntrant().getId());
        robotSnapshot.setRoundKills(getEntrant().getRoundKills());
        robotSnapshot.setTotalKills(getEntrant().getTotalKills());
        robotSnapshot.setTotalDeaths(getEntrant().getTotalDeaths());
        robotSnapshot.setLastMessage(getComputer().getLastMessage());
        return robotSnapshot;
    }

    @Override
    public void checkCollision(Robot robot) {
        if (robot.getPosition().getVectorTo(position).getMagnitudeSquared() < 64) {
            collides();
            robot.collides();
        }
    }

    private void collides() {
        position.copyFrom(oldPosition);
        if (speed.times(Duration.ONE_CYCLE) > 2) {
            armor.inflictDamage(1);
        }
        throttle.setPower(0);
        throttle.setDesiredPower(0);
        computer.getRegisters().getCollisionCount().increment();
    }

    public void inflictDamage(Robot cause, double damageAmount) {
        if (!isDead()) {
            armor.inflictDamage(shield.absorbDamage(damageAmount));
            if (isDead()) {
                cause.getEntrant().incrementKills();
            }
        }
    }

    public void explode() {
        if (!isDead()) {
            die();
            getArena().explosion(this, new LinearDamageFunction(position, isOverburn() ? 1.3 : 1, 25.0));
        }
    }

    public void update(Duration duration) {
        oldPosition.copyFrom(position);
        super.update(duration);
        getThrottle().update(duration);
        getHeading().moveToward(getDesiredHeading(), STEERING_SPEED);
        getComputer().update(duration);
        if (heat.getTemperature().getLogScale() >= 500) {
            destruct();
        } else if (heat.getTemperature().getLogScale() >= 475) {
            armor.inflictDamage(duration.getCycles() / 4d);
        } else if (heat.getTemperature().getLogScale() >= 450) {
            armor.inflictDamage(duration.getCycles() / 8d);
        } else if (heat.getTemperature().getLogScale() >= 400) {
            armor.inflictDamage(duration.getCycles() / 16d);
        } else if (heat.getTemperature().getLogScale() >= 350) {
            armor.inflictDamage(duration.getCycles() / 32d);
        } else if (heat.getTemperature().getLogScale() >= 300) {
            armor.inflictDamage(duration.getCycles() / 64d);
        }
        heat.cool(isOverburn() ? getCoolTemp(duration).times(0.66) : getCoolTemp(duration));
        shield.update(duration);
        if (position.getX() < 4 || position.getX() > 1000 - 4 ||
                position.getY() < 4 || position.getY() > 1000 - 4) {
            collides();
        }
    }

    private Temperature getCoolTemp(Duration duration) {
        return Temperature.fromLogScale(duration.getCycles() * 1.125);
    }

    public void setThrottle(Throttle throttle) {
        this.throttle = throttle;
        throttle.setSpeed(speed);
        throttle.setHeat(heat);
    }
}