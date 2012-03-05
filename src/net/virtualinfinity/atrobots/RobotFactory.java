package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.compiler.HardwareSpecification;
import net.virtualinfinity.atrobots.computer.*;
import net.virtualinfinity.atrobots.debugger.*;
import net.virtualinfinity.atrobots.simulation.atrobot.HardwareContext;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public class RobotFactory {
    private static final int ROBOT_STACK_SIZE = 256;
    private static final int LOWER_MEMORY_BLOCK_SIZE = 1024;
    private static final Debugger DEBUGGER = DebugConsole.create(getSystemConsole()).getDebugger();
    private volatile int id;
    protected final String name;
    protected final Program program;
    protected final HardwareSpecification hardwareSpecification;
    protected final DebugInfo debugInfo;
    protected final int maxProcessorSpeed;
    protected volatile String message = "";
    private boolean debug;
    private final RobotScoreKeeper robotScoreKeeper = new RobotScoreKeeper();

    public RobotFactory(String message, Program program, String name, int maxProcessorSpeed, DebugInfo debugInfo, HardwareSpecification hardwareSpecification) {
        this.message = message;
        this.program = program;
        this.name = name;
        this.maxProcessorSpeed = maxProcessorSpeed;
        this.debugInfo = debugInfo;
        this.hardwareSpecification = hardwareSpecification;
    }

    private static Console getSystemConsole() {
        return new ConsoleImpl(new ReaderConsoleInput(System.in), new PrintStreamConsoleOutput(System.out), new PrintStreamConsoleOutput(System.err));
    }

    /**
     * Create a robot.
     *
     * @param roundState
     * @return the robot to enter.
     */
    public Robot createRobot(RoundState roundState) {
        final Robot robot = new Robot(name, id, robotScoreKeeper);
        robot.addRobotListener(robotScoreKeeper);
        final RandomAccessMemoryArray lowerMemoryBlock = new RandomAccessMemoryArray(LOWER_MEMORY_BLOCK_SIZE);
        robot.setComputer(createComputer(lowerMemoryBlock, roundState.getMaxProcessorSpeed()));
        final HardwareContext hardwareContext = new HardwareContext();
        hardwareContext.setRobot(robot);
        hardwareSpecification.configureHardwareContext(hardwareContext);
        hardwareContext.setLowerMemoryArray(lowerMemoryBlock);
        hardwareContext.wireRobotComponents(roundState.getArena(), roundState.getTotalRounds(), roundState.getRoundNumber());
        if (debug) {
            robot.getComputer().setDebugListener(DEBUGGER);
        }
        return robot;
    }

    private Computer createComputer(RandomAccessMemoryArray lowerMemoryBlock, int maxProcessorSpeed) {
        return new Computer(createMemory(lowerMemoryBlock), ROBOT_STACK_SIZE, getProcessorSpeed(maxProcessorSpeed), debugInfo);
    }

    private Memory createMemory(RandomAccessMemoryArray lowerMemoryBlock) {
        final Memory memory = new Memory();
        memory.addMemoryArray(lowerMemoryBlock);
        memory.addMemoryArray(program.createProgramMemory());
        return memory;
    }

    private int getProcessorSpeed(int maxProcessorSpeed) {
        return Math.max(this.maxProcessorSpeed, maxProcessorSpeed);
    }

    void setId(int id) {
        this.id = id;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}
