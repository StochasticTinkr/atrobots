package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.computer.Memory;
import net.virtualinfinity.atrobots.computer.Program;
import net.virtualinfinity.atrobots.computer.RandomAccessMemoryArray;

/**
 * Represents an entrant in a game.
 *
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
    private DebugInfo debugInfo;
    private int maxProcessorSpeed = Integer.MAX_VALUE;


    /**
     * Create a robot.
     *
     * @return the robot to enter.
     */
    public Robot createRobot() {
        final Robot robot = new Robot();
        robot.setEntrant(this);
        robot.setArena(game.getRound().getArena());
        robot.setComputer(createComputer());
        HardwareContext hardwareContext = new HardwareContext();
        hardwareContext.setRobot(robot);
        hardwareSpecification.configureHardwareContext(hardwareContext);
        hardwareContext.wireRobotComponents();
        roundKills = 0;
        return robot;
    }

    private Computer createComputer() {
        final Memory memory = new Memory();
        memory.addMemoryArray(new RandomAccessMemoryArray(1024));
        memory.addMemoryArray(program.createProgramMemory());
        Computer computer = new Computer(memory, 256, Math.max(maxProcessorSpeed, game.getMaxProcessorSpeed()));
        computer.setEntrant(this);
        return computer;
    }

    /**
     * Get the total number of kills this entrant has earned during this game.
     *
     * @return the number of kills.
     */
    public int getTotalKills() {
        return totalKills;
    }

    /**
     * Get the total number of kills this entrant has earned during this round
     *
     * @return the number of kills.
     */
    public int getRoundKills() {
        return roundKills;
    }

    /**
     * Get the total number of times the entrant has died this game.
     *
     * @return the number of deaths.
     */
    public int getTotalDeaths() {
        return totalDeaths;
    }

    /**
     * Get the game this entrant is in.
     *
     * @return the game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Set the program to use for the robots this entrant creates.
     *
     * @param program the program.
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Set the game that this entrant will enter.
     *
     * @param game the game.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Set the display name of this entrant.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the hardware specifications to use for the robots.
     *
     * @param hardwareSpecification the specs.
     */
    public void setHardwareSpecification(HardwareSpecification hardwareSpecification) {
        this.hardwareSpecification = hardwareSpecification;
    }

    /**
     * Get the name of this entrant.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the unique-per-game identifier for this entrant.
     *
     * @return the ID.
     */
    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    /**
     * Record a kill.
     */
    public void incrementKills() {
        roundKills++;
        totalKills++;
    }

    /**
     * Get the debugging information.
     *
     * @return the debugging information.
     */
    public DebugInfo getDebugInfo() {
        return debugInfo;
    }

    void setDebugInfo(DebugInfo debugInfo) {
        this.debugInfo = debugInfo;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }

    public void setMaxProcessorSpeed(int maxProcessorSpeed) {
        this.maxProcessorSpeed = maxProcessorSpeed;
    }
}
