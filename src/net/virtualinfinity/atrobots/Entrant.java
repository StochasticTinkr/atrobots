package net.virtualinfinity.atrobots;

import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Entrant {
    private int id;
    private Game game;
    private String name;
    private Program program;
    private HardwareSpecification hardwareSpecification;
    private int totalKills;
    private int roundKills;
    private int totalDeaths;


    public Robot createRobot() {
        final Robot robot = new Robot();
        robot.setEntrant(this);
        robot.setComputer(createComputer());
        hardwareSpecification.configureRobot(robot);
        robot.setHardwareBus(new HardwareBus());
        robot.getHardwareBus().setPorts(createPortHandlers(robot));
        robot.getHardwareBus().setInterrupts(createInterruptHandlers(robot));
        robot.getHardwareBus().addResetable(robot.getTurret().getScanner());
        robot.getHardwareBus().addResetable(robot.getTurret());
        robot.getHardwareBus().addResetable(robot.getOdometer());
        robot.getHardwareBus().addResetable(robot);
        robot.getHardwareBus().addResetable(robot.getShield());
        robot.getHardwareBus().setComputer(robot.getComputer());
        robot.getHardwareBus().setRobot(robot);
        return robot;
    }

    private Map<Integer, PortHandler> createPortHandlers(Robot robot) {
        return new AtRobotPortFactory(robot).createPortHandlers();
    }

    private Map<Integer, InterruptHandler> createInterruptHandlers(Robot robot) {
        return new AtRobotInterruptFactory(robot).createInterruptTable();
    }

    private Computer createComputer() {
        final Memory memory = new Memory();
        memory.addMemoryArray(new RandomAccessMemoryArray(1024));
        memory.addMemoryArray(program.createProgramMemory());
        return new Computer(memory, 256);
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getRoundKills() {
        return roundKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public Game getGame() {
        return game;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHardwareSpecification(HardwareSpecification hardwareSpecification) {
        this.hardwareSpecification = hardwareSpecification;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void setRoundKills(int roundKills) {
        this.roundKills = roundKills;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Program getProgram() {
        return program;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
