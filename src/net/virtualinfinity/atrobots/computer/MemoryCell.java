package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class MemoryCell {
    private final Memory memory;
    private final int index;

    public MemoryCell(Memory memory, int index) {
        this.memory = memory;
        this.index = index;
    }

    public short signed() {
        return memory.get(index);
    }

    public int unsigned() {
        return memory.unsigned(index);
    }

    public void set(short value) {
        memory.set(index, value);
    }

    public void decrement() {
        memory.decrement(index);
    }

    public void increment() {
        memory.increment(index);
    }

    public void or(short value) {
        memory.or(index, value);
    }

    public void and(short value) {
        memory.and(index, value);
    }

    public String toString() {
        return "@" + index + "=" + this.signed();
    }
}
