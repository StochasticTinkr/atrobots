package net.virtualinfinity.atrobots.ports;

import net.virtualinfinity.atrobots.computer.Computer;

/**
 * @author Daniel Pitts
 */
public abstract class PortHandler {
    private Computer computer;

    public short read() {
        computer.invalidPortError();
        return 0;
    }

    public void write(short value) {
        computer.invalidPortError();
    }

    public Computer getComputer() {
        return computer;
    }

    public PortHandler setComputer(Computer computer) {
        this.computer = computer;
        return this;
    }
}
