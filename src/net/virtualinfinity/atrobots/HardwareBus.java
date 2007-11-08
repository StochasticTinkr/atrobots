package net.virtualinfinity.atrobots;

import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author Daniel Pitts
 */
public class HardwareBus {

    private final Map<Integer, PortHandler> ports;
    private final Map<Integer, InterruptHandler> interrupts;
    private final Collection<Resetable> resetables = new ArrayList<Resetable>();
    private final Heading desiredHeading = new Heading();

    public HardwareBus(Map<Integer, PortHandler> ports,
                       Map<Integer, InterruptHandler> interrupts) {
        this.ports = ports;
        this.interrupts = interrupts;
    }

    public void callInterrupt(int interruptNumber) {
        interrupts.get(interruptNumber).call();
    }

    public short readPort(int portNumber) {
        return ports.get(portNumber).read();
    }

    public void writePort(int portNumber, short value) {
        ports.get(portNumber).write(value);
    }

    public void reset() {
        for (Resetable resetable: resetables) {
            resetable.reset();
        }
    }

    public void addResetable(Resetable resetable) {
        resetables.add(resetable);
    }

    public Heading getDesiredHeading() {
        return desiredHeading;
    }
}
