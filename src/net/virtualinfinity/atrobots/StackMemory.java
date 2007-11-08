package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class StackMemory {
    private final MemoryArray stackMemory;
    private final MemoryCell stackPointer;

    public StackMemory(MemoryCell stackPointer, int stackSize) {
        this.stackPointer = stackPointer;
        stackMemory = new RandomAccessMemoryArray(stackSize);
    }

    public short pop() {
        final short value = stackMemory.get(stackPointer.signed());
        stackPointer.decrement();
        return value;
    }

    public void push(short value) {
        stackPointer.increment();
        stackMemory.put(stackPointer.signed(), value);
    }

    public void reset() {
        stackMemory.clear();
    }
}
