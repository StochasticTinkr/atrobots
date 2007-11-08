package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Shield implements Resetable {
    private boolean active;

    public PortHandler getLatch() {
        return new PortHandler() {
            public short read() {
                return (short) (isActive() ? 1 : 0);
            }

            public void write(short value) {
                setActive(value != 0);
            }
        };
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void reset() {
        setActive(false);
    }
}
