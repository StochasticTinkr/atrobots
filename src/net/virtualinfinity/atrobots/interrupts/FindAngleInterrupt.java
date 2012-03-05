package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.simulation.arena.ArenaObject;


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
                        (x.signed()) - (object.getPosition().getX()),
                        (y.signed()) - (object.getPosition().getY())).getBygrees()
        );
    }
}
