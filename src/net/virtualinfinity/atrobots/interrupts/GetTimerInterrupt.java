package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.GameTimer;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetTimerInterrupt extends InterruptHandler {
    private final MemoryCell lowCell;
    private final MemoryCell highCell;
    private final GameTimer gameTimer;

    public GetTimerInterrupt(MemoryCell lowCell, MemoryCell highCell, GameTimer gameTimer) {
        super();
        this.lowCell = lowCell;
        this.highCell = highCell;
        this.gameTimer = gameTimer;
    }

    public void handleInterrupt() {
        final int cycles = gameTimer.getTime().getCycles();
        lowCell.set((short) cycles);
        highCell.set((short) (cycles >>> 16));
    }
}
