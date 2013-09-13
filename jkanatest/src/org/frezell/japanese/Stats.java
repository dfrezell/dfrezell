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

package org.frezell.japanese;

import org.frezell.util.swing.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Stats implements ActionListener {
    private static final int FLOW_H = 10;
    private static final int FLOW_V = 2;

    public static final int MODES = 3;
    public static final int LABELS = 6;

    public static final int CORRECT_LABEL = 0;
    public static final int INCORRECT_LABEL = 1;
    public static final int BEST_LABEL = 2;
    public static final int THIS_TIME_LABEL = 3;
    public static final int AVG_TIME_LABEL = 4;
    public static final int TOTAL_TIME_LABEL = 5;

    private JFrame m_stats;

    private int m_mode;
    private int m_correct;
    private int m_incorrect;
    private long m_time;

    private JLabel[][] m_statLabels;

    public Stats(int mode) {
        init();
        m_mode = mode;
    }

    public void init() {
        m_stats = new JFrame(JKanaTest.APPNAME + " - Statistics");
        m_stats.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JButton close = new JButton("Close");
        JPanel stats = new JPanel(new GridLayout(3, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        close.addActionListener(this);

        initStatLabels();
        stats.add(initStat(JKanaTest.MODE_HIRAGANA));
        stats.add(initStat(JKanaTest.MODE_KATAKANA));
        stats.add(initStat(JKanaTest.MODE_MIXED));

        buttonPanel.add(close);

        stats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Container c = m_stats.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(stats, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);

        m_stats.pack();
        GraphicsUtils.centerWindow(m_stats);
    }

    private JPanel initStat(int mode) {
        JPanel modePanel = new JPanel(new GridLayout(0, 1));
        JPanel correct = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JPanel incorrect = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JPanel best = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JPanel thisTime = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JPanel avgTime = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JPanel totalTime = new JPanel(new FlowLayout(FlowLayout.LEFT, FLOW_H, FLOW_V));
        JLabel correctLabel = new JLabel("Correct answers:");
        JLabel incorrectLabel = new JLabel("Incorrect answers:");
        JLabel bestLabel = new JLabel("Best result:");
        JLabel thisTimeLabel = new JLabel("This test time:");
        JLabel avgTimeLabel = new JLabel("Average test time:");
        JLabel totalTimeabel = new JLabel("Total test time:");
        Dimension d = new Dimension(100, 10);
        String label = "";

        if (mode == JKanaTest.MODE_HIRAGANA) {
            label = "HIRAGANA MODE - xx test(s)";
        } else if (mode == JKanaTest.MODE_KATAKANA) {
            label = "KATAKANA MODE - xx test(s)";
        } else if (mode == JKanaTest.MODE_MIXED) {
            label = "MIXED MODE - xx test(s)";
        }

        correct.add(correctLabel);
        correct.add(m_statLabels[mode][CORRECT_LABEL]);
        correct.add(Box.createRigidArea(d));

        incorrect.add(incorrectLabel);
        incorrect.add(m_statLabels[mode][INCORRECT_LABEL]);
        incorrect.add(Box.createRigidArea(d));

        best.add(bestLabel);
        best.add(m_statLabels[mode][BEST_LABEL]);
        best.add(Box.createRigidArea(d));

        thisTime.add(thisTimeLabel);
        thisTime.add(m_statLabels[mode][THIS_TIME_LABEL]);
        thisTime.add(Box.createRigidArea(d));

        avgTime.add(avgTimeLabel);
        avgTime.add(m_statLabels[mode][AVG_TIME_LABEL]);
        avgTime.add(Box.createRigidArea(d));

        totalTime.add(totalTimeabel);
        totalTime.add(m_statLabels[mode][TOTAL_TIME_LABEL]);
        totalTime.add(Box.createRigidArea(d));

        modePanel.add(correct);
        modePanel.add(incorrect);
        modePanel.add(best);
        modePanel.add(thisTime);
        modePanel.add(avgTime);
        modePanel.add(totalTime);
        modePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), label));

        return modePanel;
    }

    public void initStatLabels() {
        m_statLabels = new JLabel[MODES][LABELS];

        for (int i = 0; i < MODES; i++) {
            for (int j = 0; j < LABELS; j++) {
                m_statLabels[i][j] = new JLabel("-");
            }
        }
    }

    public void showStats() {
        m_stats.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        m_stats.setVisible(false);
    }

    public void setMode(int mode) {
        m_mode = mode;
    }

    public void start() {
        m_correct = 0;
        m_incorrect = 0;
        m_time = 0;
    }

    public void stop() {
        m_correct = 0;
        m_incorrect = 0;
        m_time = 0;
    }

    public void answer(boolean correct, long time) {
        if (correct) {
            m_correct++;
        } else {
            m_incorrect++;
        }

        m_time += time;

        m_statLabels[m_mode][CORRECT_LABEL].setText(String.valueOf(m_correct));
        m_statLabels[m_mode][INCORRECT_LABEL].setText(String.valueOf(m_incorrect));
        m_statLabels[m_mode][THIS_TIME_LABEL].setText(String.valueOf(millisToElapseTime(m_time)));

    }

    public void calculate() {

    }

    private String millisToElapseTime(long millis) {
        StringBuffer time = new StringBuffer();
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int millisecs = 0;

        millisecs = (int) (millis % 1000);
        millis /= 1000;

        if (millis > 0) {
            seconds = (int) (millis % 60);
            millis /= 60;
        }

        if (millis > 0) {
            minutes = (int) (millis % 60);
            millis /= 60;
        }

        if (millis > 0) {
            hours = (int) (millis % 24);
            millis /= 24;
        }

        if (millis > 0) {
            days = (int) (millis % 30);
            millis /= 30;
        }

        if (days != 0) {
            time.append(days).append(" days, ");
        }
        if (days > 0 || hours != 0) {
            time.append(hours).append(" hours, ");
        }
        if (days > 0 || hours > 0 || minutes != 0) {
            time.append(minutes).append(" minutes, ");
        }

        BigDecimal big = new BigDecimal((float) seconds + ((float) millisecs / 1000.0f));
        big.setScale(3, BigDecimal.ROUND_HALF_EVEN);
        time.append(big.floatValue());
        time.append(" seconds");

        return time.toString();
    }
}
