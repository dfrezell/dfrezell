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

import org.frezell.util.RandomLib;
import org.frezell.util.prefs.PrefsManager;
import org.frezell.util.swing.GraphicsUtils;
import org.frezell.util.swing.SwingWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

public class JKanaTest extends JFrame implements ComponentListener, ActionListener {
    public static final String APPNAME = "JKana Test";
    public static final String INTRO_TEXT = "Select test mode and press <Start>";

    public static final String PREF_WIDTH = "width";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_X = "x";
    public static final String PREF_Y = "y";

    public static final int WIDTH = 280;
    public static final int HEIGHT = 420;
    public static final float FONT_SIZE = 100.0f;

    public static final int WRONG_DELAY = 2000;

    public static final int MODE_HIRAGANA = 0;
    public static final int MODE_KATAKANA = 1;
    public static final int MODE_MIXED = 2;

    public static final int RESET_PROGRESS = -1;
    public static final int INIT_PROGRESS = 0;

    public static final String STR_KATAKANA = "Katakana";
    public static final String STR_HIRAGANA = "Hiragana";
    public static final String STR_MIXED = "Mixed";
    public static final String STR_REPEAT = "Repeat wrongly-answered questions";
    public static final String STR_STATS = "Stats";
    public static final String STR_ABOUT = "About";
    public static final String STR_START = "Start";
    public static final String STR_STOP = "Stop";
    public static final String STR_QUIT = "Quit";
    public static final String STR_FILE = "File";
    public static final String STR_HELP = "Help";

    private JPanel m_jpTextPanel;
    private JTextField m_jpText;
    private JButton m_startButton;
    private JButton m_stopButton;
    private JTextField m_romajiInput;
    private ButtonGroup m_modeGroup;
    private JLabel m_feedback;
    private JProgressBar m_progress;

    private int m_mode = MODE_HIRAGANA;
    private JCheckBox m_repeat;
    private Font m_font;
    /**
     * The current kana to select the next character from.
     */
    private Kana[] m_kana;
    /**
     * The millisecond time of the last answered question.
     */
    private long m_millis;
    /**
     * The randomly ordered list of character indices for the Kana.  For
     * mixed mode operation, the high bit is set for the Katakana characters,
     * so use KATAKANA_MASK to get the index of katakana array.
     */
    private int[] m_random;
    /**
     * The current index in the random array.
     */
    private int m_randomIndex;

    private Stats m_stats;

    public JKanaTest(String title) {
        super(title);
    }

