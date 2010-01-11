package net.virtualinfinity.atrobots;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Daniel Pitts
 */
public class AbstractCompilerTest extends TestCase {
    protected EntrantFactory entrantFactory;
    protected PrintStream source;
    private ByteArrayOutputStream out;
    private Game game;
    protected Robot robot;

    public void setUp() throws IOException {
        entrantFactory = new EntrantFactory("test");
        out = new ByteArrayOutputStream(1024);
        source = new PrintStream(out);
        game = new Game(1);
    }

    public void tearDown() throws IOException {
        out.close();
        source.close();

    }

    public void testNothing() {
        assertTrue(true);
    }

    protected void compile() {
        try {
            out.flush();
            final ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
            try {
                entrantFactory.compile(stream).dumpErrors();
                final Entrant entrant = entrantFactory.createEntrant();
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
