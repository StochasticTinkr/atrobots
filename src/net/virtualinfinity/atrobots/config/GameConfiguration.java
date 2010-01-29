package net.virtualinfinity.atrobots.config;

import net.virtualinfinity.atrobots.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class GameConfiguration {
    private final List<EntrantSource> sources = new ArrayList<EntrantSource>();
    private int totalRounds;
    private int maxProcessorSpeed;

    public static class LoadedGame {
        private final Game game;
        private final Collection<Entrants> entrants;

        public LoadedGame(Game game, Collection<Entrants> entrants) {
            this.game = game;
            this.entrants = entrants;
        }
    }

    public LoadedGame createGame() throws IOException {
        final Game game = new Game(totalRounds);
        game.setMaxProcessorSpeed(maxProcessorSpeed);
        final Collection<Entrants> compiledEntrants = new ArrayList<Entrants>();
        for (EntrantSource source : sources) {
            compiledEntrants.add(source.createEntrants());
        }
        for (Entrants entrants : compiledEntrants) {
            game.addAll(entrants);
        }
        return new LoadedGame(game, compiledEntrants);
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }

    public void setMaxProcessorSpeed(int maxProcessorSpeed) {
        this.maxProcessorSpeed = maxProcessorSpeed;
    }
}
