package net.virtualinfinity.atrobots.computer;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a block of memory of some type (RAM or ROM).
 *
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

    /**
     * Use a special register for the given address.  A special register isn't read from memory,
     * but from some external system.  Generially these are read-only, but writing is not an error.
     *
     * @param address         the address which will be read specially.
     * @param specialRegister the handler which returns the special value.
     */
    public void addSpecialRegister(int address, SpecialRegister specialRegister) {
        specialRegisters.put(address, specialRegister);
    }
}
