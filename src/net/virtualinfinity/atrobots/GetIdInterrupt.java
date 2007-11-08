package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class GetIdInterrupt extends InterruptHandler {
    private final Transponder transponder;
    private final MemoryCell cell;

    public GetIdInterrupt(Transponder transponder, MemoryCell cell) {
        super();
        this.transponder = transponder;
        this.cell = cell;
    }

    public void handleInterrupt() {
        cell.set((short) transponder.getId());
    }
}
