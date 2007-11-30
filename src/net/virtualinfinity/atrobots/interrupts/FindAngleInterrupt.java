package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.ArenaObject;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Distance;

/**
 * @author Daniel Pitts
 */
public class FindAngleInterrupt extends InterruptHandler {
    private final MemoryCell x;
    private final MemoryCell y;
    private final MemoryCell destination;
    private final ArenaObject object;

    public FindAngleInterrupt(MemoryCell x, MemoryCell y, MemoryCell destination, ArenaObject object) {
        this.x = x;
        this.y = y;
        this.destination = destination;
        this.object = object;
    }

    public void handleInterrupt() {
        destination.set(
                (short) AbsoluteAngle.fromCartesian(
                        Distance.fromMeters(x.signed()).minus(object.getPosition().getX()),
                        Distance.fromMeters(y.signed()).minus(object.getPosition().getY())).getBygrees()
        );
    }
}
