package net.virtualinfinity.atrobots.gui;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class RobotFileUtils {
    public static FileNameExtensionFilter getAtRobotsFileNameFilter() {
        return new FileNameExtensionFilter("AT-Robots files", "at2", "atl", "ats");
    }

    static EntrantFile[] getFilesByName(Iterable<String> initialRobots) {
        final Collection<EntrantFile> files = new ArrayList<>();
        boolean debug = false;
        for (final String file : initialRobots) {
            if (!debug && "-d".equals(file)) {
                debug = true;
            } else {
                final File foundFile = robotFile(file);
                if (foundFile.isDirectory()) {
                    Stream.of(foundFile.listFiles(new AtRobotFileFilter())).map(EntrantFile::new).forEachOrdered(files::add);
                } else {
                    files.add(new EntrantFile(debug, foundFile));
                }
                debug = false;
            }
        }
        return files.toArray(new EntrantFile[files.size()]);
    }

    static EntrantFile[] getEntrantFiles(File[] initialRobots) {
        return Stream.of(initialRobots).map(file -> new EntrantFile(false, file)).toArray(EntrantFile[]::new);
    }

    static File robotFile(String robotName) {
        final File file = new File(robotName);
        if (file.exists()) {
            return file;
        }
        return Stream.of(file.getParentFile().listFiles(new FilenameAt2Filter(robotName))).findFirst().orElse(file);
    }

    static class EntrantFile {
        final boolean debug;
        final File file;

        public EntrantFile(File file) {
            this(false, file);
        }

        EntrantFile(boolean debug, File file) {
            this.debug = debug;
            this.file = file;
        }
    }

    static class FilenameAt2Filter implements FilenameFilter {
        private final String robotName;

        public FilenameAt2Filter(String robotName) {
            this.robotName = robotName;
        }

        public boolean accept(File dir, String name) {
            return name.toLowerCase().equals(robotName + ".at2") || name.toLowerCase().equals(robotName + ".atl");
        }
    }

    private static class AtRobotFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".at2") || name.toLowerCase().endsWith(".atl");
        }
    }
}
