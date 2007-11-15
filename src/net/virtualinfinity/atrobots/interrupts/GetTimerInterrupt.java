package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetTimerInterrupt extends InterruptHandler {
    private final Game game;
    private final MemoryCell lowCell;
    private final MemoryCell highCell;

    public GetTimerInterrupt(Game game, MemoryCell lowCell, MemoryCell highCell) {
        super();
        this.game = game;
        this.lowCell = lowCell;
        this.highCell = highCell;
    }

    public void handleInterrupt() {
        final int cycles = game.getRound().getTime().getCycles();
        lowCell.set((short) cycles);
        highCell.set((short) (cycles >>> 16));
    }
}
