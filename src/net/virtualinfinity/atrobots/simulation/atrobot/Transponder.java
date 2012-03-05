package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.ports.PortHandler;

/**
 * @author Daniel Pitts
 */
public class Transponder {
    private int id;

    public int getId() {
        return id;
    }

    public PortHandler getIdLatchPort() {
        return new PortHandler() {
            public short read() {
                return (short) getId();
            }

            public void write(short value) {
                setId(value);
            }
        };
    }

    public void setId(int id) {
        this.id = id;
    }
}
