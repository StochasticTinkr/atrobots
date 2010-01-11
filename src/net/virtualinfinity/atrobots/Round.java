package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Round {
    private Duration time = Duration.fromCycles(0);
    private Duration roundEnd = Duration.fromCycles(10);
    private Duration maxCycles = Duration.fromCycles(25000);
    private Arena arena = new Arena();
    private int number;
    private Game game;
    private Map<Entrant, Robot> robots = new HashMap<Entrant, Robot>();

    public Round(int number, Game game) {
        this.number = number;
        this.game = game;
    }

    public Duration getTime() {
        return time;
    }

    public Arena getArena() {
        return arena;
    }

    public int getNumber() {
        return number;
    }

    public void step() {
        if (arena.countActiveRobots() == 1) {
            roundEnd = roundEnd.minus(Duration.ONE_CYCLE);
        }
        if (roundEnd.getCycles() == 0 || arena.countActiveRobots() == 0 || time.compareTo(maxCycles) > 0) {
            game.roundOver();
            return;
        }
        arena.simulate();
        time = time.plus(Duration.ONE_CYCLE);

    }

    public void putRobot(Entrant entrant, Robot robot) {
        robots.put(entrant, robot);
    }

    public Robot getRobot(Entrant entrant) {
        return robots.get(entrant);
    }
}
