package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.atsetup.AtRobotInstruction;
import net.virtualinfinity.atrobots.atsetup.AtRobotRegister;
import net.virtualinfinity.atrobots.computer.MemoryArray;
import net.virtualinfinity.atrobots.parser.Errors;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author Daniel Pitts
 */
public class EntrantFactoryTest extends AbstractCompilerTest {

    public void testNumberLabel() throws IOException {
        source.println(":3");
        compile();
        assertFalse(compilerOutput.hasErrors());
        assertFirstInstructionIs(3, 0, 0, 2);
    }

    private void assertFirstInstructionIs(int instruction, int op0, int op1, int microcode) {
        assertEquals(instruction, getMemoryArray().get(0));
        assertEquals(op0, getMemoryArray().get(1));
        assertEquals(op1, getMemoryArray().get(2));
        assertEquals(microcode, getMemoryArray().get(3));
    }

    public void testTokenTranslation() throws IOException {
        source.println("MOV ax, bx");
        compile();
        assertFirstInstructionIs(AtRobotInstruction.MOV.value,
                AtRobotRegister.AX.address,
                AtRobotRegister.BX.address,
                0x110);
        assertFalse(compilerOutput.hasErrors());
    }

    public void testNormalLabel() throws IOException {
        source.println("!Test");
        source.println("jmp !Test");
        compile();
        assertFalse(compilerOutput.hasErrors());
        assertEquals(0, getMemoryArray().get(1));
        assertEquals(0x40, getMemoryArray().get(3));
    }

    public void testCompileAll() {
        final File[] files = new File("original").listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".at2");
            }
        });
        Errors errors = new Errors();
        Compiler compiler = new Compiler();
        for (File file : files) {
            try {
                System.out.println("Loading " + file);
                compilerOutput = compiler.compile(file);
                final Errors result = compilerOutput.getErrors();
                if (result.hasErrors()) {
                    errors.info("Errors in " + file.getName());
                    errors.addAll(result);
                }
            } catch (IOException e1) {
                errors.info("Errors in " + file.getName());
                errors.info(e1.getMessage());
            } catch (Throwable t) {
                errors.info("Compiler error in " + file.getName());
                errors.info(t.getMessage());
                t.printStackTrace();
            }
        }
        errors.dumpErrors();
        assertFalse(errors.hasErrors());
    }


    private MemoryArray getMemoryArray() {
        return compilerOutput.getProgram().createProgramMemory();
    }

}
