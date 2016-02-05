package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;
import net.virtualinfinity.atrobots.compiler.Errors;
import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * This is the main GUI class.
 *
 * @author Daniel Pitts
 */
public class Main extends ArenaWindowBuilder implements Runnable {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static final PrintStream log = System.out;
    private volatile Game game;
    private boolean paused = true;

    private final Object gameLock;
    private static final int frameDelay = 25;
    private volatile boolean useDelay = true;
    private final Thread gameThread = new GameThread();

    private volatile boolean closed;
    private boolean debugMode;
    private Collection<String> initialRobots;
    private final AbstractAction runAction = new AbstractAction("Run") {
        public void actionPerformed(ActionEvent e) {
            setPaused(!paused);
            this.putValue(Action.NAME, paused ? "Run" : "Pause");
        }
    };

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
    }

    public void run() {
        initializeWindow();

        startGame();
    }

    private void startGame() {
        setGame(new Game(1000));
        if (!initialRobots.isEmpty()) {
            new EntrantLoader(initialRobots).execute();
        }

        gameThread.setDaemon(true);
        gameThread.start();
    }

    @Override
    protected void buildMenuBar() {
        menubar.add(createFileMenu());
        menubar.add(createViewMenu());
        menubar.add(new JButton(runAction));
        if (isDebugMode()) {
            addDebugMenuItems();
        }
    }

    private JMenu createFileMenu() {
        final JMenu menu = new JMenu("Game");
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
                chooser.setFileFilter(RobotFileUtils.getAtRobotsFileNameFilter());
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
                new EntrantLoader(new File("original").listFiles((dir, name) -> {
                    return name.toLowerCase().endsWith(".at2");
                })).execute();

            }
        }));
        menubar.add(new JButton(new AbstractAction("Add single") {
            public void actionPerformed(ActionEvent e) {
                for (final File parent : new File[]{new File("."), new File("original")}) {
                    new EntrantLoader(parent.listFiles((dir, name) -> name.toLowerCase().matches("zitgun.at2") ||
                        name.toLowerCase().endsWith("sniper2.at2"))).execute();
                }

            }
        }));
        menubar.add(new JButton(speedToggleAction));
    }

    public static void main(String[] args) {
        final Main main = new Main();
        final Collection<String> initialRobots = new ArrayList<>(Arrays.asList(args));
        main.setDebugMode(initialRobots.remove("--debug"));
        main.setInitialRobots(initialRobots);
        EventQueue.invokeLater(main);
    }

    private void setInitialRobots(Collection<String> initialRobots) {
        this.initialRobots = initialRobots;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    protected void registerCloseListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                synchronized (gameLock) {
                    closed = true;
                    paused = false;
                    gameLock.notifyAll();
                }

            }
        });
    }

    private class EntrantLoader extends SwingWorker<Errors, RobotFactory> {
        private final RobotFileUtils.EntrantFile[] selectedFiles;

        public EntrantLoader(File[] selectedFiles) {
            this(RobotFileUtils.getEntrantFiles(selectedFiles));
        }

        public EntrantLoader(RobotFileUtils.EntrantFile[] selectedFiles) {
            this.selectedFiles = selectedFiles;
        }

        public EntrantLoader(Iterable<String> initialRobots) {
            this(RobotFileUtils.getFilesByName(initialRobots));
        }


        protected Errors doInBackground() throws Exception {
            final Errors errors = new Errors();
            for (final RobotFileUtils.EntrantFile entrantFile : selectedFiles) {
                final File file = entrantFile.file;
                final AtRobotCompiler compiler = new AtRobotCompiler();
                try {
                    log.println("Loading " + file);
                    final AtRobotCompilerOutput result = compiler.compile(file);
                    if (result.hasErrors()) {
                        errors.info("Errors in " + file.getName());
                        errors.addAll(result.getErrors());
                    }
                    if (game != null) {
                        String name = file.getName();
                        if (name.toLowerCase().endsWith(".at2")) {
                            name = name.substring(0, name.length()-4);
                        }
                        game.addEntrant(result.createRobotFactory(name).setDebug(entrantFile.debug));
                    }
                } catch (final IOException e1) {
                    errors.info("Errors in " + file.getName());
                    errors.info(e1.getMessage());
                } catch (final Throwable t) {
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
            } catch (final InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
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
                } catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
