package net.virtualinfinity.atrobots;

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
        assertEquals(3, getMemoryArray().get(0));
        assertEquals(0, getMemoryArray().get(1));
        assertEquals(0, getMemoryArray().get(2));
        assertEquals(2, getMemoryArray().get(3));
    }

    public void testTokenTranslation() throws IOException {
        source.println("MOV ax, bx");
        compile();
        assertEquals(22, getMemoryArray().get(0));
        assertEquals(65, getMemoryArray().get(1));
        assertEquals(66, getMemoryArray().get(2));
        assertEquals(0x110, getMemoryArray().get(3));
    }

    public void testNormalLabel() throws IOException {
        source.println("!Test");
        source.println("jmp !Test");
        compile();
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
        for (File file : files) {
            EntrantFactory factory = new EntrantFactory(file);
            try {
                System.out.println("Loading " + file);
                final Errors result = factory.compile();
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
        return entrantFactory.createEntrant().getProgram().createProgramMemory();
    }

}
