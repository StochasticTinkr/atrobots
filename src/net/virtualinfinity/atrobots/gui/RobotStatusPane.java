package net.virtualinfinity.atrobots.gui;

import net.virtualinfinity.atrobots.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.SimulationObserver;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.SnapshotAdaptor;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Describe this class.
 *
 * @author <a href="mailto:daniel.pitts@cbs.com">Daniel Pitts</a>
 */
public class RobotStatusPane extends JList implements SimulationObserver {
    private final DefaultListModel robotList;


    private RobotStatusPane() {
        robotList = new DefaultListModel();
        setModel(robotList);
        this.setCellRenderer(new RobotStatusRenderer());
    }

    public void frameAvailable(final SimulationFrameBuffer frameBuffer) {
        EventQueue.invokeLater(new UpdateRobotList(frameBuffer));
    }

    private void updateRobotStatus(RobotSnapshot robotSnapshot) {
        for (int i = 0; i < robotList.getSize(); ++i) {
            final RobotItem robotItem = (RobotItem) robotList.get(i);
            if (robotItem.getId() == robotSnapshot.getId()) {
                robotItem.setRobotSnapshot(robotSnapshot);
                return;
            }
        }
        robotList.addElement(new RobotItem(robotSnapshot));
        revalidate();
    }

    public static RobotStatusPane createRobotStatusPane() {
        return new RobotStatusPane();
    }

    public java.util.Set<Integer> getSelectedRobotIds() {
        final Object[] objects = this.getSelectedValues();
        final Set<Integer> selectedIds = new HashSet<Integer>();
        for (Object o : objects) {
            selectedIds.add(((RobotItem) o).getId());

        }
        return selectedIds;
    }

    private static class Bar extends JComponent {
        private BoundedRangeModel model;
        private final ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        };
        private float[] foregroundGradientFractions;
        private Color[] foregroundGradientColors;

        @Override
        protected void paintComponent(Graphics g) {
            final Graphics2D g2d = (Graphics2D) g;
            final Insets insets = getInsets(null);
            final int maxWidth = getWidth() - insets.left - insets.right;
            final int maxHeight = getHeight() - insets.top - insets.bottom;
            if (foregroundGradientFractions != null) {
                g2d.setPaint(new LinearGradientPaint(insets.left, 0, getWidth() - insets.right, 0, foregroundGradientFractions, foregroundGradientColors));
            } else {
                g2d.setPaint(getForeground());
            }
            g2d.fillRect(insets.left, insets.top, maxWidth * model.getValue() / model.getMaximum(), maxHeight);
            g2d.drawRect(insets.left, insets.top, maxWidth - 1, maxHeight - 1);
        }

        public float[] getForegroundGradientFractions() {
            return foregroundGradientFractions;
        }

        public void setForegroundGradientFractions(float[] foregroundGradientFractions) {
            this.foregroundGradientFractions = foregroundGradientFractions;
        }

        public Color[] getForegroundGradientColors() {
            return foregroundGradientColors;
        }

        public void setForegroundGradientColors(Color[] foregroundGradientColors) {
            this.foregroundGradientColors = foregroundGradientColors;
        }

        private Bar() {
            setModel(new DefaultBoundedRangeModel());
        }

        public void setModel(BoundedRangeModel model) {
            if (this.model != null) {
                model.removeChangeListener(changeListener);
            }
            model.addChangeListener(changeListener);
            this.model = model;
        }

        public int getMinimum() {
            return getModel().getMinimum();
        }

        public void setMinimum(int newMinimum) {
            getModel().setMinimum(newMinimum);
        }

        public int getMaximum() {
            return getModel().getMaximum();
        }

        public void setMaximum(int newMaximum) {
            getModel().setMaximum(newMaximum);
        }

        public int getValue() {
            return getModel().getValue();
        }

        public void setValue(int newValue) {
            getModel().setValue(newValue);
        }

        public void setValueIsAdjusting(boolean b) {
            getModel().setValueIsAdjusting(b);
        }

        public boolean getValueIsAdjusting() {
            return getModel().getValueIsAdjusting();
        }

        public int getExtent() {
            return getModel().getExtent();
        }

        public void setExtent(int newExtent) {
            getModel().setExtent(newExtent);
        }

