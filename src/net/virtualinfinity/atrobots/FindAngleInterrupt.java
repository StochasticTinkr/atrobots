package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class FindAngleInterrupt extends InterruptHandler {
    private final MemoryCell x;
    private final MemoryCell y;
    private final MemoryCell destination;

    public FindAngleInterrupt(MemoryCell x, MemoryCell y, MemoryCell destination) {
        this.x = x;
        this.y = y;
        this.destination = destination;
    }

    public void handleInterrupt() {
        destination.set(
                (short) AbsoluteAngle.fromCartesian(Distance.fromMeters(x.signed()), Distance.fromMeters(y.signed())).getBygrees()
        );
    }
}
