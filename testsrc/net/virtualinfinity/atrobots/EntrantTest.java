package net.virtualinfinity.atrobots;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class EntrantTest extends TestCase {

    public void testSittingDuck() throws IOException {
        final EntrantFactory entrantFactory = new EntrantFactory(new File("original/SDUCK.AT2"));
        entrantFactory.compile();
        final Entrant entrant = entrantFactory.createEntrant();
        final Game game = new Game(1);
        entrant.setGame(game);
        game.nextRound();
        entrant.createRobot();

    }
}
