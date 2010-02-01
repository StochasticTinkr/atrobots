package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;
import net.virtualinfinity.atrobots.computer.Memory;
import net.virtualinfinity.atrobots.computer.Program;
import net.virtualinfinity.atrobots.computer.RandomAccessMemoryArray;
import net.virtualinfinity.atrobots.debugger.*;

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
    private boolean debug;
    private static final Debugger DEBUGGER = DebugConsole.create(getSystemConsole()).getDebugger();
    private String message = "";

    private static Console getSystemConsole() {
        return new ConsoleImpl(new ReaderConsoleInput(System.in), new PrintStreamConsoleOutput(System.out), new PrintStreamConsoleOutput(System.err));
    }

    /**
     * Create a robot.
     *
     * @return the robot to enter.
     */
    public Robot createRobot() {
        final Robot robot = new Robot();
        robot.setEntrant(this);
        robot.setArena(game.getRound().getArena());
        final RandomAccessMemoryArray lowerMemoryBlock = new RandomAccessMemoryArray(1024);
        robot.setComputer(createComputer(lowerMemoryBlock));
        HardwareContext hardwareContext = new HardwareContext();
        hardwareContext.setRobot(robot);
        hardwareSpecification.configureHardwareContext(hardwareContext);
        hardwareContext.setLowerMemoryArray(lowerMemoryBlock);
        hardwareContext.wireRobotComponents();
        if (debug) {
            robot.getComputer().setDebugListener(DEBUGGER);
        }
        roundKills = 0;

        return robot;
    }

    private Computer createComputer(RandomAccessMemoryArray lowerMemoryBlock) {
        final Memory memory = new Memory();
        memory.addMemoryArray(lowerMemoryBlock);
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

    public void setDebugInfo(DebugInfo debugInfo) {
        this.debugInfo = debugInfo;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }

    public void setMaxProcessorSpeed(int maxProcessorSpeed) {
        this.maxProcessorSpeed = maxProcessorSpeed;
    }

    public Robot getCurrentRobot() {
        return getGame().getRound().getRobot(this);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
