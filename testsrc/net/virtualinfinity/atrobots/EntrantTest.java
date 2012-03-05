package net.virtualinfinity.atrobots;

import junit.framework.TestCase;
import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;

import java.io.File;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class EntrantTest extends TestCase {

    public void testSittingDuck() throws IOException {
        final AtRobotCompilerOutput compile = new AtRobotCompiler().compile(new File("original/SDUCK.AT2"));
        final Entrant entrant = Entrant.createEntrant("sduck", compile);
        final Game game = new Game(1);
        entrant.setGame(game);
        game.nextRound();
        entrant.createRobot(new RoundState(entrant.getGame().getRound().getArena(), entrant.getGame().getTotalRounds(), entrant.getGame().getRound().getRoundNumber(), entrant.getGame().getMaxProcessorSpeed()));

    }
}
