package net.virtualinfinity.atrobots.tournament;

import net.virtualinfinity.atrobots.arena.FrameBuilder;
import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.game.Game;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public class Tournament {
    private List<RobotFactory> competitors;
    private int roundsPerPairing = 25;
    private FrameBuilder frameBuffer;

    public Tournament() {
        frameBuffer = new FrameBuilder();
    }

    public FrameBuilder getFrameBuffer() {
        return frameBuffer;
    }

    public TournamentResults run() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        final TournamentResults results = new TournamentResults();
        for (final RobotFactory left : competitors) {
            for (final RobotFactory right : competitors) {
                if (right == left) {
                    break;
                }
                compete(results, left, right);
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        return results;
    }

    public List<RobotFactory> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<RobotFactory> competitors) {
        this.competitors = competitors;
    }

    public int getRoundsPerPairing() {
        return roundsPerPairing;
    }

    public void setRoundsPerPairing(int roundsPerPairing) {
        this.roundsPerPairing = roundsPerPairing;
    }

    private void compete(TournamentResults results, RobotFactory left, RobotFactory right) {
        System.out.println("Running pairing: " + left.getName() + " vs " + right.getName());
        final Game game = new Game(roundsPerPairing, frameBuffer);
        game.addEntrant(left);
        game.addEntrant(right);
        game.nextRound();
        while (game.stepRound()) {

        }
        results.recordScores(game, left, right);
    }
}
