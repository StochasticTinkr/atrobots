package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.*;
import net.virtualinfinity.atrobots.measures.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Computer {
    private Entrant entrant;
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
    private int cyclesPerSimCycle = 5;
    private final ErrorHandler errorHandler = new ErrorHandler();
    private boolean shutDown;
    private int maxInstructionPointer;

    public Computer(Memory memory, int stackSize) {
        this.memory = memory;
        this.registers = new Registers(memory);
        this.stack = new StackMemory(registers.getStackPointerCell(), stackSize);
        this.program = new MemoryRegion(memory, 1024, 4096);
        instructionTable = new InstructionTable();
        Map<Integer, Integer> jumpTable = new HashMap<Integer, Integer>();
        maxInstructionPointer = program.size() / 4;
        for (int i = 0; i < maxInstructionPointer; ++i) {
            if (getMicrocodeAt(0, i) == Microcode.NumberedLabel) {
                jumpTable.put((int) getConstantAt(0, i), i);
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
        Computer.this.hardwareBus = hardwareBus;
    }

    public void useCycles() {
        while (cycles > 0 && !shutDown) {
            getHardwareBus().checkHeat();
            executeInstruction();
        }
        if (shutDown && cycles > 0) {
            cycles = 0;
        }
        getHardwareBus().checkHeat();
    }

    void executeInstruction() {
        hardwareBus.preInstruction();
        if (nextInstructionPointer >= maxInstructionPointer) {
            nextInstructionPointer = 0;
        }
        instructionPointer = nextInstructionPointer;
        nextInstructionPointer++;
//        if (entrant.getName().contains("RANDMAN4")) {
////            System.out.println(entrant.getId() + " "+entrant.getName() + ": ");
//            System.out.println(getInstructionString() + " ** " + registers);
//            System.out.println(getSourceLine());
//        }
        getInstruction().execute(this);
    }

    private String getSourceLine() {
        return entrant.getDebugInfo().getLineForInstructionPointer(instructionPointer);
    }

    public String getInstructionString() {
        return instructionPointer + ": "
                + getOperandString(0) + "  "
                + getOperandString(1) + ", "
                + getOperandString(2) + ":  "
//                + getInstruction().getClass().getSimpleName()
                ;
    }

    private String getOperandString(int opnumber) {
        return getMicrocode(opnumber).formatValue(this, opnumber);
    }

    public short getOperandValue(int opnumber) {
        return getMicrocode(opnumber).getValue(Computer.this, opnumber);
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
        return memory.get(Microcode.Dereference.getValue(Computer.this, opnumber));
    }

    private short getDeferencedValue(int opnumber) {
        return memory.get(Microcode.Constant.getValue(Computer.this, opnumber));
    }

    public Instruction getInstruction() {
        final Microcode microcode = getMicrocode(0);
        if (microcode == Microcode.NumberedLabel) {
            return instructionTable.getNumberedLabelInstruction();
        }
        if (!microcode.hasValue()) {
            return instructionTable.getInvalidMicrocodeInstruction();
        }
        return instructionTable.getInstruction(microcode.getValue(Computer.this, 0));
    }

    public void consumeCycles(int cost) {
        cycles -= cost;
    }

    public void setOperandValue(int opnumber, short value) {
        final Microcode microcode = getMicrocode(opnumber);
        if (microcode.isAddressible()) {
            memory.set(microcode.getAddress(Computer.this, opnumber), value);
        }
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
        microcode.setNextInstructionPointerFromLabel(Computer.this, 1);
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
        memory.increment(getMicrocode(opnumber).getAddress(Computer.this, opnumber));
    }

    public void decrementOperand(int opnumber) {
        memory.decrement(getMicrocode(opnumber).getAddress(Computer.this, opnumber));
    }

    public void push() {
        stack.push(getOperandValue(1));
    }

    public void pop() {
        setOperandValue(1, stack.pop());
    }

    public void readPort() {
        setOperandValue(2, hardwareBus.readPort(getOperandValue(1)));
    }

    public void writePort() {
        hardwareBus.writePort(getOperandValue(1), getOperandValue(2));
    }

    public int getOperandAddress(int opnumber) {
        return getMicrocode(opnumber).getAddress(Computer.this, opnumber);
    }

    public Memory getMemory() {
        return memory;
    }

    public void reset() {
        stack.reset();
        getHardwareBus().reset();
    }

    public PortHandler createDefaultPortHandler() {
        return new InvalidPort().setComputer(Computer.this);
    }

    public MemoryRegion getCommQueueMemoryRegion() {
        return new MemoryRegion(memory, 512, 256);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void update(Duration duration) {
        cycles += duration.getCycles() * getCyclesPerSimCycle();
        useCycles();
    }

    public int getCyclesPerSimCycle() {
        return cyclesPerSimCycle;
    }

    private void jumpToNumberedLabel(short value) {
        setInstructionPointer(jumpTable.get((int) value));
    }

    public void jumpToLine() {
        setInstructionPointer(getOperandValue(1));
    }

    private void setInstructionPointer(int instructionPointer) {
        Computer.this.nextInstructionPointer = instructionPointer;
    }

    public void setCommQueue(CommunicationsQueue commQueue) {
        Computer.this.commQueue = commQueue;
    }

    public CommunicationsQueue getCommQueue() {
        return commQueue;
    }

    public void invalidMicrocodeError() {
        errorHandler.invalidMicrocodeError();
    }

    public void divideByZeroError() {
        errorHandler.divideByZeroError();
    }

    public void genericError(short operandValue) {
        errorHandler.genericError(operandValue);
    }

    public void unknownInstructionError() {
        errorHandler.unknownInstructionError();
    }

    public void invalidInterruptError() {
        errorHandler.invalidInterruptError();
    }

    public void invalidPortError() {
        errorHandler.invalidPortError();
    }

    public void commQueueEmptyError() {
        errorHandler.commQueueEmptyError();
    }

    public void writeToRomError() {
        errorHandler.writeToRomError();
    }

    public void notAddressableError() {
        errorHandler.notAddressableError();
    }

    public void shutDown() {
        shutDown = true;
    }

    public boolean isShutDown() {
        return shutDown;
    }

    public void startUp() {
        shutDown = false;
    }

    public int getCycles() {
        return cycles;
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public int getNextInstructionPointer() {
        return nextInstructionPointer;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void setEntrant(Entrant entrant) {
        this.entrant = entrant;
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
            return codes[microcode & 15];
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
                    computer.setInstructionPointer(getValue(computer, opnumber));
                } else {
                    computer.jumpToNumberedLabel(getValue(computer, opnumber));
                }
            }
        }

        public String formatValue(Computer computer, int opnumber) {
            switch (this) {
                case DoubleDereference:
                    return "[@" + computer.getConstant(opnumber) +
                            "]=[" + computer.getDeferencedValue(opnumber) + "]=" +
                            computer.getDoubleDereferencedValue(opnumber);
                case Dereference:
                    return "@" + computer.getConstant(opnumber) + "=" + computer.getDeferencedValue(opnumber);
                case NumberedLabel:
                    return ":" + computer.getConstant(opnumber);
                case ResolvedLabel:
                    return "!" + computer.getConstant(opnumber);
                case UnresolvedLabel:
                    return "!?";
                case Constant:
                    return String.valueOf(computer.getConstant(opnumber));
                default:
                case Invalid:
                    return "??";
            }
        }
    }

    private class ErrorHandler implements ComputerErrorHandler {
        public void genericError(short operandValue) {
            System.out.println("error " + operandValue);
            // TODO: robot error
//            System.out.println("Computer$ErrorHandler.genericError");
        }

        public void unknownInstructionError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.unknownInstructionError");
        }

        public void invalidInterruptError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.invalidInterruptError");
        }

        public void notAddressableError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.notAddressableError");
        }

        public void invalidMicrocodeError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.invalidMicrocodeError");
        }

        public void divideByZeroError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.divideByZeroError");
        }

        public void invalidPortError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.invalidPortError");

        }

        public void commQueueEmptyError() {
            // TODO: robot error
//            System.out.println("Computer$ErrorHandler.commQueueEmptyError");
        }

        boolean inMethod = false;

        public void memoryBoundsError(int address) {
            // TODO: robot error
            if (inMethod) {
                System.out.println("Computer$ErrorHandler.memoryBoundsError: @" + address);
                return;
            }
            inMethod = true;
            System.out.println("Computer$ErrorHandler.memoryBoundsError: @" + address + ":  " + Computer.this.getInstructionString());
            inMethod = false;
        }

        public void writeToRomError() {
            // TODO: robot error
            System.out.println("Computer$ErrorHandler.writeToRomError");
        }
    }

    public StackMemory getStack() {
        return stack;
    }
}
