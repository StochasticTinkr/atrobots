package net.virtualinfinity.atrobots;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Daniel Pitts
 */
public abstract class AbstractCompilerTest extends TestCase {
    protected Compiler compiler;
    protected PrintStream source;
    private ByteArrayOutputStream out;
    private Game game;
    protected Robot robot;
    protected CompilerOutput compilerOutput;

    public void setUp() throws IOException {
        compiler = new Compiler();
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
