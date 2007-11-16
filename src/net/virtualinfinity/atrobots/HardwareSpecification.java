package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.CommunicationsQueue;

import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class HardwareSpecification {
    private final Map<String, Integer> configs;

    public HardwareSpecification(Map<String, Integer> configs) {
        this.configs = configs;
    }

    public Armor createArmor() {
        return new Armor(chooseDoubleFor("armor", 50, 66, 100, 120, 130, 150));
    }

    public MineLayer createMineLayer() {
        return new MineLayer(chooseIntegerFor("mines", 2, 4, 6, 10, 16, 24));
    }

    public Radar createRadar() {
        return new Radar();
    }

    public Shield createShield() {
        return new Shield(chooseDoubleFor("shield", 1, 1, 1, 2.0 / 3, 1.0 / 2, 1.0 / 3));
    }

    public Sonar createSonar() {
        return new Sonar();
    }

    public Transceiver createTransceiver(Robot robot) {
        final Transceiver transceiver = new Transceiver();
        transceiver.setRadioDispatcher(robot.getEntrant().getGame().getRound().getArena().getRadioDispatcher());
        return transceiver;
    }

    public Transponder createTransponder() {
        return new Transponder();
    }

    public Turret createTurret() {
        return new Turret();
    }

    private Scanner createScanner() {
        return new Scanner(chooseDoubleFor("scanner", 250, 350, 500, 700, 1000, 1500));
    }

    private Throttle createThrottle() {
        return new Throttle(chooseDoubleFor("engine", 0.5, 0.8, 1.0, 1.12, 1.35, 1.50) *
                chooseDoubleFor("armor", 1.33, 1.20, 1.00, 0.85, 0.75, 0.66));
    }

    void configureRobot(Robot robot) {
        robot.setThrottle(createThrottle());
        robot.setArmor(createArmor());
        robot.getArmor().setRobot(robot);
        robot.setMineLayer(createMineLayer());
        robot.getMineLayer().setRobot(robot);
        robot.setRadar(createRadar());
        robot.setShield(createShield());
        robot.getShield().setRobot(robot);
        robot.setSonar(createSonar());
        robot.getSonar().setRobot(robot);
        robot.setTransceiver(createTransceiver(robot));
        robot.setTransponder(createTransponder());
        robot.setTurret(createTurret());
        robot.getTurret().getHeading().setRelation(robot.getHeading());
        robot.getTurret().setKeepshift(false);
        robot.getTurret().getHeading().setAngle(robot.getHeading().getAngle());
        robot.getTurret().setMissileLauncher(new MissileLauncher(robot, robot.getPosition(), robot.getTurret().getHeading()));
        robot.getMineLayer().setArena(robot.getEntrant().getGame().getRound().getArena());
        robot.getComputer().setCommQueue(new CommunicationsQueue(robot.getComputer().getCommQueueMemoryRegion(),
                robot.getComputer().getRegisters().getCommunicationQueueHead(),
                robot.getComputer().getRegisters().getCommunicationQueueTail()));
        robot.getTransceiver().setCommQueue(robot.getComputer().getCommQueue());
        robot.getComputer().getCommQueue().setComputerErrorHandler(robot.getComputer().getErrorHandler());
        robot.getComputer().getMemory().setErrorHandler(robot.getComputer().getErrorHandler());
        robot.getTurret().setScanner(createScanner());
        robot.getTurret().getScanner().setRobot(robot);

    }

    public double chooseDoubleFor(String name, double... values) {
        return values[Math.max(0, Math.min(configs.get(name), values.length))];
    }

    public int chooseIntegerFor(String name, int... values) {
        return values[Math.max(0, Math.min(configs.get(name), values.length))];
    }

    public <T> T chooseFor(String name, T... values) {
        return values[Math.max(0, Math.min(configs.get(name), values.length))];
    }

}
