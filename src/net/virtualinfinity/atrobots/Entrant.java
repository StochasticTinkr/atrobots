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
    private static final int ROBOT_STACK_SIZE = 256;
    private static final int LOWER_MEMORY_BLOCK_SIZE = 1024;
    private volatile int id;
    private final String name;
    private final Program program;
    private final HardwareSpecification hardwareSpecification;
    private final DebugInfo debugInfo;
    private final int maxProcessorSpeed;
    private volatile String message = "";
    private volatile Game game;
    private volatile int totalTies;
    private volatile int totalWins;
    private volatile int totalKills;
    private volatile int roundKills;
    private volatile int totalDeaths;
    private boolean debug;
    private static final Debugger DEBUGGER = DebugConsole.create(getSystemConsole()).getDebugger();

    public Entrant(String name, Program program, HardwareSpecification hardwareSpecification, DebugInfo debugInfo, int maxProcessorSpeed, String message) {
        this.name = name;
        this.program = program;
        this.hardwareSpecification = hardwareSpecification;
        this.debugInfo = debugInfo;
        this.maxProcessorSpeed = maxProcessorSpeed;
        this.message = message;
    }

    private static Console getSystemConsole() {
        return new ConsoleImpl(new ReaderConsoleInput(System.in), new PrintStreamConsoleOutput(System.out), new PrintStreamConsoleOutput(System.err));
    }

    /**
     * Create a robot.
     *
     * @return the robot to enter.
     */
    public Robot createRobot() {
        final Robot robot = new Robot(name, id, totalDeaths, totalKills, totalWins, totalTies);
        robot.addRobotListener(new RobotListener() {

            public void wonRound(Robot robot) {
                incrementWins();
            }

            public void tiedRound(Robot robot) {
                incrementTies();
            }

            public void killedRobot(Robot robot) {
                incrementKills();
            }

            public void died(Robot robot) {
                incrementDeaths();
            }
        });
        final RandomAccessMemoryArray lowerMemoryBlock = new RandomAccessMemoryArray(LOWER_MEMORY_BLOCK_SIZE);
        robot.setComputer(createComputer(lowerMemoryBlock));
        final HardwareContext hardwareContext = new HardwareContext();
        hardwareContext.setRobot(robot);
        hardwareSpecification.configureHardwareContext(hardwareContext);
        hardwareContext.setLowerMemoryArray(lowerMemoryBlock);
        hardwareContext.wireRobotComponents(game.getRound().getArena(), game.getTotalRounds(), game.getRound().getRoundNumber());
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
        final Computer computer = new Computer(memory, ROBOT_STACK_SIZE, getProcessorSpeed());
        computer.setEntrant(this);
        return computer;
    }

    private int getProcessorSpeed() {
        return Math.max(maxProcessorSpeed, game.getMaxProcessorSpeed());
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

    public int getTotalTies() {
        return totalTies;
    }

    public int getTotalWins() {
        return totalWins;
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
     * Set the game that this entrant will enter.
     *
     * @param game the game.
     */
    public void setGame(Game game) {
        this.game = game;
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
    public synchronized void incrementKills() {
        roundKills++;
        totalKills++;
    }

    /**
     * Record a win.
     */
    public synchronized void incrementWins() {
        totalWins++;
    }

    /**
     * Record a tie.
     */
    public synchronized void incrementTies() {
        totalTies++;
    }

    /**
     * Record a death.
     */
    public synchronized void incrementDeaths() {
        totalDeaths++;
    }


    /**
     * Get the debugging information.
     *
     * @return the debugging information.
     */
    public DebugInfo getDebugInfo() {
        return debugInfo;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
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