        public BoundedRangeModel getModel() {
            return model;
        }
    }

    private static class RobotStatusRenderer extends JPanel implements ListCellRenderer {
        private final JLabel name = new JLabel();
        private final JLabel roundKills = new JLabel();
        private final Bar armor = new Bar();
        private final Bar heat = new Bar();
        private final JLabel lastMessage = new JLabel();

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            final Color background = isSelected ? Color.blue : Color.black;
            setBackground(background);
            setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.lightGray, Color.darkGray));
            final Color foreground = isSelected ? Color.yellow : Color.gray;
            armor.setForegroundGradientFractions(new float[]{0, .25f, 1});
            armor.setForegroundGradientColors(new Color[]{new Color(0, 0, .25f), Color.blue, Color.green});
            heat.setForegroundGradientFractions(new float[]{0, .25f, 1});
            heat.setForegroundGradientColors(new Color[]{new Color(.2f, 0, 0), Color.red, Color.yellow});
            name.setForeground(foreground);
            roundKills.setForeground(foreground);
            lastMessage.setForeground(foreground);
            removeAll();
            setLayout(new GridLayout(5, 1, 0, 1));
            add(name);
            add(roundKills);
            add(armor);
            add(heat);
            add(lastMessage);
            RobotItem item = (RobotItem) value;
            final RobotSnapshot robotSnapshot = item.getRobotSnapshot();
            if (!item.isDead()) {
                name.setText(robotSnapshot.getName());
                roundKills.setText("Kills (round/total): " + robotSnapshot.getRoundKills() + "/" + robotSnapshot.getTotalKills());
                armor.setValue((int) Math.round(Math.min(100, robotSnapshot.getArmor())));
                heat.setValue((int) Math.round(Math.min(100, robotSnapshot.getTemperature().getLogScale() * .2)));
            } else {
                armor.setValue(0);
                heat.setValue(0);
                name.setText("<html><strike>" + robotSnapshot.getName() + "</html></strike>");
            }
            lastMessage.setText(robotSnapshot.getLastMessage() == null ? "" : robotSnapshot.getLastMessage());
            return this;
        }
    }

    private class UpdateRobotList implements Runnable {
        private final SimulationFrameBuffer frameBuffer;

        public UpdateRobotList(SimulationFrameBuffer frameBuffer) {
            this.frameBuffer = frameBuffer;
        }

        public void run() {
            for (int i = 0; i < robotList.getSize(); ++i) {
                final RobotItem robotItem = (RobotItem) robotList.get(i);
                robotItem.clearUpdated();
            }
            frameBuffer.getCurrentFrame().visitRobots(new SnapshotAdaptor() {
                @Override
                public void acceptRobot(RobotSnapshot robotSnapshot) {
                    updateRobotStatus(robotSnapshot);
                }
            });
            for (int i = 0; i < robotList.getSize(); ++i) {
                final RobotItem robotItem = (RobotItem) robotList.get(i);
                if (robotItem.isChanged()) {
                    robotList.set(i, robotItem);
                }
                if (!robotItem.isUpdated() && !robotItem.isDead()) {
                    robotItem.died();
                    robotList.set(i, robotItem);
                }
            }
        }
    }

    private static class RobotItem {
        private RobotSnapshot robotSnapshot;
        private boolean dead;
        private boolean updated;
        private boolean changed;

        public RobotItem(RobotSnapshot robotSnapshot) {
            setRobotSnapshot(robotSnapshot);
        }

        public RobotSnapshot getRobotSnapshot() {
            return robotSnapshot;
        }

        public boolean isUpdated() {
            return updated;
        }

        public void clearUpdated() {
            updated = false;
            changed = false;
        }

        public void setRobotSnapshot(RobotSnapshot robotSnapshot) {
            changed = this.robotSnapshot == null || !this.robotSnapshot.equals(robotSnapshot);
            this.updated = true;
            this.dead = robotSnapshot.getArmor() <= 0;
            this.robotSnapshot = robotSnapshot;
        }

        public boolean isChanged() {
            return changed;
        }

        public boolean isDead() {
            return dead;
        }

        public int getId() {
            return getRobotSnapshot().getId();
        }

        public void died() {
            dead = true;
        }
    }
}