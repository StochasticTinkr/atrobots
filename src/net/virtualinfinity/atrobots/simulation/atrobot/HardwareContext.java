package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.computer.*;
import net.virtualinfinity.atrobots.hardware.*;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.Arena;

import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class HardwareContext {
    private Throttle throttle;
    private double coolMultiplier;
    private Armor armor;
    private MineLayer mineLayer;
    private Radar radar;
    private Shield shield;
    private Sonar sonar;
    private Transceiver transceiver;
    private Transponder transponder;
    private Turret turret;
    private MissileLauncher missileLauncher;
    private double missileLauncherPower;
    private Scanner scanner;
    private Robot robot;
    private HardwareBus hardwareBus;
    private MemoryArray lowerMemoryArray;
    private final SpecialRegister desiredPowerRegister = new SpecialRegister() {
        public short get() {
            return (short) robot.getThrottle().getDesiredPower();
        }
    };
    private final SpecialRegister desiredHeadingRegister = new SpecialRegister() {
        public short get() {
            return (short) (robot.getDesiredHeading().getAngle().getBygrees() & 255);
        }
    };
    private final SpecialRegister turretShiftRegister = new SpecialRegister() {
        public short get() {
            return (short) robot.getTurretShift();
        }
    };
    private final SpecialRegister scannerAccuracyRegister = new SpecialRegister() {
        public short get() {
            return (short) robot.getTurret().getScanner().getAccuracy();
        }
    };
    private final SpecialRegister odometerRegister = new SpecialRegister() {
        public short get() {
            return (short) Math.round(robot.getOdometer().getDistance());
        }
    };

    public void setThrottle(Throttle throttle) {
        this.throttle = throttle;
    }

    public void setCoolMultiplier(double coolMultiplier) {
        this.coolMultiplier = coolMultiplier;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public void setMineLayer(MineLayer mineLayer) {
        this.mineLayer = mineLayer;
    }

    public void setRadar(Radar radar) {
        this.radar = radar;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public void setSonar(Sonar sonar) {
        this.sonar = sonar;
    }

    public void setTransceiver(Transceiver transceiver) {
        this.transceiver = transceiver;
    }

    public void setTransponder(Transponder transponder) {
        this.transponder = transponder;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }

    public void setMissileLauncher(MissileLauncher missileLauncher) {
        this.missileLauncher = missileLauncher;
    }

    public void setMissileLauncherPower(double missileLauncherPower) {
        this.missileLauncherPower = missileLauncherPower;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void wireRobotComponents(Arena arena, int totalRounds, int roundNumber) {
        robot.getHeatSinks().setCoolMultiplier(coolMultiplier);
        wireThrottle();
        wireArmor();
        wireMineLayer();
        wireRadar();
        wireShield();
        wireSonar();
        wireRadar();
        final CommunicationsQueue commQueue = createCommQueue();
        wireTranceiver(commQueue);
        wireComputer(commQueue);
        wireTransponder();
        wireTurret();
        wireMissileLauncher(arena);
        wireScanner();
        wireHardwareBus(arena, totalRounds, roundNumber);
        connectSpecialRegisters();
    }

    private void connectSpecialRegisters() {
        lowerMemoryArray.addSpecialRegister(9, odometerRegister);
        lowerMemoryArray.addSpecialRegister(0, desiredPowerRegister);
        lowerMemoryArray.addSpecialRegister(1, desiredHeadingRegister);
        lowerMemoryArray.addSpecialRegister(2, turretShiftRegister);
        lowerMemoryArray.addSpecialRegister(3, scannerAccuracyRegister);
    }

    private void wireHardwareBus(Arena arena, int totalRounds, int roundNumber) {
        robot.setHardwareBus(hardwareBus);
        hardwareBus.setHeat(robot.getHeatSinks());
        hardwareBus.setPorts(createPortHandlers());
        hardwareBus.setInterrupts(createInterruptHandlers(arena, totalRounds, roundNumber));
        hardwareBus.addResetable(robot.getTurret().getScanner());
        hardwareBus.addResetable(robot.getTurret());
        hardwareBus.addResetable(robot.getOdometer());
        hardwareBus.addResetable(robot);
        hardwareBus.addResetable(robot.getShield());
        hardwareBus.setAutoShutdownListener(robot.getComputer());
        robot.getComputer().setHardwareBus(hardwareBus);
        hardwareBus.addShutdownListener(robot.getComputer());
        hardwareBus.addShutdownListener(robot.getShield());
        hardwareBus.addShutdownListener(robot.getThrottle());
    }

    private Map<Integer, PortHandler> createPortHandlers() {
        return getPortFactory().createPortHandlers(robot);
    }

    private AtRobotPortFactory getPortFactory() {
        return new AtRobotPortFactory();
    }

    private Map<Integer, InterruptHandler> createInterruptHandlers(Arena arena, int totalRounds, int roundNumber) {
        return getInterruptFactory().createInterruptTable(robot, arena, totalRounds, roundNumber);
    }

    private AtRobotInterruptFactory getInterruptFactory() {
        return new AtRobotInterruptFactory();
    }

    private void wireMissileLauncher(Arena arena) {
        missileLauncher.setHeading(turret.getHeading());
        missileLauncher.setPower(missileLauncherPower);
        missileLauncher.setHeatSinks(robot.getHeatSinks());
        missileLauncher.setMissileFactory(robot);
        missileLauncher.setOverburner(robot);
        missileLauncher.setArena(arena);
        missileLauncher.setPosition(robot.getPosition());
    }

    private void wireScanner() {
        turret.setScanner(scanner);
        scanner.setScanSource(robot);
        scanner.setHeading(robot.getHeading());
    }

    private void wireComputer(CommunicationsQueue commQueue) {
        robot.getComputer().setCommQueue(commQueue);
        robot.getComputer().getMemory().setErrorHandler(robot.getComputer().getErrorHandler());
    }

    private void wireTurret() {
        robot.setTurret(turret);
        turret.getHeading().setRelation(robot.getHeading());
        turret.setKeepshift(false);
        turret.getHeading().setAngle(robot.getHeading().getAngle());
        turret.setMissileLauncher(missileLauncher);
    }

    private void wireTransponder() {
        robot.setTransponder(transponder);
    }

    private void wireTranceiver(CommunicationsQueue commQueue) {
        robot.setTransceiver(transceiver);
        transceiver.setCommQueue(commQueue);
    }

    private void wireSonar() {
        robot.setSonar(sonar);
        sonar.setScanSource(robot);
    }

    private void wireShield() {
        robot.setShield(shield);
        robot.getShield().setHeatSinks(robot.getHeatSinks());
    }

    private void wireRadar() {
        radar.setScanSource(robot);
        robot.setRadar(radar);
    }

    private void wireMineLayer() {
        robot.setMineLayer(mineLayer);
        mineLayer.setOwner(robot);
    }

    private void wireArmor() {
        robot.setArmor(armor);
    }

    private void wireThrottle() {
        robot.setThrottle(throttle);
        throttle.setOverburner(robot);
    }

    private CommunicationsQueue createCommQueue() {
        final CommunicationsQueue queue = new CommunicationsQueue(robot.getComputer().getCommQueueMemoryRegion(),
                robot.getComputer().getRegisters().getCommunicationQueueHead(),
                robot.getComputer().getRegisters().getCommunicationQueueTail());
        queue.setComputerErrorHandler(robot.getComputer().getErrorHandler());
        return queue;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void setHardwareBus(HardwareBus hardwareBus) {
        this.hardwareBus = hardwareBus;
    }

    public void setLowerMemoryArray(MemoryArray lowerMemoryArray) {
        this.lowerMemoryArray = lowerMemoryArray;
    }
}
