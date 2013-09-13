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

import org.frezell.ricochet.menu.MenuFactory;
import org.frezell.util.PrefsManager;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class RicochetRobots implements ComponentListener {


    private JFrame m_frame;
    private GameState m_state;

    public void init() throws SAXException, ParserConfigurationException, IOException {
        m_frame = new JFrame("Ricochet Robots");
        m_state = new GameState(m_frame);
        m_frame.getContentPane().setLayout(new BorderLayout());
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_frame.setJMenuBar(MenuFactory.createMenuBar(m_state));

        Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = (int) (Math.min(maxBounds.getWidth(), maxBounds.getHeight()) / 16) * 14;
        int height = width;
        m_frame.setSize(PrefsManager.instance().getInt(getClass(), Prefs.PREF_WIDTH, width),
                PrefsManager.instance().getInt(getClass(), Prefs.PREF_HEIGHT, height));
        Point center = new Point();
        center.x = (maxBounds.width >> 1) - (m_frame.getWidth() >> 1);
        center.y = (maxBounds.height >> 1) - (m_frame.getHeight() >> 1);
        m_frame.setLocation(PrefsManager.instance().getInt(getClass(), Prefs.PREF_X, center.x),
                PrefsManager.instance().getInt(getClass(), Prefs.PREF_Y, center.y));

        m_frame.addComponentListener(this);
    }

    public void setVisible(boolean vis) {
        m_frame.setVisible(vis);
    }

    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(getClass(), Prefs.PREF_WIDTH, c.getWidth());
        PrefsManager.instance().putInt(getClass(), Prefs.PREF_HEIGHT, c.getHeight());
    }

    public void componentMoved(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(getClass(), Prefs.PREF_X, c.getX());
        PrefsManager.instance().putInt(getClass(), Prefs.PREF_Y, c.getY());
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    private static void writeErrorLog(Throwable error) {
        try {
            File file = new File(System.getProperty("user.home"), "ricochet_error.log");
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
            RicochetRobots app = new RicochetRobots();
            app.init();
            app.setVisible(true);
        } catch (Throwable t) {
            writeErrorLog(t);
        }
    }
}