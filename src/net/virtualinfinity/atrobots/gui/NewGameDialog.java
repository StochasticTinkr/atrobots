package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.Game;
import net.virtualinfinity.atrobots.compiler.Errors;
import net.virtualinfinity.atrobots.config.EntrantFile;
import net.virtualinfinity.atrobots.config.EntrantSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner numberOfRounds;
    private JSpinner maxCpu;
    private JButton loadEntrantsButton;
    private JList entrants;
    private JButton removeButton;
    private Game game;
    private NewGameDialog.EntrantFactoryListModel entrantsModel;

    public NewGameDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        loadEntrantsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser jFileChooser = new JFileChooser(new File("."));
                jFileChooser.setMultiSelectionEnabled(true);
                jFileChooser.setFileFilter(new FileNameExtensionFilter("AT-Robots files", "at2", "ats"));
                if (jFileChooser.showOpenDialog(NewGameDialog.this) == JFileChooser.APPROVE_OPTION) {
                    Errors errors = new Errors();
                    for (File file : jFileChooser.getSelectedFiles()) {
                        try {
                            EntrantSource factory = new EntrantFile(file);
                            System.out.println("Loading " + file);
                            entrantsModel.add(factory);
                        } catch (IOException e1) {
                            errors.info("Errors in " + file.getName());
                            errors.info(e1.getMessage());
                        } catch (Throwable t) {
                            errors.info("Compiler error in " + file.getName());
                            errors.info(t.getMessage());
                            t.printStackTrace();
                        }
                    }
                    errors.showErrorDialog("Errors whiel loading entrants.", NewGameDialog.this);
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entrantsModel.remove(entrants.getSelectedIndices());
            }
        });
        maxCpu.setModel(new SpinnerNumberModel(5, 1, 1000, 5));
        numberOfRounds.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    }

    private void onOK() {
// add your code here
        game = new Game(((Number) numberOfRounds.getValue()).intValue());
        game.setMaxProcessorSpeed(((Number) maxCpu.getValue()).intValue());
        for (EntrantSource factory : entrantsModel.getList()) {
            try {
                game.addAll(factory.createEntrants());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        game = null;
        dispose();
    }

    public static void main(String[] args) {
        NewGameDialog dialog = new NewGameDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        entrantsModel = new EntrantFactoryListModel();
        entrants = new JList(entrantsModel);
    }

    public Game getGame() {
        return game;
    }

    private class EntrantFactoryListModel extends AbstractListModel implements ListModel {
        List<EntrantSource> list = new ArrayList<EntrantSource>();

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index).getName();
        }

        public void add(EntrantSource factory) {
            list.add(factory);
            fireIntervalAdded(this, list.size() - 1, list.size() - 1);
        }

        public void remove(EntrantSource factory) {
            final int i = list.indexOf(factory);
            if (i != -1) {
                list.remove(i);
                fireIntervalRemoved(this, i, i);
            }
        }

        public void remove(int[] indices) {
            List<EntrantSource> toRemove = new ArrayList<EntrantSource>();
            for (int i : indices) {
                toRemove.add(list.get(i));
            }
            for (EntrantSource factory : toRemove) {
                remove(factory);
            }
        }

        public List<EntrantSource> getList() {
            return list;
        }
    }
}
