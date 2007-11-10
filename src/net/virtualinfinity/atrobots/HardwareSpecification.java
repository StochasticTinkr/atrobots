package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class HardwareSpecification {
    public Armor createArmor() {
        return new Armor();
    }

    public MineLayer createMineLayer() {
        return new MineLayer();
    }

    public Radar createRadar() {
        return new Radar();
    }

    public Shield createShield() {
        return new Shield();
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

    void configureRobot(Robot robot) {
        robot.setArmor(createArmor());
        robot.setMineLayer(createMineLayer());
        robot.setRadar(createRadar());
        robot.setShield(createShield());
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
        robot.getTurret().setScanner(new Scanner());
        robot.getTurret().getScanner().setRobot(robot);

    }

}
