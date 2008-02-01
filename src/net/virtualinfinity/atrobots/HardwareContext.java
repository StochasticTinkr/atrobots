package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.CommunicationsQueue;
import net.virtualinfinity.atrobots.interrupts.AtRobotInterruptFactory;
import net.virtualinfinity.atrobots.interrupts.InterruptHandler;

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

    public void wireRobotComponents() {
        robot.getHeat().setCoolMultiplier(coolMultiplier);
        wireThrottle();
        wireArmor();
        wireMineLayer();
        wireRadar();
        wireShield();
        wireSonar();
        final CommunicationsQueue commQueue = createCommQueue();
        wireTranceiver(commQueue);
        wireComputer(commQueue);
        wireTransponder();
        wireTurret();
        wireMissileLauncher();
        wireScanner();
        final HardwareBus hardwareBus = new HardwareBus();
        robot.setHardwareBus(hardwareBus);
        hardwareBus.setRobot(robot);
        hardwareBus.setPorts(createPortHandlers());
        hardwareBus.setInterrupts(createInterruptHandlers());
        hardwareBus.addResetable(robot.getTurret().getScanner());
        hardwareBus.addResetable(robot.getTurret());
        hardwareBus.addResetable(robot.getOdometer());
        hardwareBus.addResetable(robot);
        hardwareBus.addResetable(robot.getShield());
        hardwareBus.setComputer(robot.getComputer());
    }

    private Map<Integer, PortHandler> createPortHandlers() {
        return new AtRobotPortFactory(robot).createPortHandlers();
    }

    private Map<Integer, InterruptHandler> createInterruptHandlers() {
        return new AtRobotInterruptFactory(robot).createInterruptTable();
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
        transceiver.setRadioDispatcher(robot.getEntrant().getGame().getRound().getArena().getRadioDispatcher());
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
        robot.setRadar(radar);
    }

    private void wireMineLayer() {
        robot.setMineLayer(mineLayer);
        mineLayer.setRobot(robot);
        mineLayer.setArena(robot.getEntrant().getGame().getRound().getArena());
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
}
