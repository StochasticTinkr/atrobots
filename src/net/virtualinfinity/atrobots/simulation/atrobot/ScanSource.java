package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.simulation.arena.ScanResult;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface ScanSource {
    ScanResult scan(AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy);
}
