package net.virtualinfinity.atrobots.parser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Errors {
    List<String> messages = new ArrayList<String>();

    public void add(String error, int lineNumber) {
        messages.add("Line " + lineNumber + ": " + error);
    }

    public boolean hasErrors() {
        return !messages.isEmpty();
    }

    public void add(String error, int lineNumber, int column) {
        messages.add("Line " + lineNumber + " (column " + column + "): " + error);
    }

    public void showErrorDialog(String name, Object parent) {
        if (!hasErrors()) {
            return;
        }
        final JDialog dialog = parent instanceof Frame ? new JDialog((Frame) parent, name, true) : new JDialog((Dialog) parent, name, true);
        final JList jList = new JList();
        dialog.add(new JScrollPane(jList));
        final DefaultListModel listModel = new DefaultListModel();
        for (String st : messages) {
            listModel.addElement(st);
        }
        jList.setModel(listModel);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void info(String info) {
        messages.add(info);
    }

    public void addAll(Errors result) {
        messages.addAll(result.messages);
    }

    public void dumpErrors() {
        if (hasErrors()) {
            for (String m : messages) {
                System.out.println(m);
            }
        }
    }
}
