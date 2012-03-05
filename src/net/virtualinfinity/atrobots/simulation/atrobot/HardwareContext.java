package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.computer.CommunicationsQueue;
import net.virtualinfinity.atrobots.computer.MemoryArray;
import net.virtualinfinity.atrobots.computer.SpecialRegister;
import net.virtualinfinity.atrobots.interrupts.AtRobotInterruptFactory;
import net.virtualinfinity.atrobots.interrupts.InterruptHandler;
import net.virtualinfinity.atrobots.ports.AtRobotPortFactory;
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
        robot.getHeat().setCoolMultiplier(coolMultiplier);
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
        wireMissileLauncher();
        wireScanner();
        wireHardwareBus(arena, totalRounds, roundNumber);
        connectSpecialRegisters();
    }

    private void connectSpecialRegisters() {
        lowerMemoryArray.addSpecialRegister(0, new SpecialRegister() {
            public short get() {
                return (short) robot.getThrottle().getDesiredPower();
            }
        });
        lowerMemoryArray.addSpecialRegister(1, new SpecialRegister() {
            public short get() {
                return (short) (hardwareBus.getDesiredHeading().getAngle().getBygrees() & 255);
            }
        });
        lowerMemoryArray.addSpecialRegister(2, new SpecialRegister() {
            public short get() {
                return (short) robot.getTurretShift();
            }
        });
        lowerMemoryArray.addSpecialRegister(3, new SpecialRegister() {
            public short get() {
                return (short) robot.getTurret().getScanner().getAccuracy();
            }
        });
        lowerMemoryArray.addSpecialRegister(9, new SpecialRegister() {
            public short get() {
                return (short) Math.round(robot.getOdometer().getDistance());
            }
        });
    }

    private void wireHardwareBus(Arena arena, int totalRounds, int roundNumber) {
        hardwareBus.connectToRobot(robot);
        hardwareBus.setPorts(createPortHandlers());
        hardwareBus.setInterrupts(createInterruptHandlers(arena, totalRounds, roundNumber));
        hardwareBus.addResetable(robot.getTurret().getScanner());
        hardwareBus.addResetable(robot.getTurret());
        hardwareBus.addResetable(robot.getOdometer());
        hardwareBus.addResetable(robot);
        hardwareBus.addResetable(robot.getShield());
        hardwareBus.connectComputer(robot.getComputer());
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

    private void wireMissileLauncher() {
        missileLauncher.setHeading(turret.getHeading());
        missileLauncher.setPower(missileLauncherPower);
        missileLauncher.setRobot(robot);
        missileLauncher.setPosition(robot.getPosition());
    }

    private void wireScanner() {
        turret.setScanner(scanner);
        scanner.setRobot(robot);
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
        sonar.setRobot(robot);
    }

    private void wireShield() {
        robot.setShield(shield);
        robot.getShield().setRobot(robot);
    }

    private void wireRadar() {
        radar.setRobot(robot);
        robot.setRadar(radar);
    }

    private void wireMineLayer() {
        robot.setMineLayer(mineLayer);
        mineLayer.setRobot(robot);
    }

    private void wireArmor() {
        robot.setArmor(armor);
        armor.setRobot(robot);
    }

    private void wireThrottle() {
        robot.setThrottle(throttle);
        throttle.setRobot(robot);
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
