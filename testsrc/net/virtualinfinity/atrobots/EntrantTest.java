package net.virtualinfinity.atrobots;

import junit.framework.TestCase;
import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class EntrantTest extends TestCase {

    public void testSittingDuck() throws IOException {
        final Entrant entrant = new AtRobotCompiler().compile(new File("original/SDUCK.AT2")).createEntrant("sduck");
        final Game game = new Game(1);
        entrant.setGame(game);
        game.nextRound();
        entrant.createRobot();

    }
}
