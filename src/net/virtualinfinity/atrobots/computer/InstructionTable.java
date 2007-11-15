package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class InstructionTable {
    private final Instruction invalidMicrocodeInstruction = new InvalidMicrocodeInstruction(1);
    private final Instruction[] instructions = new Instruction[47];
    private final Instruction numberedLabelInstruction = new NoOperationInstruction(0);

    {
        instructions[0] = new NoOperationInstruction(1);
        instructions[1] = new AddInstruction(1);
        instructions[2] = new SubtractInstruction(1);
        instructions[3] = new BitwiseOrInstruction(1);
        instructions[4] = new BitwiseAndInstruction(1);
        instructions[5] = new BitwiseExclusiveOrInstruction(1);
        instructions[6] = new BitwiseNegationInstruction(1);
        instructions[7] = new MultiplyInstruction(10);
        instructions[8] = new DivideInstruction(10);
        instructions[9] = new ModuloInstruction(10);
        instructions[10] = new PopInstructionPointerInstruction(1);
        instructions[11] = new CallInstruction(1);
        instructions[12] = new JumpInstruction(1);
        instructions[13] = new JumpWhenLessInstruction(0);
        instructions[14] = new JumpWhenGreaterInstruction(0);
        instructions[15] = new JumpWhenNotEqualInstruction(0);
        instructions[16] = new JumpWhenEqualInstruction(0);
        instructions[17] = new SwapInstruction(3);
        instructions[18] = new SetCXInstruction(1);
        instructions[19] = new LoopInstruction(1);
        instructions[20] = new CompareInstruction(1);
        instructions[21] = new TestInstruction(2);
        instructions[22] = new MoveInstruction(1);
        instructions[23] = new AddressInstruction(2);
        instructions[24] = new GetInstruction(2);
        instructions[25] = new PutInstruction(2);
        instructions[26] = new CallInterruptInstruction(0);
        instructions[27] = new ReadPortInstruction(4);
        instructions[28] = new WritePortInstruction(4);
        instructions[29] = new DelayInstruction(0);
        instructions[30] = new PushInstruction(1);
        instructions[31] = new PopInstruction(1);
        instructions[32] = new ErrorInstruction(0);
        instructions[33] = new IncrementInstruction(1);
        instructions[34] = new DecrementInstruction(1);
        instructions[35] = new BitShiftLeftInstruction(1);
        instructions[36] = new BitShiftRightInstruction(1);
        instructions[37] = new BitRotateLeftInstruction(1);
        instructions[38] = new BitRotateRightInstruction(1);
        instructions[39] = new JumpWhenZeroInstruction(0);
        instructions[40] = new JumpWhenNotZeroInstruction(0);
        instructions[41] = new JumpWhenGreaterOrEqualInstruction(0);
        instructions[42] = new JumpWhenLessOrEqualInstruction(0);
        instructions[43] = new BitShiftLeftInstruction(1);
        instructions[44] = new SignedBitShiftRightInstruction(1);
        instructions[45] = new NegateInstruction(1);
        instructions[46] = new SetInstructionPointerInstruction(1);
    }

    public Instruction getInvalidMicrocodeInstruction() {
        return invalidMicrocodeInstruction;
    }

    public Instruction getInstruction(short value) {
        return value >= 0 && value < instructions.length ? instructions[value] :
                new UnknownInstruction(1);
    }

    public Instruction getNumberedLabelInstruction() {
        return numberedLabelInstruction;
    }
}

/*
NoOperation              1  NOP           NOP        Simply wastes a clock-cycle.
Add                      1  ADD           ADD V N    Adds      V+N, result stored in V
Subtract                 1  SUB           SUB V N    Subtracts V-N, result stored in V
BitwiseOr                1  OR            OR  V N    Bitwise OR,  V or  N, result stored in V
BitwiseAnd               1  AND           AND V N    Bitwise AND, V and N, result stored in V
BitwiseExclusiveOr       1  XOR           XOR V N    Bitwise XOR, V xor N, result stored in V
BitwiseNegation          1  NOT           NOT V      Bitwise NOT, not(V),  result stored in V
Multiply                10  MPY           MPY V N    Mulitplies V*N, result stored in V
Divide                  10  DIV           DIV V N    Divides    V/N, result stored in V (integer)
Modulo                  10  MOD           MOD V N    MOD's    V & N, result stored in V (modulus)
Return                   1  RET  RETURN   RET        Returns from a subroutine (pops the ip)
Call                     1  CALL GSB      CALL N     Calls subroutine at label #N (pushes ip)
Jump                     1  JMP  GOTO     JMP N      Jumps program (ip) to label #N
JumpWhenLess             0  JLS  JB       JLS N      Jumps to label N if last compare was <
JumpWhenGreater          0  JGR  JA       JGR N      Jumps to label N if last compare was >
JumpWhenNotEqual         0  JNE           JNE N      Jumps to label N if last compare was <>
JumpWhenEqual            0  JEQ  JE       JEQ N      Jumps to label N if last compare was =
Swap                     3  XCHG SWAP     XCHG V V   Exchanges the values of two variables
SetCX                    1  DO            DO N       Sets CX = N
Loop                     1  LOOP          LOOP N     Decrements CX, If CX>0 then Jumps to label N
Compare                  1  CMP           CMP N N    Compares two numbers, results in flags reg.
Test                     2  TEST          TEST N N   Ands two numbers, result not stored, flags set
Move                     1  MOV  SET      MOV V N    Sets V = N
Address                  2  LOC  ADDR     LOC V V    Sets first V = memory address of second V
Get                      2  GET           GET V N    Sets V = number from memory location N
Put                      2  PUT           PUT N1 N2  Sets memory location N2 = N1
CallInterrupt            0  INT           INT N      Executes interrupt number N
ReadPort                 4  IPO  IN       IPO N V    Inputs number from port N, result into V
WritePort                4  OPO  OUT      OPO N1 N2  Outputs N2 to port N1
Delay                    0  DEL  DELAY    DEL N      Equivelant to N NOPS.
Push                     1  PUSH          PUSH N     Puts N onto the stack (sp incremented)
Pop                      1  POP           POP  V     Removes a number from the stack, into V
Error                    0  ERR  ERROR    ERR N      Generate an error code, useful for debugging
Increment                1  INC           INC V      Increments V, (v=v+1)
Decrement                1  DEC           DEC V      Decrements V, (v=v-1)
BitShiftLeft             1  SHL           SHL V N    Bit-shifts  V left  N bit positions
BitShiftRight            1  SHR           SHR V N    Bit-shifts  V right N bit positions
BitRotateLeft            1  ROL           ROL V N    Bit-rotates V left  N bit positions
BitRotateRight           1  ROR           ROR V N    Bit-rotates V right N bit positions
JumpWhenZero             0  JZ            JZ  N      Jumps to label N if last compare was 0
JumpWhenNotZero          0  JNZ           JNZ N      Jumps to label N if last compare was not 0
JumpWhenNotLess          0  JAE  JGE      JAE N      Jumps to label N if last compare was >=
JumpWhenNotGreater       0  JBE  JLE      JBE N      Jumps to label N if last compare was <=
BitShiftLeft             1  SAL           SAL V N    Bit-Shifts, same as SHL
SignedBitShiftRight      1  SAR           SAR V N    Same as SHR, except preserves bit 15.
Negate                   1  NEG           NEG V      Negates V:   V = 0-V  (aka "two's compliment")
SetInstructionPointer    1  JTL           JTL N      Jumps to line N of compiled program.
 */
