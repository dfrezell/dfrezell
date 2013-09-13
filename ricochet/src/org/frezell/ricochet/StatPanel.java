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

package org.frezell.ricochet;

import org.frezell.ricochet.event.GameStatusEvent;
import org.frezell.ricochet.event.GameStatusListener;
import org.frezell.ricochet.event.MoveEvent;
import org.frezell.ricochet.event.MoveListener;
import org.frezell.swing.ImageButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

public class StatPanel extends JPanel implements MoveListener,
        GameStatusListener, ActionListener {
    public static final int PANEL_BG = 0;
    public static final int PICK_TARGET_ENABLED = 1;
    public static final int PICK_TARGET_DISABLED = 2;
    public static final int NUM_IMAGES = 3;

    public static final String PICK_TARGET = "PickTarget";
    public static final String BID = "Bid";

    private static BufferedImage[] g_origImages;

    private JTextField m_countField;
    private int m_count;
    private ImageButton m_pickTarget;
    private JTextField m_bidAmount;

    static {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String dir = "img/";

        g_origImages = new BufferedImage[NUM_IMAGES];

        try {
            g_origImages[PANEL_BG] = ImageIO.read(loader.getResource(dir + "Stat_Panel.png"));
            g_origImages[PICK_TARGET_ENABLED] = ImageIO.read(loader.getResource(dir + "Pick_Target_Enable.png"));
            g_origImages[PICK_TARGET_DISABLED] = ImageIO.read(loader.getResource(dir + "Pick_Target_Disable.png"));
        } catch (IOException ioe) {
            Logger.getAnonymousLogger().warning("Error loading images: " + ioe.getMessage());
        }
    }

    public StatPanel() {
        init();
    }

    public void init() {
        m_countField = new JTextField(6);
        m_pickTarget = new ImageButton(g_origImages[PICK_TARGET_ENABLED]);
        m_bidAmount = new JTextField(6);

        setPreferredSize(new Dimension(Short.MAX_VALUE, g_origImages[PANEL_BG].getHeight()));
        setLayout(null);
        setOpaque(false);

        m_countField.setEditable(false);
        m_countField.setText(Integer.toString(m_count));
        m_countField.setForeground(Color.GREEN);
        m_countField.setBackground(Color.BLACK);
        m_countField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(58, 49, 49)),
                BorderFactory.createLineBorder(new Color(148, 148, 148))));
        m_countField.setSize(70, 20);
        m_countField.setLocation(54, 10);

        m_bidAmount.setForeground(Color.GREEN);
        m_bidAmount.setBackground(Color.BLACK);
        m_bidAmount.setCaretColor(Color.WHITE);
        m_bidAmount.setName(BID);
        m_bidAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(58, 49, 49)),
                BorderFactory.createLineBorder(new Color(148, 148, 148))));
        m_bidAmount.setSize(70, 20);
        m_bidAmount.setLocation(183, 10);

        m_pickTarget.addActionListener(this);
        m_pickTarget.setName(PICK_TARGET);
        m_pickTarget.setDisabledImage(g_origImages[PICK_TARGET_DISABLED]);
        m_pickTarget.setSize(g_origImages[PICK_TARGET_DISABLED].getWidth(),
                g_origImages[PICK_TARGET_DISABLED].getHeight());
        m_pickTarget.setLocation(397, 7);

        add(m_countField);
        add(m_bidAmount);
        add(m_pickTarget);
    }

    public void setCount(int count) {
        m_count = count;
        m_countField.setText(" " + Integer.toString(count));
    }

    public int getCount() {
        return m_count;
    }

    public void addPickTargetListener(ActionListener listener) {
        m_pickTarget.addActionListener(listener);
    }

    public void addBidListener(ActionListener listener) {
        m_bidAmount.addActionListener(listener);
    }

    private void setPickTargetEnabled(boolean enable) {
        m_pickTarget.setEnabled(enable);
    }

    public void paint(Graphics g) {
        g.drawImage(g_origImages[PANEL_BG], 0, 0, null);
        super.paint(g);
    }

    public void movePerformed(MoveEvent e) {
        setCount(++m_count);
    }

    public void gameStatusChanged(GameStatusEvent e) {
        int status = e.getStatus();

        switch (status) {
            case GameStatusEvent.NEW_GAME:
                setCount(0);
                setPickTargetEnabled(true);
                break;
            case GameStatusEvent.RESET_POSITION:
                setCount(0);
                break;
            case GameStatusEvent.UNDO:
                setCount(--m_count);
                break;
            case GameStatusEvent.REDO:
                setCount(++m_count);
                break;
            case GameStatusEvent.TARGET_REACHED:
                setCount(0);
                setPickTargetEnabled(true);
                break;
            case GameStatusEvent.TARGET_NOT_REACHED:
                setCount(0);
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m_pickTarget) {
            setPickTargetEnabled(false);
        }
    }
}
