package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.Compiler;
import net.virtualinfinity.atrobots.CompilerOutput;
import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.gui.renderers.GradientExplosionRenderer;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.gui.renderers.ScanRenderer;
import net.virtualinfinity.atrobots.gui.renderers.SimpleExplosionRenderer;
import net.virtualinfinity.atrobots.parser.Errors;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * This is the main GUI class.
 *
 * @author Daniel Pitts
 */
public class Main implements Runnable {
    private JFrame mainFrame;
    private JMenuBar menubar;
    private volatile Game game;
    private ArenaPane arenaPane;
    private boolean paused = true;

    private final Object gameLock;
    private volatile int frameDelay = 25;
    private volatile boolean useDelay = true;
    private Thread gameThread = new GameThread();

    private volatile boolean closed;
    private RobotStatusPane robotStatusPane;
    private boolean debugMode;
    private java.util.List<String> initialRobots;
    private RobotRenderer robotRenderer;
    private ScanRenderer scanRenderer;
    private final ToggleProperty toggleRobotStatusBars;
    private final ToggleProperty toggleRenderDeadRobots;
    private final ToggleProperty toggleFillScanArc;
    private final ToggleProperty toggleFillShields;
    private final ToggleProperty toggleShowNames;
    private final AbstractAction runAction = new AbstractAction("Run") {
        public void actionPerformed(ActionEvent e) {
            setPaused(!paused);
            this.putValue(Action.NAME, paused ? "Run" : "Pause");
        }
    };

    public boolean isPaused() {
        synchronized (gameLock) {
            return paused;
        }
    }

    public void setPaused(boolean paused) {
        synchronized (gameLock) {
            this.paused = paused;
            gameLock.notifyAll();
        }
    }

    private final AbstractAction speedToggleAction = new AbstractAction("Full Speed") {
        public void actionPerformed(ActionEvent e) {
            useDelay = !useDelay;
            this.putValue(Action.NAME, useDelay ? "Full Speed" : "Slower");
        }
    };

    public Main() {
        gameLock = new Object();
        toggleRobotStatusBars = new ToggleProperty("Robot Status Bars", new ShowStatusBarsAccessor());
        toggleRenderDeadRobots = new ToggleProperty("Dead Robots", new ShowDeadRobotsAccessor());
        toggleFillScanArc = new ToggleProperty("Filled Scans", new ShowFilledScansAccessor());
        toggleFillShields = new ToggleProperty("Filled Shields", new ShowFillShieldAccessor());
        toggleShowNames = new ToggleProperty("Label Robots", new ShowRobotNameAccessor());
    }

