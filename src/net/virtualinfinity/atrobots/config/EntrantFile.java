package net.virtualinfinity.atrobots.config;

import net.virtualinfinity.atrobots.compiler.CompilerOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class EntrantFile extends EntrantSource {
    private File file;
    private String defaultName;

    public EntrantFile() {
    }

    public EntrantFile(File file) throws IOException {
        setFile(file);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) throws IOException {
        defaultName = null;
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }
        this.file = file.getCanonicalFile();
        defaultName = removeEndIgnoreCase(file.getName(), ".at2");
    }

    public static String removeEndIgnoreCase(String s, String suffix) {
        if (s == null) {
            return null;
        }
        if (s.toLowerCase().endsWith(suffix)) {
            return s.substring(0, s.length() - suffix.length());
        }
        return s;
    }

    @Override
    protected String getDefaultName() {
        return defaultName;
    }

    protected CompilerOutput compile(net.virtualinfinity.atrobots.compiler.Compiler compiler) throws IOException {
        return compiler.compile(file);
    }
}
