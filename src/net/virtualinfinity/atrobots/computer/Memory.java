package net.virtualinfinity.atrobots.computer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Memory {
    private final List<MemoryArray> arrays = new ArrayList<MemoryArray>();
    private static final DecrementMemoryOperation DECREMENT = new DecrementMemoryOperation();
    private static final IncrementMemoryOperation INCREMENT = new IncrementMemoryOperation();
    private ComputerErrorHandler errorHandler;

    public void or(int index, short value) {
        doOperation(index, new OrMemoryOperation(value));
    }

    public void and(int index, short value) {
        doOperation(index, new AndMemoryOperation(value));
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

    private interface MemoryOperation {
        int perform(MemoryArray array, int index);
    }

    private static class SetMemoryOperation implements MemoryOperation {

        final short value;

        private SetMemoryOperation(short value) {
            this.value = value;
        }

        public int perform(MemoryArray array, int index) {
            array.put(index, value);
            return 0;
        }

    }

    private static class OrMemoryOperation implements MemoryOperation {

        final short value;

        private OrMemoryOperation(short value) {
            this.value = value;
        }

        public int perform(MemoryArray array, int index) {
            array.or(index, value);
            return 0;
        }

    }

    private static class AndMemoryOperation implements MemoryOperation {

        final short value;

        private AndMemoryOperation(short value) {
            this.value = value;
        }

        public int perform(MemoryArray array, int index) {
            array.and(index, value);
            return 0;
        }

    }

    private static class GetMemoryOperation implements MemoryOperation {

        public int perform(MemoryArray array, int index) {
            return array.get(index);
        }

    }

    private static class DecrementMemoryOperation implements MemoryOperation {

        public int perform(MemoryArray array, int index) {
            array.decrement(index);
            return 0;
        }

    }

    private static class IncrementMemoryOperation implements MemoryOperation {

        public int perform(MemoryArray array, int index) {
            array.increment(index);
            return 0;
        }
    }

    public void addMemoryArray(MemoryArray array) {
        arrays.add(array);
        array.setErrorHandler(errorHandler);
    }

    public short get(int index) {
        return (short) doOperation(index, new GetMemoryOperation());
    }

    public int unsigned(int index) {
        return get(index) & 0xFFFF;
    }

    public void set(int index, short value) {
        doOperation(index, new SetMemoryOperation(value));
    }

    private int doOperation(int index, MemoryOperation operation) {
        for (MemoryArray array : arrays) {
            if (index < array.size()) {
                return operation.perform(array, index);
            }
            index -= array.size();
        }
        return 0;
    }

    public MemoryCell getCell(int index) {
        return new MemoryCell(this, index);
    }

    public void decrement(int index) {
        doOperation(index, DECREMENT);
    }

    public void increment(int index) {
        doOperation(index, INCREMENT);
    }

    public void setErrorHandler(ComputerErrorHandler errorHandler) {
        for (MemoryArray array : arrays) {
            array.setErrorHandler(errorHandler);
        }
        this.errorHandler = errorHandler;
    }
}
