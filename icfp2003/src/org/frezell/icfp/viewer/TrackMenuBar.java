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

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.frezell.icfp.*;
import org.frezell.icfp.prefs.*;
import org.frezell.icfp.viewer.util.*;
import org.frezell.icfp.viewer.util.SwingWorker;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.10 $
 */
public class TrackMenuBar extends JMenuBar implements ActionListener {
    public static final String EVENT_OPEN_MAP = "OpenMap";
    public static final String EVENT_CLEAR_MAP = "ClearMap";
    public static final String EVENT_OPEN_TRACE = "OpenTrace";
    public static final String EVENT_NEW_TRACE = "NewTrace";
    public static final String EVENT_CLEAR_TRACE = "ClearTrace";
    public static final String EVENT_UNDO = "Undo";
    public static final String EVENT_REDO = "Redo";

    private static final String PREF_OPENDIR = "OpenDir";

    private static final String MENUITEM_OPEN_RACETRACK = "Open Racetrack...";
    private static final String MENUITEM_OPEN_TRACE = "Open Trace...";
    private static final String MENUITEM_NEW_TRACE = "New Trace...";

    private static final String MENUITEM_CLOSE_RACETRACK = "Close Racetrack";
    private static final String MENUITEM_CLOSE_TRACE = "Close Trace";

    private static final String MENUITEM_UNDO = "Undo";
    private static final String MENUITEM_REDO = "Redo";

    private static final String MENUITEM_ABOUT = "About";

    private static final String MENUITEM_EXIT = "Exit";

    public void init() {
        JMenu menu;
        JMenuItem menuitem;

        menu = new JMenu("File");
        menu.setMnemonic('f');
        menuitem = new JMenuItem(MENUITEM_OPEN_RACETRACK);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        menuitem.setMnemonic('o');
        menuitem.addActionListener(this);
        menu.add(menuitem);

        menuitem = new JMenuItem(MENUITEM_NEW_TRACE);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        menuitem.setMnemonic('n');
        menuitem.addActionListener(this);
        menuitem.setEnabled(false);
        menu.add(menuitem);

        menuitem = new JMenuItem(MENUITEM_OPEN_TRACE);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control shift O"));
        menuitem.setMnemonic('p');
        menuitem.addActionListener(this);
        menuitem.setEnabled(false);
        menu.add(menuitem);

        menu.addSeparator();

        menuitem = new JMenuItem(MENUITEM_CLOSE_RACETRACK);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control shift R"));
        menuitem.setMnemonic('m');
        menuitem.addActionListener(this);
        menuitem.setEnabled(false);
        menu.add(menuitem);

        menuitem = new JMenuItem(MENUITEM_CLOSE_TRACE);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control shift T"));
        menuitem.setMnemonic('t');
        menuitem.addActionListener(this);
        menuitem.setEnabled(false);
        menu.add(menuitem);

        menu.addSeparator();

        menuitem = new JMenuItem(MENUITEM_EXIT);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control X"));
        menuitem.setMnemonic('x');
        menuitem.addActionListener(this);
        menu.add(menuitem);
        add(menu);

        menu = new JMenu("Edit");
        menu.setMnemonic('e');
        menuitem = new JMenuItem(MENUITEM_UNDO);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        menuitem.setMnemonic('u');
        menuitem.addActionListener(this);
        menu.add(menuitem);
        menuitem = new JMenuItem(MENUITEM_REDO);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control shift Z"));
        menuitem.setMnemonic('r');
        menuitem.addActionListener(this);
        menu.add(menuitem);
        add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic('h');
        menuitem = new JMenuItem(MENUITEM_ABOUT);
        menuitem.setAccelerator(KeyStroke.getKeyStroke("control A"));
        menuitem.setMnemonic('a');
        menuitem.addActionListener(this);
        menu.add(menuitem);
        add(menu);
    }

    private void doOpenRacetrack() {
        SwingWorker worker = new SwingWorker() {
            public Object construct() {
                File file = showOpenFileDialog(new TrackFileFilter());

                try {
                    if (file != null) {
                        return Track.getTrack(file);
                    }
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }

                return null;
            }

            public void finished() {
                Track track = (Track) get();
                if (track != null) {
                    setMenusEnabled(MENUITEM_OPEN_RACETRACK);
                    firePropertyChange(EVENT_OPEN_MAP, null, track);
                }
            }
        };

        worker.start();
    }

