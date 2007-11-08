package net.virtualinfinity.atrobots;

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
