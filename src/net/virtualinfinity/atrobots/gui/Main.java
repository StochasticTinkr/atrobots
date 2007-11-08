package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.EntrantFactory;
import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.Errors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Daniel Pitts
 */
public class Main implements Runnable{
    private JFrame mainFrame;
    private JMenuBar menubar;
    private JMenu menu;
    private Game game;

    public void run() {
        initializeSystemLookAndFeel();
        mainFrame = new JFrame("AT-Robots 2 Clone 0.0.01");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setLocationRelativeTo(null);

        menubar = new JMenuBar();
        mainFrame.setJMenuBar(menubar);
        menubar.add(createFileMenu());
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JMenu createFileMenu() {
        menu = new JMenu("Game");
        menu.add(new AbstractAction("New Game") {
            public void actionPerformed(ActionEvent e) {
                final NewGameDialog dialog = new NewGameDialog();
                dialog.setModal(true);
                dialog.pack();
                dialog.setVisible(true);
                final Game newGame = dialog.getGame();
                if (newGame != null) {
                    if (game != null) {
                        game.dispose();
                    }
                    game = newGame;
                }
            }
        });
        menu.add(new AbstractAction("Add Entrant") {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    final SwingWorker<Errors, Object> worker = new SwingWorker<Errors, Object>() {
                        protected Errors doInBackground() throws Exception {
                            Errors errors = new Errors();
                            for (File file : chooser.getSelectedFiles()) {
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
                            return errors;
                        }

                        protected void done() {
                            try {
                                get().showErrorDialog("Errors", mainFrame);
                            } catch (InterruptedException e1) {
                            } catch (ExecutionException e1) {
                            }
                        }
                    };
                    worker.execute();
                }
            }
        });
        return menu;
    }

    private void initializeSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Main());
    }
}
