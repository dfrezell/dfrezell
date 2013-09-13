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
import org.frezell.icfp.prefs.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.11 $
 */
public class TrackViewer extends JFrame implements ComponentListener, PropertyChangeListener {
    public static final String APPNAME = "ICFP Track Viewer";

    public static final String PREF_WIDTH = "width";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_X = "x";
    public static final String PREF_Y = "y";
    public static final String PREF_SPLITBARLOC = "splitbarloc";

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int SPLITBARLOC = 520;

    public TrackViewer(String title) {
        super(title);
    }

    public void init() {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        TrackMenuBar menubar = new TrackMenuBar();
        TrackPanel trackPanel = new TrackPanel();
        TracePanel tracePanel = new TracePanel();
        StatusPanel statusPanel = new StatusPanel();
        JScrollPane scrollPane = new JScrollPane(trackPanel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, tracePanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(PrefsManager.instance().getInt(TrackViewer.class, PREF_WIDTH, WIDTH),
                PrefsManager.instance().getInt(TrackViewer.class, PREF_HEIGHT, HEIGHT));
        setLocation(PrefsManager.instance().getInt(TrackViewer.class, PREF_X, center.x - getWidth() / 2),
                PrefsManager.instance().getInt(TrackViewer.class, PREF_Y, center.y - getHeight() / 2));

        menubar.init();
        setJMenuBar(menubar);

        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_OPEN_MAP, RaceManager.instance());
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_CLEAR_MAP, RaceManager.instance());
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_OPEN_TRACE, RaceManager.instance());
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_CLEAR_TRACE, RaceManager.instance());
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_NEW_TRACE, RaceManager.instance());

        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_OPEN_MAP, trackPanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_CLEAR_MAP, trackPanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_OPEN_MAP, statusPanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_CLEAR_MAP, statusPanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_CLEAR_TRACE, tracePanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_NEW_TRACE, tracePanel);
        menubar.addPropertyChangeListener(TrackMenuBar.EVENT_OPEN_TRACE, tracePanel);

        RaceManager.instance().addPropertyChangeListener(RaceManager.EVENT_CAR_MOVED, trackPanel);
        RaceManager.instance().addPropertyChangeListener(RaceManager.EVENT_CAR_MOVED, statusPanel);
        RaceManager.instance().addPropertyChangeListener(RaceManager.EVENT_CAR_MOVED, tracePanel);
        RaceManager.instance().addPropertyChangeListener(RaceManager.EVENT_CAR_STOPPED, tracePanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        splitPane.setDividerLocation(PrefsManager.instance().getInt(
                TrackViewer.class, PREF_SPLITBARLOC, SPLITBARLOC));
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(1.0);
        // We listen to update on the location of the divider bar so we can
        // store the new value in the preferences.
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, this);

        addComponentListener(this);
    }


    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(TrackViewer.class, PREF_WIDTH, c.getWidth());
        PrefsManager.instance().putInt(TrackViewer.class, PREF_HEIGHT, c.getHeight());
    }

    public void componentMoved(ComponentEvent e) {
        Component c = e.getComponent();
        PrefsManager.instance().putInt(TrackViewer.class, PREF_X, c.getX());
        PrefsManager.instance().putInt(TrackViewer.class, PREF_Y, c.getY());
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == JSplitPane.DIVIDER_LOCATION_PROPERTY) {
            PrefsManager.instance().putInt(TrackViewer.class, PREF_SPLITBARLOC,
                    ((Integer) evt.getNewValue()).intValue());
        }
    }

    public static void main(String[] args) {
        TrackViewer viewer = new TrackViewer(APPNAME);
        viewer.init();
        viewer.setVisible(true);
    }
}