    public void run() {
        initializeSystemLookAndFeel();
        gameThread.setDaemon(true);
        gameThread.start();
        mainFrame = new JFrame("AT-Robots 2 Clone 0.0.01");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                synchronized (gameLock) {
                    closed = true;
                    paused = false;
                    gameLock.notifyAll();
                }

            }
        });
        menubar = new JMenuBar();
        mainFrame.setJMenuBar(menubar);
        menubar.add(createFileMenu());
        menubar.add(createViewMenu());
        menubar.add(new JButton(runAction));
        if (isDebugMode()) {
            addDebugMenuItems();
        }
        robotStatusPane = RobotStatusPane.createRobotStatusPane();
        final JScrollPane robotStatusScroller = new JScrollPane(robotStatusPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainFrame.getContentPane().add(robotStatusScroller, BorderLayout.EAST);
        arenaPane = new ArenaPane();
        mainFrame.getContentPane().add(arenaPane, BorderLayout.CENTER);
        arenaPane.setBackground(Color.black);
        arenaPane.setOpaque(true);
        arenaPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.darkGray));
        arenaPane.setPreferredSize(new Dimension(500, 500));
        setGame(new Game(1000));
        arenaPane.setRobotStatusPane(robotStatusPane);
        robotRenderer = new RobotRenderer();
        scanRenderer = new ScanRenderer();
        arenaPane.getArenaRenderer().setRobotRenderer(robotRenderer);
        arenaPane.getArenaRenderer().setScanRenderer(scanRenderer);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLocationByPlatform(true);
        mainFrame.pack();
        mainFrame.setVisible(true);
        if (!initialRobots.isEmpty()) {
            new EntrantLoader(initialRobots).execute();
        }
    }

    private JMenu createViewMenu() {
        final JMenu viewMenu = new JMenu("View");
        viewMenu.add(toggleShowNames.configure(new JCheckBoxMenuItem()));
        viewMenu.add(toggleRobotStatusBars.configure(new JCheckBoxMenuItem()));
        viewMenu.add(toggleFillShields.configure(new JCheckBoxMenuItem()));
        viewMenu.add(toggleRenderDeadRobots.configure(new JCheckBoxMenuItem()));
        viewMenu.addSeparator();
        viewMenu.add(toggleFillScanArc.configure(new JCheckBoxMenuItem()));
        viewMenu.add(new JCheckBoxMenuItem(new AbstractAction("Gradiant Explosions") {
            public void actionPerformed(ActionEvent e) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                boolean selected = aButton.getModel().isSelected();
                arenaPane.getArenaRenderer().setExplosionRenderer(selected ? new GradientExplosionRenderer() : new SimpleExplosionRenderer());
            }
        }));
        return viewMenu;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("Game");
        menu.add(new AbstractAction("New Game") {
            public void actionPerformed(ActionEvent e) {
                final NewGameDialog dialog = new NewGameDialog();
                dialog.setModal(true);
                dialog.pack();
                dialog.setVisible(true);
                final Game newGame = dialog.getGame();
                if (newGame != null) {
//                    if (game != null) {
//                        game.dispose();
//                    }
                    setGame(newGame);
                }
                game.nextRound();
            }
        });
        menu.add(new AbstractAction("Add Entrant") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("AT-Robots files", ".at2", ".ats"));
                chooser.setMultiSelectionEnabled(true);
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    new EntrantLoader(chooser.getSelectedFiles()).execute();
                }
            }
        });
        return menu;
    }

    private void setGame(Game newGame) {
        setPaused(true);
        arenaPane.reset();
        robotStatusPane.reset();
        game = newGame;
        game.addSimulationObserver(arenaPane);
        game.addSimulationObserver(robotStatusPane);
    }

    private void addDebugMenuItems() {
        menubar.add(new JButton(new AbstractAction("Add all original") {
            public void actionPerformed(ActionEvent e) {
                new EntrantLoader(new File("original").listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".at2");
                    }
                })).execute();

            }
        }));
        menubar.add(new JButton(new AbstractAction("Add single") {
            public void actionPerformed(ActionEvent e) {
                for (final File parent : new File[]{new File("."), new File("original")})
                    new EntrantLoader(parent.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().matches("zitgun.at2") ||
                                    name.toLowerCase().endsWith("sniper2.at2");
                        }
                    })).execute();

            }
        }));
        menubar.add(new JButton(speedToggleAction));
    }

    private void initializeSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    public static void main(String[] args) {
        final Main main = new Main();
        java.util.List<String> initialRobots = new ArrayList<String>(Arrays.asList(args));
        main.setDebugMode(initialRobots.remove("--debug"));
        main.setInitialRobots(initialRobots);
        EventQueue.invokeLater(main);
    }

    private void setInitialRobots(java.util.List<String> initialRobots) {
        this.initialRobots = initialRobots;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private static EntrantFile[] getFilesByName(java.util.List<String> initialRobots) {
        final java.util.List<EntrantFile> files = new ArrayList<EntrantFile>();
        boolean debug = false;
        for (String file : initialRobots) {
            if (!debug && "-d".equals(file)) {
                debug = true;
            } else {
                files.add(new EntrantFile(debug, robotFile(file)));
                debug = false;
            }
        }
        return files.toArray(new EntrantFile[files.size()]);
    }

    private static EntrantFile[] getEntrantFiles(File[] initialRobots) {
        final java.util.List<EntrantFile> files = new ArrayList<EntrantFile>();
        for (File file : initialRobots) {
            files.add(new EntrantFile(false, file));
        }
        return files.toArray(new EntrantFile[files.size()]);
    }

    private static File robotFile(String robotName) {
        final File file = new File(robotName);
        if (file.exists()) {
            return file;
        }
        for (File f : file.getParentFile().listFiles(new FilenameAt2Filter(robotName))) {
            return f;
        }
        return file;
    }

    static class EntrantFile {
        final boolean debug;
        final File file;

        EntrantFile(boolean debug, File file) {
            this.debug = debug;
            this.file = file;
        }
    }

    private class EntrantLoader extends SwingWorker<Errors, Entrant> {
        private final EntrantFile[] selectedFiles;

        public EntrantLoader(File[] selectedFiles) {
            this(getEntrantFiles(selectedFiles));
        }

        public EntrantLoader(EntrantFile[] selectedFiles) {
            this.selectedFiles = selectedFiles;
        }

        public EntrantLoader(java.util.List<String> initialRobots) {
            this(getFilesByName(initialRobots));
        }


        protected Errors doInBackground() throws Exception {
            Errors errors = new Errors();
            for (EntrantFile entrantFile : selectedFiles) {
                final File file = entrantFile.file;
                Compiler compiler = new net.virtualinfinity.atrobots.Compiler();
                try {
                    System.out.println("Loading " + file);
                    final CompilerOutput result = compiler.compile(file);
                    if (result.hasErrors()) {
                        errors.info("Errors in " + file.getName());
                        errors.addAll(result.getErrors());
                    }
                    if (game != null) {
                        game.addEntrant(result.createEntrant(file.getName()));
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
            return errors;
        }

        protected void done() {
            try {
                get().showErrorDialog("Errors", mainFrame);
                game.nextRound();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }
    }

    private abstract class RepaintArenaAfterSetBooleanAccessor implements BooleanAccessor {
        public final void set(boolean value) {
            doSet(value);
            arenaPane.repaint();
        }

        protected abstract void doSet(boolean value);
    }

    private class ShowStatusBarsAccessor extends RepaintArenaAfterSetBooleanAccessor {
        public boolean get() {
            return robotRenderer.isShowStatusBars();
        }

        public void doSet(boolean value) {
            robotRenderer.setShowStatusBars(value);
        }
    }

    private class ShowFillShieldAccessor extends RepaintArenaAfterSetBooleanAccessor {
        public boolean get() {
            return robotRenderer.isFillShield();
        }

        public void doSet(boolean value) {
            robotRenderer.setFillShield(value);
        }
    }

    private class ShowRobotNameAccessor extends RepaintArenaAfterSetBooleanAccessor {
        public boolean get() {
            return robotRenderer.isShowName();
        }

        public void doSet(boolean value) {
            robotRenderer.setShowName(value);
        }
    }


    private class ShowDeadRobotsAccessor extends RepaintArenaAfterSetBooleanAccessor {
        public boolean get() {
            return robotRenderer.isRenderDead();
        }

        public void doSet(boolean value) {
            robotRenderer.setRenderDead(value);
        }
    }

    private class ShowFilledScansAccessor extends RepaintArenaAfterSetBooleanAccessor {
        public boolean get() {
            return scanRenderer.isFillArcs();
        }

        public void doSet(boolean value) {
            scanRenderer.setFillArcs(value);
        }
    }

    private class GameThread extends Thread {
        public void run() {
            while (!closed) {
                try {
                    synchronized (gameLock) {
                        while (paused && !closed) {
                            gameLock.wait();
                        }
                    }
                    if (useDelay) {
                        Thread.sleep(frameDelay);
                    } else {
                        Thread.yield();
                    }
                    synchronized (gameLock) {
                        if (!closed && game != null) {
                            game.stepRound();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class FilenameAt2Filter implements FilenameFilter {
        private final String robotName;

        public FilenameAt2Filter(String robotName) {
            this.robotName = robotName;
        }

        public boolean accept(File dir, String name) {
            return name.toLowerCase().equals(robotName + ".at2");
        }
    }
}
