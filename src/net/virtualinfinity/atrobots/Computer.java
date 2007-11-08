package net.virtualinfinity.atrobots;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Computer implements ComputerErrorHandler {
    private Memory memory;
    private Registers registers;
    private StackMemory stack;
    private MemoryRegion program;
    private int cycles;
    private int instructionPointer;
    private int nextInstructionPointer;
    private InstructionTable instructionTable;
    private HardwareBus hardwareBus;
    private Map<Integer, Integer> jumpTable;
    private CommunicationsQueue commQueue;

    public Computer(Memory memory, int stackSize) {
        this.memory = memory;
        this.registers = new Registers(memory);
        this.stack = new StackMemory(registers.getStackPointerCell(), stackSize);
        this.program = new MemoryRegion(memory, 1024, 4096);
        instructionTable = new InstructionTable();
        Map<Integer, Integer> jumpTable = new HashMap<Integer, Integer>();
        final int maxIP = program.size() / 4;
        for (int i = 0; i < maxIP; ++i) {
            if (getMicrocodeAt(0, i) == Microcode.NumberedLabel) {
                jumpTable.put((int)getConstantAt(0, i), i);
            }
        }
        if (jumpTable.isEmpty()) {
            this.jumpTable = Collections.emptyMap();
        } else {
            this.jumpTable = jumpTable;
        }
    }

    public HardwareBus getHardwareBus() {
        return hardwareBus;
    }

    public void setHardwareBus(HardwareBus hardwareBus) {
        this.hardwareBus = hardwareBus;
    }

    public void useCycles() {
        while (cycles > 0) {
            executeInstruction();
        }
    }

    private void executeInstruction() {
        instructionPointer = nextInstructionPointer;
        nextInstructionPointer++;
        getInstruction().execute(this);
    }

    public short getOperandValue(int opnumber) {
        return getMicrocode(opnumber).getValue(this, opnumber);
    }

    private Microcode getMicrocode(int opnumber) {
        return Microcode.get(getConstant(3) >> (4 * opnumber));
    }
    private Microcode getMicrocodeAt(int opnumber, int pointer) {
        return Microcode.get(getConstantAt(3, pointer) >> (4 * opnumber));
    }

    private short getConstant(int opnumber) {
        return getConstantAt(opnumber, instructionPointer);
    }

    private short getConstantAt(int opnumber, int pointer) {
        return program.get(pointer * 4 + opnumber);
    }

    private short getUnresolvedLabelValue(int opnumber) {
        return getConstant(opnumber);
    }

    private short getLabelValue(int opnumber) {
        return getConstant(opnumber);
    }

    private short getNumberedLabelValue(int opnumber) {
        return getConstant(opnumber);
    }

    private short getDoubleDereferencedValue(int opnumber) {
        return memory.get(Microcode.Dereference.getValue(this, opnumber));
    }

    private short getDeferencedValue(int opnumber) {
        return memory.get(Microcode.Constant.getValue(this, opnumber));
    }

    public Instruction getInstruction() {
        final Microcode microcode = getMicrocode(0);
        if (!microcode.hasValue()) {
            return instructionTable.getInvalidMicrocodeInstruction();
        }
        if (microcode == Microcode.NumberedLabel) {
            return instructionTable.getNumberedLabelInstruction();
        }
        return instructionTable.getInstruction(microcode.getValue(this, 0));
    }

    public void consumeCycles(int cost) {
        cycles -= cost;
    }

    public void invalidMicrocodeError() {
        // TODO: robot error
    }

    public void setOperandValue(int opnumber, short value) {
        final Microcode microcode = getMicrocode(opnumber);
        if (microcode.isAddressible()) {
            memory.set(microcode.getAddress(this, opnumber), value);
        }
    }

    public void divideByZeroError() {
        // TODO: robot error
    }

    public void popInstructionPointer() {
        nextInstructionPointer = stack.pop();
    }

    public void call() {
        stack.push((short) nextInstructionPointer);
        jump();
    }

    public void jump() {
        final Microcode microcode = getMicrocode(1);
        microcode.setNextInstructionPointerFromLabel(this, 1);
    }

    public Flags getFlags() {
        return registers.getFlags();
    }

    public Registers getRegisters() {
        return registers;
    }

    public void callInterrupt() {
        hardwareBus.callInterrupt(getOperandValue(1));
    }

    public void incrementOperand(int opnumber) {
        memory.increment(getMicrocode(opnumber).getAddress(this, opnumber));
    }

    public void decrementOperand(int opnumber) {
        memory.decrement(getMicrocode(opnumber).getAddress(this, opnumber));
    }

    public void push() {
        stack.push(getOperandValue(1));
    }

    public void pop() {
        setOperandValue(1, stack.pop());
    }

    public void genericError(short operandValue) {
        // TODO: robot error
    }

    public void readPort() {
        setOperandValue(2, hardwareBus.readPort(getOperandValue(1)));
    }

    public void writePort() {
        hardwareBus.writePort(getOperandValue(1), getOperandValue(2));
    }

    public int getOperandAddress(int opnumber) {
        return getMicrocode(opnumber).getAddress(this, opnumber);
    }

    public Memory getMemory() {
        return memory;
    }

    public void unknownInstructionError() {
        // TODO: robot error
    }

    public void reset() {
        stack.reset();
        getHardwareBus().reset();
    }

    public void invalidInterruptError() {
        // TODO: robot error
    }

    public CommunicationsQueue getCommQueue() {
        return commQueue;
    }

    public void invalidPortError() {
        // TODO: robot error
    }

    public void commQueueEmptyError() {
        // TODO: robot error
    }

    public void memoryBoundsError() {
        // TODO: robot error
    }

    public void writeToRomError() {
        // TODO: robot error
    }

    PortHandler createDefaultPortHandler() {
        return new InvalidPort().setComputer(this);
    }

    public MemoryRegion getCommQueueMemoryRegion() {
        return new MemoryRegion(memory, 512, 256);
    }

    public ComputerErrorHandler getErrorHandler() {
        return this;
    }

    enum Microcode {
        Constant {
            public short getValue(Computer computer, int opnumber) {
                return computer.getConstant(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                computer.notAddressableError();
                return 0;
            }},
        Dereference {
            public short getValue(Computer computer, int opnumber) {
                return computer.getDeferencedValue(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                return computer.getConstant(opnumber);
            }},
        DoubleDereference {
            public short getValue(Computer computer, int opnumber) {
                return computer.getDoubleDereferencedValue(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                return computer.getDeferencedValue(opnumber);
            }},
        NumberedLabel {
            public short getValue(Computer computer, int opnumber) {
                return computer.getNumberedLabelValue(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                computer.notAddressableError();
                return 0;
            }},
        ResolvedLabel {
            public short getValue(Computer computer, int opnumber) {
                return computer.getLabelValue(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                computer.notAddressableError();
                return 0;
            }},
        UnresolvedLabel {
            public short getValue(Computer computer, int opnumber) {
                return computer.getUnresolvedLabelValue(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                computer.notAddressableError();
                return 0;
            }},
        Invalid {
            public short getValue(Computer computer, int opnumber) {
                computer.invalidMicrocodeError();
                return computer.getConstant(opnumber);
            }
            public int getAddress(Computer computer, int opnumber) {
                computer.notAddressableError();
                return 0;
            }};

        private static final Microcode[] codes = new Microcode[15];
        static {
            Arrays.fill(codes, Invalid);
            codes[0] = Constant;
            codes[1] = Dereference;
            codes[2] = NumberedLabel;
            codes[3] = UnresolvedLabel;
            codes[4] = ResolvedLabel;
            codes[8] = Dereference;
            codes[9] = DoubleDereference;
        }

        public static Microcode get(int microcode) {
            return codes[microcode&15];
        }

        public abstract short getValue(Computer computer, int opnumber);
        public boolean isValid() {
            return this != Invalid && this != UnresolvedLabel;
        }
        public boolean isAddressible() {
            return this == Dereference || this == DoubleDereference;
        }

        public boolean hasValue() {
            return isAddressible() || this == Constant;
        }

        public abstract int getAddress(Computer computer, int opnumber);

        public void setNextInstructionPointerFromLabel(Computer computer, int opnumber) {
            if (isValid()) {
                if (this == ResolvedLabel) {
                    computer.jumpToLine();
                } else {
                    computer.jumpToNumberedLabel();
                }
            }
        }
    }

    private void jumpToNumberedLabel() {
    }

    public void jumpToLine() {
        this.nextInstructionPointer = getConstant(1);
    }

    private void notAddressableError() {
        // TODO: robot error
    }

    public void setCommQueue(CommunicationsQueue commQueue) {
        this.commQueue = commQueue;
    }
}
