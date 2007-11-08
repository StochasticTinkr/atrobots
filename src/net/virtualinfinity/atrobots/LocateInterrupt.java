package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class LocateInterrupt extends InterruptHandler {
    private final ArenaObject object;
    private final MemoryCell xHolder;
    private final MemoryCell yHolder;

    public LocateInterrupt(ArenaObject object, MemoryCell xHolder, MemoryCell yHolder) {
        this.object = object;
        this.xHolder = xHolder;
        this.yHolder = yHolder;
    }

    public void handleInterrupt() {
        xHolder.set((short)Math.round(object.getPosition().getX().getMeters()));
        yHolder.set((short)Math.round(object.getPosition().getY().getMeters()));
    }
}
