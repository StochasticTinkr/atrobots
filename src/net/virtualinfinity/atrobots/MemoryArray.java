package net.virtualinfinity.atrobots;

import java.util.Arrays;

/**
 * @author Daniel Pitts
 */
public abstract class MemoryArray {
    private ComputerErrorHandler errorHandler;
    protected final short[] cells;

    public MemoryArray(int blockSize) {
        cells = new short[blockSize];
    }

    public final int size() {
        return cells.length;
    }

    public final short get(int index) {
        return cells[index];
    }

    public abstract void put(int index, short value);

    public abstract void decrement(int index);

    public abstract void increment(int index);

    public abstract void or(int index, short value);

    public abstract void and(int index, short value);

    public abstract void clear();

    public ComputerErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ComputerErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
