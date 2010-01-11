package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public interface DebugListener {
    void beforeInstruction(Computer computer);

    void afterInstruction(Computer computer);
}
