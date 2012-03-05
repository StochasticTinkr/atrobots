package net.virtualinfinity.atrobots.simulation.radio;

/**
 * @author Daniel Pitts
 */
public interface RadioListener {
    void radioSignal(RadioListener source, int channel, short value);
}
