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

import org.frezell.util.PrefsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrefsDialog extends JDialog implements ActionListener {
    public static final String USE_BLACK_ROBOT = "Use Black Robot";
    public static final String USE_CATCHUP_RULE = "Use Catch-Up Rule";
    public static final String OK_STR = "Ok";
    public static final String CANCEL_STR = "Cancel";

    private JCheckBox m_blackRobot;
    private JCheckBox m_catchUpRule;
    private JButton m_ok;
    private JButton m_cancel;
    private int m_exitStatus;

    public PrefsDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, "Preferences", modal);
        init(owner);
    }

    public void init(Frame owner) {
        JPanel prefs = new JPanel();
        JPanel blackRobotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel catchUpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        prefs.setLayout(new BoxLayout(prefs, BoxLayout.Y_AXIS));

        m_blackRobot = new JCheckBox(USE_BLACK_ROBOT,
                PrefsManager.instance().getBoolean(getClass(), Prefs.PREF_BLACK_ROBOT, false));
        m_catchUpRule = new JCheckBox(USE_CATCHUP_RULE,
                PrefsManager.instance().getBoolean(getClass(), Prefs.PREF_CATCH_UP, true));
        m_ok = new JButton(OK_STR);
        m_cancel = new JButton(CANCEL_STR);
        m_exitStatus = JOptionPane.CANCEL_OPTION;

        m_ok.addActionListener(this);
        m_cancel.addActionListener(this);

        blackRobotPanel.add(m_blackRobot);
        catchUpPanel.add(m_catchUpRule);
        okCancelPanel.add(m_ok);
        okCancelPanel.add(m_cancel);
        prefs.add(blackRobotPanel);
        prefs.add(catchUpPanel);
        prefs.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20),
                BorderFactory.createTitledBorder("Prefs")));
        getContentPane().add(Box.createRigidArea(new Dimension(300, 1)));
        getContentPane().add(prefs);
        getContentPane().add(okCancelPanel);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        pack();
        setLocationRelativeTo(owner);
    }

    public void savePreferences() {
        PrefsManager.instance().putBoolean(getClass(), Prefs.PREF_BLACK_ROBOT, m_blackRobot.isSelected());
        PrefsManager.instance().putBoolean(getClass(), Prefs.PREF_CATCH_UP, m_catchUpRule.isSelected());
    }

    public int getExitStatus() {
        return m_exitStatus;
    }

    public int showPrefsDialog() {
        show();
        dispose();

        return m_exitStatus;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd == OK_STR) {
            m_exitStatus = JOptionPane.OK_OPTION;
        } else {
            m_exitStatus = JOptionPane.CANCEL_OPTION;
        }

        hide();
    }
}
