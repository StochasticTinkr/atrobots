package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Flags {
    private final MemoryCell flagCell;

    public Flags(MemoryCell flagCell) {
        this.flagCell = flagCell;
    }

    public boolean isLess() {
        return hasBitInMask(2);
    }

    public void setLess(boolean less) {
        optionallySetBitsInMask(less, 2);
    }

    public boolean isGreater() {
        return hasBitInMask(4);
    }

    public void setGreater(boolean greater) {
        optionallySetBitsInMask(greater, 4);
    }

    public boolean isEqual() {
        return hasBitInMask(1);
    }

    public void setEqual(boolean equal) {
        optionallySetBitsInMask(equal, 1);
    }

    public boolean isZero() {
        return hasBitInMask(8);
    }

    public void setZero(boolean zero) {
        optionallySetBitsInMask(zero, 8);
    }

    private boolean hasBitInMask(int bitMask) {
        return 0 != (flagCell.unsigned() & bitMask);
    }

    private void optionallySetBitsInMask(boolean shouldSet, int bitMask) {
        if (shouldSet) {
            addBitsInMask(bitMask);
        }
    }

    private void addBitsInMask(int bitMask) {
        flagCell.or((short) bitMask);
    }

    public void reset() {
        flagCell.and((short) 0xFFF0);
    }
}
