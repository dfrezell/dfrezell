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
import java.beans.*;
import javax.swing.*;

import org.frezell.icfp.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.5 $
 */
public class StatusPanel extends JPanel implements PropertyChangeListener {
    private static final String EMPTY_TEXT = "--";

    private JTextField m_xPos;
    private JTextField m_yPos;
    private JTextField m_velocity;
    private JTextField m_direction;

    public StatusPanel() {
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        add(buildStatusPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildStatusPanel() {
        JPanel statusPanel = new JPanel();
        JLabel label;
        Dimension rigid = new Dimension(8, 1);

        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        m_xPos = new JTextField(EMPTY_TEXT, 8);
        m_yPos = new JTextField(EMPTY_TEXT, 8);
        m_velocity = new JTextField(EMPTY_TEXT, 8);
        m_direction = new JTextField(EMPTY_TEXT, 8);

        m_xPos.setEditable(false);
        m_xPos.setHorizontalAlignment(JTextField.RIGHT);
        m_yPos.setEditable(false);
        m_yPos.setHorizontalAlignment(JTextField.RIGHT);
        m_velocity.setEditable(false);
        m_velocity.setHorizontalAlignment(JTextField.RIGHT);
        m_direction.setEditable(false);
        m_direction.setHorizontalAlignment(JTextField.RIGHT);

        label = new JLabel("x:");
        label.setLabelFor(m_xPos);
        statusPanel.add(label);
        statusPanel.add(m_xPos);

        statusPanel.add(Box.createRigidArea(rigid));

        label = new JLabel("y:");
        label.setLabelFor(m_yPos);
        statusPanel.add(label);
        statusPanel.add(m_yPos);

        statusPanel.add(Box.createRigidArea(rigid));

        label = new JLabel("velocity:");
        label.setLabelFor(m_velocity);
        statusPanel.add(label);
        statusPanel.add(m_velocity);

        statusPanel.add(Box.createRigidArea(rigid));

        label = new JLabel("direction:");
        label.setLabelFor(m_direction);
        statusPanel.add(label);
        statusPanel.add(m_direction);

        return statusPanel;
    }

    private void updateStatus(Car car) {
        updateStatus(car.getFixedPointX(), car.getFixedPointY(),
                car.getFixedPointVelocity(), car.getFixedPointDirection());
    }

    private void updateStatus(int fixedX, int fixedY, int fixedV, int fixedD) {
        updateStatus(Integer.toString(fixedX >>> FPA.FIXED_SHIFT), Integer.toString(fixedX),
                Integer.toString(fixedY >>> FPA.FIXED_SHIFT), Integer.toString(fixedY),
                Integer.toString(fixedV >>> FPA.FIXED_SHIFT), Integer.toString(fixedV),
                Integer.toString(fixedD >>> FPA.FIXED_SHIFT), Integer.toString(fixedD));
    }

    private void updateStatus(String x, String xFixed, String y, String yFixed,
            String v, String vFixed, String d, String dFixed) {
        m_xPos.setText(x);
        m_xPos.setToolTipText(xFixed);

        m_yPos.setText(y);
        m_yPos.setToolTipText(yFixed);

        m_velocity.setToolTipText(v);
        m_velocity.setText(vFixed);

        m_direction.setToolTipText(d);
        m_direction.setText(dFixed);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(RaceManager.EVENT_CAR_MOVED)) {
            updateStatus(RaceManager.instance().getCar());
        } else if (evt.getPropertyName().equals(TrackMenuBar.EVENT_OPEN_MAP)) {
            Track track = (Track) evt.getNewValue();
            // Loading a new track or clearing the old one, we initialize depending
            // on which we are doing.
            Point p = track.getInitialCarPosition();
            updateStatus(p.x << FPA.FIXED_SHIFT,  p.y << FPA.FIXED_SHIFT, 0, 0);
        } else if (evt.getPropertyName().equals(TrackMenuBar.EVENT_CLEAR_MAP)) {
            updateStatus(EMPTY_TEXT, null, EMPTY_TEXT, null,
                    EMPTY_TEXT, null, EMPTY_TEXT, null);
        }
    }
}
