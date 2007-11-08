package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ZeroMemoryCellsInterrupt extends InterruptHandler {
    private final MemoryCell[] cells;

    public ZeroMemoryCellsInterrupt(MemoryCell... cells) {
        this.cells = cells;
    }

    public void handleInterrupt() {
        for (MemoryCell cell: cells) {
            cell.set((short) 0);
        }
    }
}
