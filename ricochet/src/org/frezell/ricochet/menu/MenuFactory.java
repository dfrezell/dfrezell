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
package org.frezell.ricochet.menu;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuFactory {
    public static final String FILE = "File";
    public static final String NEW_GAME = "New Game";
    public static final String QUIT = "Quit";
    public static final String EDIT = "Edit";
    public static final String UNDO = "Undo";
    public static final String REDO = "Redo";
    public static final String RESET = "Reset";
    public static final String PREFERENCES = "Preferences";
    public static final String HELP = "Help";
    public static final String ABOUT = "About";

    public static JMenuBar createMenuBar(ActionListener listener) {
        JMenuBar bar = new JMenuBar();
        JMenu menu;
        JMenuItem menuitem;

        menu = new JMenu(FILE);
        menu.setMnemonic('F');
        menuitem = new JMenuItem(NEW_GAME);
        menuitem.setMnemonic('N');
        menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        menu.addSeparator();
        menuitem = new JMenuItem(QUIT);
        menuitem.setMnemonic('Q');
        menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        bar.add(menu);

        menu = new JMenu(EDIT);
        menu.setMnemonic('E');
        menuitem = new JMenuItem(UNDO);
        menuitem.setMnemonic('U');
        menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        menuitem = new JMenuItem(REDO);
        menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        menuitem.setMnemonic('R');
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        menu.addSeparator();
        menuitem = new JMenuItem(RESET);
        menuitem.setMnemonic('s');
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        menuitem = new JMenuItem(PREFERENCES);
        menuitem.setMnemonic('e');
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        bar.add(menu);

        menu = new JMenu(HELP);
        menu.setMnemonic('H');
        menuitem = new JMenuItem(ABOUT);
        menuitem.setMnemonic('A');
        menuitem.addActionListener(listener);
        menu.add(menuitem);
        bar.add(menu);

        return bar;
    }
}
