package net.virtualinfinity.atrobots;

import junit.framework.TestCase;
import net.virtualinfinity.atrobots.arena.Arena;
import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class RobotTest extends TestCase {

    public void testSittingDuck() throws IOException {
        final AtRobotCompilerOutput compile = new AtRobotCompiler().compile(new File("original/SDUCK.AT2"));
        final RobotFactory robotFactory = new RobotFactory(compile.getMessage(), compile.getProgram(), "sduck", compile.getMaxProcessorSpeed(), compile.getDebugInfo(), compile.getHardwareSpecification());
        final StandardRoundState roundState = new StandardRoundState(new Arena(), 1, 1);
        final Robot robot = robotFactory.createRobot(roundState, 5, new RobotScoreKeeper());
        assertNotNull(robot);

    }
}
