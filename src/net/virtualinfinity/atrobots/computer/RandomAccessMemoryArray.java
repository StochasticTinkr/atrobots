package net.virtualinfinity.atrobots.computer;

import java.util.Arrays;

/**
 * @author Daniel Pitts
 */
public class RandomAccessMemoryArray extends MemoryArray {
    public RandomAccessMemoryArray(int blockSize) {
        super(blockSize);
    }

    public void put(int index, short value) {
        if (inRange(index)) {
            // TODO: Error
        } else
            cells[index] = value;

    }

    public void decrement(int index) {
        if (inRange(index)) {
            // TODO: Error
        } else
            --cells[index];
    }

    public void increment(int index) {
        if (inRange(index)) {
            // TODO: Error
        } else
            ++cells[index];
    }

    public void or(int index, short value) {
        if (inRange(index)) {
            // TODO: Error
        } else
            cells[index] |= value;
    }

    public void and(int index, short value) {
        if (inRange(index)) {
            // TODO: Error
        } else
            cells[index] &= value;
    }

    public void clear() {
        Arrays.fill(cells, (short) 0);
    }
}
