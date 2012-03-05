package net.virtualinfinity.atrobots.ports;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface PortListener {
    void consumeCycles(int cost);

    void invalidPortError();
}
