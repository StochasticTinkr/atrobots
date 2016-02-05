package net.virtualinfinity.atrobots.radio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class RadioDispatcher {
    private final Collection<RadioListener> radioListeners = new ArrayList<>();

    public void addRadioListener(RadioListener radioListener) {
        radioListeners.add(radioListener);
    }

    public void dispatch(RadioListener source, int channel, short value) {
        radioListeners.forEach(listener -> listener.radioSignal(source, channel, value));
    }
}
