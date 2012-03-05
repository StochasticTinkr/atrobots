package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.simulation.arena.Arena;
import net.virtualinfinity.atrobots.simulation.arena.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Round {
    private Duration roundEnd = Duration.fromCycles(10);
    private Duration maxCycles = Duration.fromCycles(25000);
    private Arena arena;
    private int number;
    private Game game;
    private Map<Entrant, Robot> robots = new HashMap<Entrant, Robot>();

    public Round(int number, Game game, SimulationFrameBuffer frameBuffer) {
        this.number = number;
        this.game = game;
        arena = new Arena(game);
        arena.setSimulationFrameBuffer(frameBuffer);
    }

    public Arena getArena() {
        return arena;
    }

    public int getRoundNumber() {
        return number;
    }

    public void step() {
        if (arena.countActiveRobots() == 1) {
            roundEnd = roundEnd.minus(Duration.ONE_CYCLE);
        }
        if (roundEnd.getCycles() == 0 || arena.countActiveRobots() == 0 || arena.getTime().compareTo(maxCycles) > 0) {
            game.roundOver();
            return;
        }
        arena.simulate();

    }

    public void putRobot(Entrant entrant, Robot robot) {
        robots.put(entrant, robot);
    }

    public Robot getRobot(Entrant entrant) {
        return robots.get(entrant);
    }

    public void finalizeRound() {
        arena.endRound();
    }
}