    public void init() {
        JPanel mainPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        m_progress = new JProgressBar();
        m_progress.setString(INTRO_TEXT);
        m_progress.setStringPainted(true);

        JPanel romajiStatPanel = new JPanel();
        JPanel romajiPanel = new JPanel();

        JPanel repeatPanel = new JPanel();
        m_repeat = new JCheckBox(STR_REPEAT);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(STR_FILE);
        JMenu helpMenu = new JMenu(STR_HELP);
        JMenuItem statMenuItem = new JMenuItem(STR_STATS);
        JMenuItem quitMenuItem = new JMenuItem(STR_QUIT);
        JMenuItem aboutMenuItem = new JMenuItem(STR_ABOUT);

        JPanel testModePanel = new JPanel();
        JRadioButton hiragana = new JRadioButton(STR_HIRAGANA);
        JRadioButton katakana = new JRadioButton(STR_KATAKANA);
        JRadioButton mixed = new JRadioButton(STR_MIXED);
        m_modeGroup = new ButtonGroup();

        m_feedback = new JLabel();

        JPanel startStopQuitPanel = new JPanel();
        JButton quit = new JButton(STR_QUIT);

        m_kana = Kana.getKanaList();

        m_startButton = new JButton(STR_START);
        m_stopButton = new JButton(STR_STOP);
        m_romajiInput = new JTextField();
        m_jpTextPanel = new JPanel(new GridLayout(1, 1));
        m_jpText = new JTextField() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                if (g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING) !=
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                }
                super.paint(g);
            }
        };

        initPreferences();

        // Initialize the random library so we get different set each time...
        // numbers taken from RandomLib, these are the internal limits.
        RandomLib.initialize((int) (System.currentTimeMillis() % 31328L),
                (int) ((System.currentTimeMillis() * 3) % 30081L));

        // Add all code below this point, we want to run the preferences first.
        // It's ok to create the objects above, but don't add anything except
        // after this point.
        fileMenu.setMnemonic('f');
        helpMenu.setMnemonic('h');
        statMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        statMenuItem.setMnemonic('s');
        statMenuItem.addActionListener(this);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        quitMenuItem.setMnemonic('q');
        quitMenuItem.addActionListener(this);
        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.addActionListener(this);
        fileMenu.add(statMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);
        helpMenu.add(aboutMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        m_stats = new Stats(m_mode);

        m_feedback.setMinimumSize(new Dimension(80, 20));
        m_feedback.setPreferredSize(new Dimension(120, 20));

        m_jpText.setFont(m_font);

        m_modeGroup.add(hiragana);
        m_modeGroup.add(katakana);
        m_modeGroup.add(mixed);
        hiragana.setSelected(true);

        m_repeat.setFont(new Font(m_repeat.getFont().getFontName(), Font.PLAIN, 10));
        hiragana.setFont(new Font(hiragana.getFont().getFontName(), Font.PLAIN, 10));
        katakana.setFont(new Font(katakana.getFont().getFontName(), Font.PLAIN, 10));
        mixed.setFont(new Font(mixed.getFont().getFontName(), Font.PLAIN, 10));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        m_jpTextPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        m_jpText.setHorizontalAlignment(JTextField.CENTER);
        m_jpText.setEditable(false);
        m_jpText.setBackground(Color.white);

        m_jpTextPanel.add(m_jpText, "Text");

        m_progress.setBackground(Color.white);
        m_progress.setPreferredSize(new Dimension(m_progress.getPreferredSize().width, 25));
        m_progress.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        romajiStatPanel.setLayout(new BoxLayout(romajiStatPanel, BoxLayout.X_AXIS));
        romajiPanel.setBorder(BorderFactory.createTitledBorder("Romaji:"));
        romajiPanel.add(m_romajiInput);
        romajiPanel.setLayout(new BoxLayout(romajiPanel, BoxLayout.X_AXIS));
        romajiStatPanel.add(romajiPanel);
        romajiStatPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        romajiStatPanel.add(m_feedback);
        romajiStatPanel.setMaximumSize(new Dimension(Short.MAX_VALUE,
                romajiStatPanel.getPreferredSize().height));
        m_romajiInput.addActionListener(this);
        m_romajiInput.setEnabled(false);
        m_romajiInput.setMinimumSize(new Dimension(30, 20));

        repeatPanel.setBorder(BorderFactory.createEtchedBorder());
        repeatPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        repeatPanel.add(m_repeat);
        repeatPanel.setMaximumSize(new Dimension(Short.MAX_VALUE,
                repeatPanel.getPreferredSize().height));
        m_repeat.addActionListener(this);

        testModePanel.setBorder(BorderFactory.createTitledBorder("Test mode:"));
        testModePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        testModePanel.add(hiragana);
        testModePanel.add(katakana);
        testModePanel.add(mixed);
        testModePanel.setMaximumSize(new Dimension(Short.MAX_VALUE,
                testModePanel.getPreferredSize().height));
        hiragana.addActionListener(this);
        katakana.addActionListener(this);
        mixed.addActionListener(this);

        startStopQuitPanel.setLayout(new BoxLayout(startStopQuitPanel, BoxLayout.X_AXIS));
        startStopQuitPanel.add(m_startButton);
        startStopQuitPanel.add(Box.createHorizontalGlue());
        startStopQuitPanel.add(m_stopButton);
        startStopQuitPanel.add(Box.createHorizontalGlue());
        startStopQuitPanel.add(quit);
        m_startButton.addActionListener(this);
        m_stopButton.addActionListener(this);
        m_stopButton.setEnabled(false);
        quit.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(PrefsManager.instance().getInt(getClass(), PREF_WIDTH, WIDTH),
                PrefsManager.instance().getInt(getClass(), PREF_HEIGHT, HEIGHT));
        Point center = GraphicsUtils.getCenterOffset(this);
        setLocation(PrefsManager.instance().getInt(getClass(), PREF_X, center.x),
                PrefsManager.instance().getInt(getClass(), PREF_Y, center.y));

        buttonPanel.add(romajiStatPanel);
        buttonPanel.add(repeatPanel);
        buttonPanel.add(testModePanel);
        buttonPanel.add(startStopQuitPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        mainPanel.add(m_jpTextPanel);
        mainPanel.add(m_progress);
        mainPanel.add(buttonPanel);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createEtchedBorder()));

        getContentPane().add(mainPanel);

        addComponentListener(this);
    }

    private void initPreferences() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            InputStream is = loader.getResourceAsStream("font/OpenKana.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(FONT_SIZE);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayNextKana() {
        // Get the proper Kana character array we need to display the next
        // character.  This depends on the random index, so if that changes
        // then getKana() needs to change as well.
//        String[] kana = getKana();
        int index = m_random[m_randomIndex++];

        // We always mask out the Katakana flag, this will only be set when we
        // are in mixed mode and the next random item is a katakana character.
        // but it seems better then always checking the mode then masking.  We
        // just always mask since it does nothing to the value when the other
        // modes are set, it returns the same number.
        m_jpText.setText(m_kana[index].getHiragana());
    }

    private void shuffleList(int[] list) {
        reshuffleList(list, 0);
    }

    private void reshuffleList(int[] list, int start) {
        int rand = 0;
        int swap = 0;
        for (int i = start; i < list.length; i++) {
            rand = RandomLib.randomInt(start, i - 1);
            swap = list[i];
            list[i] = list[rand];
            list[rand] = swap;
        }
    }

    private void setProgress(int value) {
        if (value == RESET_PROGRESS) {
            m_progress.setString(INTRO_TEXT);
            m_progress.setValue(0);
        } else if (value == INIT_PROGRESS) {
            m_progress.setMaximum(m_random.length);
            m_progress.setString((m_randomIndex + 1) + " / " + m_random.length);
            m_progress.setValue(1);
        } else {
            m_progress.setString((m_randomIndex + 1) + " / " + m_random.length);
            m_progress.setValue(m_progress.getValue() + 1);
        }
    }

    private void doVerifyRomaji(String romaji) {
        // Index of the displayed kana character
        int index = m_random[m_randomIndex - 1];

        final long end = System.currentTimeMillis();

        // Check to see if we are at the end of our test.  If so
        // we want to stop the test and calculate the stats.
        if (m_randomIndex == m_random.length) {
            m_stopButton.doClick();
            return;
        }

        // Check to see if our indices match, if so the user got it right.
        if (m_kana[index].isRomajiMatch(romaji)) {
            m_stats.answer(true, end - m_millis);
            setProgress(m_randomIndex + 1);
            displayNextKana();
            m_millis = end;
        } else {
            if (!m_repeat.isSelected()) {
                setProgress(m_randomIndex + 1);
            }
            
            m_stats.answer(false, end - m_millis);
            m_jpText.setForeground(Color.red);
            m_jpText.setText(m_kana[index].getRomaji());
            m_romajiInput.setEnabled(false);

            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    try {
                        Thread.sleep(WRONG_DELAY);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    return null;
                }

                public void finished() {
                    m_jpText.setForeground(Color.black);
                    displayNextKana();

                    m_romajiInput.setEnabled(true);
                    m_romajiInput.requestFocus();
                    m_millis = end;
                }
            };

            worker.start();
        }

        m_romajiInput.setText("");
    }

    private void doKatakana() {
        m_mode = MODE_KATAKANA;
        m_stats.setMode(m_mode);
    }

    private void doHiragana() {
        m_mode = MODE_HIRAGANA;
        m_stats.setMode(m_mode);
    }

    private void doMixed() {
        m_mode = MODE_MIXED;
        m_stats.setMode(m_mode);
    }

    private void doRepeat() {
    }

    private void doStats() {
        m_stats.showStats();
    }

    private void doAbout() {
        JOptionPane.showMessageDialog(null, "JKana Test v0.0.5 - Andrew Frezell <dfrezell@speakeasy.net>", "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void doStart() {
        m_startButton.setEnabled(false);
        m_stopButton.setEnabled(true);
        m_romajiInput.setEnabled(true);
        m_repeat.setEnabled(false);

        switch (m_mode) {
            case MODE_HIRAGANA:
            case MODE_KATAKANA:
                m_random = new int[m_kana.length];
                break;
            case MODE_MIXED:
                m_random = new int[m_kana.length * 2];
                break;
        }

        // Initialize the random list with the data
        for (int i = 0; i < m_random.length; i++) {
            m_random[i] = i;
        }
        // Shuffle the data around to make list random.
        shuffleList(m_random);
        // Reset the index to the start of the array.
        m_randomIndex = 0;
        setProgress(INIT_PROGRESS);
        m_stats.start();

        displayNextKana();
        m_romajiInput.requestFocus();
        for (Enumeration e = m_modeGroup.getElements(); e.hasMoreElements();) {
            ((JRadioButton) e.nextElement()).setEnabled(false);
        }

        m_millis = System.currentTimeMillis();
    }

    private void doStop() {
        m_startButton.setEnabled(true);
        m_stopButton.setEnabled(false);
        m_romajiInput.setEnabled(false);
        m_romajiInput.setText("");
        m_jpText.setText("");
        m_repeat.setEnabled(true);
        setProgress(RESET_PROGRESS);

        for (Enumeration e = m_modeGroup.getElements(); e.hasMoreElements();) {
            ((JRadioButton) e.nextElement()).setEnabled(true);
        }
        // Check to see if the test stopped because they reached
        // the end of the test or if they stopped it manually.
        if (m_randomIndex == m_random.length) {
            m_stats.calculate();
        } else {
            m_stats.stop();
        }
        m_random = null;
        m_randomIndex = 0;
    }

    private void doQuit() {
        System.exit(0);
    }

    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(getClass(), PREF_WIDTH, c.getWidth());
        PrefsManager.instance().putInt(getClass(), PREF_HEIGHT, c.getHeight());
    }

    public void componentMoved(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(getClass(), PREF_X, c.getX());
        PrefsManager.instance().putInt(getClass(), PREF_Y, c.getY());
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();

        if (event.getSource() == m_romajiInput) {
            doVerifyRomaji(cmd);
        } else if (cmd == STR_KATAKANA) {
            doKatakana();
        } else if (cmd == STR_HIRAGANA) {
            doHiragana();
        } else if (cmd == STR_MIXED) {
            doMixed();
        } else if (cmd == STR_REPEAT) {
            doRepeat();
        } else if (cmd == STR_STATS) {
            doStats();
        } else if (cmd == STR_ABOUT) {
            doAbout();
        } else if (cmd == STR_START) {
            doStart();
        } else if (cmd == STR_STOP) {
            doStop();
        } else if (cmd == STR_QUIT) {
            doQuit();
        } else {
            System.out.println(event.toString());
        }
    }

    private static void writeErrorLog(Throwable error) {
        try {
            File file = new File(System.getProperty("user.home"), "jkanatest_error.log");
            PrintWriter fout = new PrintWriter(new FileOutputStream(file));

            error.printStackTrace(fout);
            fout.close();

            JOptionPane.showMessageDialog(null, "Error:  Please email log:  " + file.getAbsolutePath() +
                    " to <dfrezell@speakeasy.net>.");

            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            JKanaTest viewer = new JKanaTest(APPNAME);
            viewer.init();
            viewer.setVisible(true);
        } catch (Throwable t) {
            writeErrorLog(t);
        }
    }
}
