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
    private final FrameBuilder frameBuffer = new FrameBuilder();
    private final long delay;
    private final ExecutorService executorService;

    public Tournament() {
        this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1), 0);
    }

    public Tournament(long delay) {
        this(Executors.newSingleThreadExecutor(), delay);
    }

    public Tournament(ExecutorService executorService, long delay) {
        this.executorService = executorService;
        this.delay = delay;
    }

    public TournamentResults run() throws InterruptedException {
        final TournamentResults results = new TournamentResults();
        for (final RobotFactory left : competitors) {
            for (final RobotFactory right : competitors) {
                if (right == left) {
                    break;
                }
                executorService.submit(new Runnable() {
                    public void run() {
                        try {
                            compete(results, left, right);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
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

    private void compete(TournamentResults results, RobotFactory left, RobotFactory right) throws InterruptedException {
        System.out.println("Running pairing: " + left.getName() + " vs " + right.getName());
        final Game game = new Game(roundsPerPairing, frameBuffer);
        game.addEntrant(left);
        game.addEntrant(right);
        game.nextRound();
        while (game.stepRound()) {
            if (delay > 0) {
                Thread.sleep(delay);
            }
        }
        results.recordScores(game, left, right);
    }

    public FrameBuilder getFrameBuffer() {
        return frameBuffer;
    }
}
