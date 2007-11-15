package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetGameInfoInterrupt extends InterruptHandler {
    private final Game game;
    private final MemoryCell activeRobots;
    private final MemoryCell currentRound;
    private final MemoryCell totalRounds;

    public GetGameInfoInterrupt(Game game, MemoryCell activeRobots, MemoryCell currentRound, MemoryCell totalRounds) {
        this.game = game;
        this.activeRobots = activeRobots;
        this.currentRound = currentRound;
        this.totalRounds = totalRounds;
    }

    public void handleInterrupt() {
        activeRobots.set((short) game.getRound().getArena().countActiveRobots());
        currentRound.set((short) game.getRound().getNumber());
        totalRounds.set((short) game.getTotalRounds());
    }
}