    private void doOpenTrace() {
        SwingWorker worker = new SwingWorker() {
            public Object construct() {
                File file = showOpenFileDialog(new TraceFileFilter());

                try {
                    if (file != null) {
                        return Trace.readTrace(file);
                    }
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }

                return null;
            }

            public void finished() {
                Trace trace = (Trace) get();
                if (trace != null) {
                    setMenusEnabled(MENUITEM_OPEN_TRACE);
                    firePropertyChange(EVENT_OPEN_TRACE, null, trace);
                }
            }
        };

        worker.start();
    }

    private void doCloseRacetrack() {
        setMenusEnabled(MENUITEM_CLOSE_RACETRACK);
        firePropertyChange(EVENT_CLEAR_MAP, null, null);
    }

    private void doCloseTrace() {
        setMenusEnabled(MENUITEM_CLOSE_TRACE);
        firePropertyChange(EVENT_CLEAR_TRACE, null, null);
    }

    private void doNewTrace() {
        setMenusEnabled(MENUITEM_NEW_TRACE);
        firePropertyChange(EVENT_NEW_TRACE, null, null);
    }

    private void doUndo() {
        firePropertyChange(EVENT_UNDO, null, null);
    }

    private void doRedo() {
        firePropertyChange(EVENT_REDO, null, null);
    }

    private void doAbout() {
        About about = new About();
        about.show();
    }

    private void doExit() {
        System.exit(0);
    }

    private void setMenusEnabled(String caller) {
        if (caller == MENUITEM_OPEN_RACETRACK) {
            getMenuItemNamed(MENUITEM_OPEN_TRACE).setEnabled(true);
            getMenuItemNamed(MENUITEM_NEW_TRACE).setEnabled(true);
            getMenuItemNamed(MENUITEM_CLOSE_RACETRACK).setEnabled(true);
        } else if (caller == MENUITEM_OPEN_TRACE) {
            getMenuItemNamed(MENUITEM_CLOSE_TRACE).setEnabled(true);
        } else if (caller == MENUITEM_CLOSE_RACETRACK) {
            getMenuItemNamed(MENUITEM_OPEN_TRACE).setEnabled(false);
            getMenuItemNamed(MENUITEM_NEW_TRACE).setEnabled(false);
            getMenuItemNamed(MENUITEM_CLOSE_RACETRACK).setEnabled(false);
        } else if (caller == MENUITEM_CLOSE_TRACE) {
            getMenuItemNamed(MENUITEM_CLOSE_TRACE).setEnabled(false);
        } else if (caller == MENUITEM_NEW_TRACE) {
            getMenuItemNamed(MENUITEM_CLOSE_TRACE).setEnabled(true);
        }
    }

    private JMenuItem getMenuItemNamed(String name) {
        JMenu menu = getMenu(0);
        int count = menu.getItemCount();
        JMenuItem item;

        for (int i = 0; i < count; i++) {
            item = menu.getItem(i);
            if (item != null && item.getText() == name) {
                return item;
            }
        }

        return null;
    }

    private File showOpenFileDialog(FileFilter filter) {
        File file = null;
        JFileChooser fileChooser = new JFileChooser(
                PrefsManager.instance().get(TrackMenuBar.class,
                        PREF_OPENDIR, System.getProperty("user.home")));
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            PrefsManager.instance().put(TrackMenuBar.class,
                    PREF_OPENDIR, fileChooser.getCurrentDirectory().getAbsolutePath());
        }

        return file;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand().intern();

        if (command == MENUITEM_OPEN_RACETRACK) {
            doOpenRacetrack();
        } else if (command == MENUITEM_OPEN_TRACE) {
            doOpenTrace();
        } else if (command == MENUITEM_CLOSE_TRACE) {
            doCloseTrace();
        } else if (command == MENUITEM_NEW_TRACE) {
            doNewTrace();
        } else if (command == MENUITEM_CLOSE_RACETRACK) {
            doCloseRacetrack();
        } else if (command == MENUITEM_UNDO) {
            doUndo();
        } else if (command == MENUITEM_REDO) {
            doRedo();
        } else if (command == MENUITEM_ABOUT) {
            doAbout();
        } else if (command == MENUITEM_EXIT) {
            doExit();
        }
    }

    private class TrackFileFilter extends FileFilter {
        private static final String TRACK_EXT = "trk";

        public boolean accept(File pathname) {
            String extension = getExtension(pathname);
            return pathname.isDirectory() ||
                    (extension != null && extension.equals(TRACK_EXT));
        }

        public String getDescription() {
            return "Track Data";
        }

        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }

    private class TraceFileFilter extends FileFilter {
        private static final String TRACK_EXT = "trc";

        public boolean accept(File pathname) {
            String extension = getExtension(pathname);
            return pathname.isDirectory() ||
                    (extension != null && extension.equals(TRACK_EXT));
        }

        public String getDescription() {
            return "Trace File";
        }

        private String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
}
