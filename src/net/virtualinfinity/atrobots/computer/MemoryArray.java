package net.virtualinfinity.atrobots.computer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public abstract class MemoryArray {
    private ComputerErrorHandler errorHandler;
    private final Map<Integer, SpecialRegister> specialRegisters;
    protected final short[] cells;

    public MemoryArray(int blockSize) {
        this(blockSize, new HashMap<Integer, SpecialRegister>());
    }

    public MemoryArray(int blockSize, Map<Integer, SpecialRegister> specialRegisters) {
        this.specialRegisters = specialRegisters;
        cells = new short[blockSize];
    }

    public final int size() {
        return cells.length;
    }

    public final short get(int index) {
        SpecialRegister register = specialRegisters.get(index);
        if (register != null) {
            return register.get();
        }
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

    public void addSpecialRegister(int index, SpecialRegister specialRegister) {
        specialRegisters.put(index, specialRegister);
    }
}
