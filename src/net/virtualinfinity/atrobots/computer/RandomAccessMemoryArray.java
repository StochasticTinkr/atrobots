package net.virtualinfinity.atrobots.computer;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class RandomAccessMemoryArray extends MemoryArray {
    public RandomAccessMemoryArray(int blockSize) {
        super(blockSize);
    }

    public RandomAccessMemoryArray(int blockSize, Map<Integer, SpecialRegister> specialRegisters) {
        super(blockSize, specialRegisters);
    }

    public void put(int index, short value) {
        cells[index] = value;
    }

    public void decrement(int index) {
        --cells[index];
    }

    public void increment(int index) {
        ++cells[index];
    }

    public void or(int index, short value) {
        cells[index] |= value;
    }

    public void and(int index, short value) {
        cells[index] &= value;
    }

    public void clear() {
        Arrays.fill(cells, (short) 0);
    }
}
