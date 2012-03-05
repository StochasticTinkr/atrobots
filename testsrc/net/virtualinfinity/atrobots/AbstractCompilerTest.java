package net.virtualinfinity.atrobots;

import junit.framework.TestCase;
import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Daniel Pitts
 */
public abstract class AbstractCompilerTest extends TestCase {
    protected AtRobotCompiler compiler;
    protected PrintStream source;
    private ByteArrayOutputStream out;
    private Game game;
    protected Robot robot;
    protected AtRobotCompilerOutput compilerOutput;

    public void setUp() throws IOException {
        compiler = new AtRobotCompiler();
        out = new ByteArrayOutputStream(1024);
        source = new PrintStream(out);
        game = new Game(1);
    }

    public void tearDown() throws IOException {
        out.close();
        source.close();

    }

    protected void compile() {
        try {
            out.flush();
            final ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
            try {
                compilerOutput = compiler.compile(stream);
                compilerOutput.getErrors().dumpErrors();
                if (compilerOutput.hasErrors()) {
                    return;
                }
                final Entrant entrant = compilerOutput.createEntrant("test");
                game.addEntrant(entrant);
                game.nextRound();
                robot = game.getRound().getRobot(entrant);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
