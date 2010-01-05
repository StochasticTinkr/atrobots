package net.virtualinfinity.atrobots.computer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Memory {
    private final List<MemoryArray> arrays = new ArrayList<MemoryArray>();
    private ComputerErrorHandler errorHandler;

    public void or(int index, short value) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                array.or(index, value);
                return;
            }
            index -= array.size();
        }
    }

    public void and(int index, short value) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                array.and(index, value);
                return;
            }
            index -= array.size();
        }
    }

    public ComputerErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public int size() {
        int size = 0;
        for (MemoryArray array : arrays) {
            size += array.size();
        }
        return size;
    }

    public void addMemoryArray(MemoryArray array) {
        arrays.add(array);
        array.setErrorHandler(errorHandler);
    }

    public short get(int index) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                return array.get(index);
            }
            index -= array.size();
        }
        return 0;
    }

    public int unsigned(int index) {
        return get(index) & 0xFFFF;
    }

    public void set(int index, short value) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                array.put(index, value);
                return;
            }
            index -= array.size();
        }
    }

    public MemoryCell getCell(int index) {
        return new MemoryCell(this, index);
    }

    public void decrement(int index) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                array.decrement(index);
                return;
            }
            index -= array.size();
        }
    }

    public void increment(int index) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                array.increment(index);
                return;
            }
            index -= array.size();
        }
    }

    public void setErrorHandler(ComputerErrorHandler errorHandler) {
        for (MemoryArray array : arrays) {
            array.setErrorHandler(errorHandler);
        }
        this.errorHandler = errorHandler;
    }
}
