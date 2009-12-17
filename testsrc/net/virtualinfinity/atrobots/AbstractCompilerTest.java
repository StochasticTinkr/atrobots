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
    private MockGame game;
    protected Robot robot;

    public void setUp() throws IOException {
        entrantFactory = new EntrantFactory("test");
        out = new ByteArrayOutputStream(1024);
        source = new PrintStream(out);
        game = new MockGame();
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
                game.addEntrant(entrantFactory.createEntrant());
                game.nextRound();
                robot = game.lastRobotCreated;
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MockGame extends Game {
        private Robot lastRobotCreated;

        public MockGame() {
            super(1);
        }

        @Override
        protected Robot createRobotFor(Entrant entrant) {
            lastRobotCreated = super.createRobotFor(entrant);
            return lastRobotCreated;
        }
    }
}
