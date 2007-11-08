package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class CopyMemoryCellsInterrupt extends InterruptHandler {
    private final MemoryCell[] cells;

    public CopyMemoryCellsInterrupt(MemoryCell...cells) {
        this.cells = cells;
    }

    public void handleInterrupt() {
        for (int i = 0; i < cells.length; i+= 2){
            cells[i+1].set(cells[i].signed());
        }
    }
}
