package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.arena.ScanResult;

/**
 * @author Daniel Pitts
 */
public class LastScanResult {
    private ScanResult scanResult;

    public void set(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public boolean isSet() {
        return scanResult != null;
    }

    public ScanResult get() {
        return scanResult;
    }
}
