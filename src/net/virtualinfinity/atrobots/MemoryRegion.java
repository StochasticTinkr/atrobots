package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MemoryRegion {
    private final Memory memory;
    private final int startAddress;
    private final int length;

    public MemoryRegion(Memory memory, int startAddress, int length) {
        this.memory = memory;
        this.startAddress = startAddress;
        this.length = Math.min(length, memory.size() - startAddress);
    }

    public short get(int index) {
        if (rangeCheck(index)) {
            return memory.get(startAddress + index);
        }
        memory.getErrorHandler().memoryBoundsError();
        return 0;
    }

    private boolean rangeCheck(int index) {
        return index >= 0 && index < length;
    }

    public int unsigned(int index) {
        if (rangeCheck(index)) {
            return memory.unsigned(startAddress + index);
        }
        memory.getErrorHandler().memoryBoundsError();
        return 0;
    }

    public void set(int index, short value) {
        if (rangeCheck(index)) {
            memory.set(index, value);
        }
        memory.getErrorHandler().memoryBoundsError();
    }

    public MemoryCell getCell(int index) {
        if (rangeCheck(index)) {
            return memory.getCell(startAddress + index);
        }
        throw new IndexOutOfBoundsException(String.valueOf(index));
    }

    public int size() {
        return length;
    }
}
