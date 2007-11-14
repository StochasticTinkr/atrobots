package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class InstructionTest extends AbstractCompilerTest {
    public void testNOP() {
        executeFirst("NOP");
        assertEquals(-1, robot.getComputer().getCycles());
    }

    public void testADD() {
        assertBinaryOperation(5, 9, "ADD", 5 + 9);
    }

    public void testSUB() {
        assertBinaryOperation(5, 9, "SUB", 5 - 9);
    }

    public void testOR() {
        assertBinaryOperation(5, 9, "OR", 5 | 9);
    }

    public void testAND() {
        assertBinaryOperation(5, 9, "AND", 5 & 9);
    }

    public void testXOR() {
        assertBinaryOperation(5, 9, "XOR", 5 ^ 9);
    }

    public void testNOT() {
        assertBinaryOperation(9, 0, "NOT", ~9);
    }

    public void testNEG() {
        assertBinaryOperation(9, 0, "NEG", -9);
    }

    public void testMPY() {
        assertBinaryOperation(5, 9, "MPY", 5 * 9);
    }

    public void testDIV() {
        assertBinaryOperation(16, 3, "DIV", 16 / 3);
    }

    public void testMOD() {
        assertBinaryOperation(16, 3, "MOD", 16 % 3);
    }

    public void testPOP() {
        compile("POP AX");
        robot.getComputer().getStack().push((short) 3);
        executeInstruction();
        assertAX(3);
    }

    public void testPUSH() {
        executeFirst("PUSH 3");
        assertStackPointer(1);
        assertStackPop(3);
    }

    public void testCALL() {
        executeFirst("CALL !bob\nNOP\n!bob\n NOP");
        assertInstructionPointer(2);
        assertStackPointer(1);
        assertStackPop(1);
    }

    public void testJMP_label() {
        executeFirst("JMP !bob\nnop\nnop\n!bob\nnop");
        assertInstructionPointer(3);
    }

    public void testJMP_number() {
        executeFirst("JMP 7\n:1\nnop\n:7");
        assertInstructionPointer(3);
    }

    // TODO: Conditional Jump Tests

    public void testDO() {
        executeFirst("DO 9");
        assertCX(9);
    }

    public void testMOV() {
        executeFirst("MOV AX, 9");
        assertAX(9);
    }

    public void testINC() {
        executeFirst("INC AX");
        assertAX(1);
    }

    public void testDEC() {
        executeFirst("DEC AX");
        assertAX(-1);
    }

    public void testCMP() {
        compile("CMP 1, 2\n CMP 2, 1\n CMP 0,0\nCMP 1,1");
        executeInstruction();
        assertCompareInequal();
        assetCompareLess();

        executeInstruction();
        assertCompareInequal();
        assertCompareGreater();

        executeInstruction();
        assertFlagEqual();
        assertFlagZero();
        assertCompareNotLessAndNotGreater();

        executeInstruction();
        assertFlagEqual();
        assertFlagNotZero();
        assertCompareNotLessAndNotGreater();
    }

    private void assertCompareNotLessAndNotGreater() {
        assertFlagNotGreater();
        assertFlagNotLess();
    }

    private void assertCompareGreater() {
        assertFlagGreater();
        assertFlagNotLess();
    }

    private void assetCompareLess() {
        assertFlagNotGreater();
        assertFlagLess();
    }

    private void assertCompareInequal() {
        assertFlagsNotEqual();
        assertFlagNotZero();
    }

    private void assertFlagZero() {
        assertTrue(robot.getComputer().getFlags().isZero());
    }

    private void assertFlagEqual() {
        assertTrue(robot.getComputer().getFlags().isEqual());
    }

    private void assertFlagNotLess() {
        assertFalse(robot.getComputer().getFlags().isLess());
    }

    private void assertFlagGreater() {
        assertTrue(robot.getComputer().getFlags().isGreater());
    }

    private void assertFlagLess() {
        assertTrue(robot.getComputer().getFlags().isLess());
    }

    private void assertFlagNotZero() {
        assertFalse(robot.getComputer().getFlags().isZero());
    }

    private void assertFlagNotGreater() {
        assertFalse(robot.getComputer().getFlags().isGreater());
    }

    private void assertFlagsNotEqual() {
        assertFalse(robot.getComputer().getFlags().isEqual());
    }

    // TODO: Compare Tests

    private void executeFirst(String program) {
        compile(program);
        executeInstruction();
    }

    private void assertStackPointer(int expectedStackPointer) {
        assertEquals(expectedStackPointer, robot.getComputer().getRegisters().getSp().signed());
    }

    private void assertInstructionPointer(int expectedInstructionPointer) {
        assertEquals(expectedInstructionPointer, robot.getComputer().getNextInstructionPointer());
    }

    private void assertStackPop(int stackPopValue) {
        assertEquals(stackPopValue, robot.getComputer().getStack().pop());
    }

    private void compile(String instruction) {
        source.println(instruction);
        compile();
    }

    private void assertBinaryOperation(int v1, int v2, String op, int result) {
        compile("MOV AX, " + v1 + "\n" + op + " AX, " + v2);
        executeInstruction();
        executeInstruction();
        assertAX(result);
    }

    private void assertAX(int result) {
        assertEquals(result, robot.getComputer().getRegisters().getAx().signed());
    }

    private void assertCX(int result) {
        assertEquals(result, robot.getComputer().getRegisters().getCx().signed());
    }

    private void executeInstruction() {
        robot.getComputer().executeInstruction();
    }
}
