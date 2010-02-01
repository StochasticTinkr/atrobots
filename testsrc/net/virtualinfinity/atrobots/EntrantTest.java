package net.virtualinfinity.atrobots;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class EntrantTest extends TestCase {

    public void testSittingDuck() throws IOException {
        final Entrant entrant = new net.virtualinfinity.atrobots.compiler.Compiler().compile(new File("original/SDUCK.AT2")).createEntrant("sduck");
        final Game game = new Game(1);
        entrant.setGame(game);
        game.nextRound();
        entrant.createRobot();

    }
}
