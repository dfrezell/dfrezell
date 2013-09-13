/*
 * Copyright (c) 2003 Drew Frezell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.frezell.icfp.viewer;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import org.frezell.icfp.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.8 $
 */
public class TracePanel extends JPanel implements PropertyChangeListener, ActionListener {
    public static final String DIR = "org/frezell/icfp/img/";

    public static final int ICON_INDEX = 0;
    public static final int PRESS_ICON_INDEX = 1;
    public static final int OVER_ICON_INDEX = 2;

    public static final String[] PLAY = {
        "play.png", "play_press.png", "play_over.png"
    };
    public static final String[] PAUSE = {
        "pause.png", "pause_press.png", "pause_over.png"
    };
    public static final String[] PREV = {
        "prev.png", "prev_press.png", "prev_over.png"
    };
    public static final String[] NEXT = {
        "next.png", "next_press.png", "next_over.png"
    };

    public static final String EMPTY = "Empty";
    public static final String EMPTY_TOOLTIP = "Load a trace file.";
    public static final String CREATE = "Create";
    public static final String CREATE_TOOLTIP = "Add trace commands to list.";
    public static final String PLAYBACK = "Playback";

    private JToggleButton m_play;
    private JToggleButton m_pause;
    private JTextField m_title;
    private JButton m_next;
    private JButton m_previous;
    private TraceTableModel m_traceModel;

    public TracePanel() {
        init();
    }

    public void init() {
        JPanel buttonBar = new JPanel();
        Box box = new Box(BoxLayout.X_AXIS);
        JTable traceTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(traceTable);
        ButtonGroup bg = new ButtonGroup();
        Dimension spacer = new Dimension(4, 24);

        buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
        buttonBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        m_traceModel = new TraceTableModel();
        traceTable.setModel(m_traceModel);
        traceTable.setColumnSelectionAllowed(false);
        traceTable.getColumn("Count").sizeWidthToFit();

        m_title = new JTextField(EMPTY, 7);
        m_title.setToolTipText(EMPTY_TOOLTIP);
        m_title.setMaximumSize(new Dimension(100, 24));
        m_title.setEditable(false);
        m_title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(2, 4, 2, 2)));

        m_play = new JToggleButton();
        m_pause = new JToggleButton();
        m_next = new JButton();
        m_previous = new JButton();

        bg.add(m_play);
        bg.add(m_pause);
        m_pause.setSelected(true);

        initButton(m_play, PLAY[ICON_INDEX], PLAY[PRESS_ICON_INDEX], PLAY[OVER_ICON_INDEX]);
        initButton(m_pause, PAUSE[ICON_INDEX], PAUSE[PRESS_ICON_INDEX], PAUSE[OVER_ICON_INDEX]);
        initButton(m_next, NEXT[ICON_INDEX], NEXT[PRESS_ICON_INDEX], NEXT[OVER_ICON_INDEX]);
        initButton(m_previous, PREV[ICON_INDEX], PREV[PRESS_ICON_INDEX], PREV[OVER_ICON_INDEX]);

        m_play.setEnabled(false);
        m_play.addActionListener(this);
        m_pause.setEnabled(false);
        m_pause.addActionListener(this);
        m_next.setEnabled(false);
        m_next.addActionListener(this);
        m_previous.setEnabled(false);
        m_previous.addActionListener(this);

        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(m_previous);
        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(m_play);
        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(m_pause);
        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(m_next);
        buttonBar.add(Box.createRigidArea(spacer));
        buttonBar.add(Box.createRigidArea(spacer));

        box.add(buttonBar);
        box.add(Box.createRigidArea(spacer));
        box.add(m_title);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(box);
        add(Box.createVerticalStrut(4));
        add(scrollPane);
    }

    private void initButton(AbstractButton button, String icon, String pressIcon, String overIcon) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        button.setIcon(new ImageIcon(loader.getResource(DIR + icon)));
        button.setPressedIcon(new ImageIcon(loader.getResource(DIR + pressIcon)));
        button.setRolloverIcon(new ImageIcon(loader.getResource(DIR + overIcon)));
        button.setSelectedIcon(new ImageIcon(loader.getResource(DIR + pressIcon)));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(null);
    }

    private void enableButtons(boolean enable) {
        m_next.setEnabled(enable);
        m_pause.setEnabled(enable);
        m_play.setEnabled(enable);
        m_previous.setEnabled(enable);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == TrackMenuBar.EVENT_CLEAR_TRACE) {
            m_title.setText(EMPTY);
            m_title.setToolTipText(EMPTY_TOOLTIP);
            m_traceModel.setRowCount(0);
            enableButtons(false);
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_OPEN_TRACE) {
            Trace t = (Trace) evt.getNewValue();
            m_title.setText(PLAYBACK);
            m_title.setToolTipText(t.getName());
            m_traceModel.setRowCount(0);
            enableButtons(true);
        } else if (evt.getPropertyName() == TrackMenuBar.EVENT_NEW_TRACE) {
            m_title.setText(CREATE);
            m_title.setToolTipText(CREATE_TOOLTIP);
            m_traceModel.setRowCount(0);
            enableButtons(true);
        } else if (evt.getPropertyName() == RaceManager.EVENT_CAR_MOVED) {
            m_traceModel.addRow(((Car) evt.getNewValue()).getState());
        } else if (evt.getPropertyName() == RaceManager.EVENT_CAR_STOPPED) {
            m_pause.setSelected(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == m_next) {
            RaceManager.instance().stepForward();
        } else if (source == m_pause) {
            RaceManager.instance().startRace(false);
        } else if (source == m_play) {
            RaceManager.instance().startRace(true);
        } else if (source == m_previous) {
            RaceManager.instance().stepBackward();
        }
    }
}
