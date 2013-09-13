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
package org.frezell.jboard.menu;

import java.awt.event.*;
import javax.swing.*;

public class MenuFactory {
	public static JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu menu;
		JMenuItem menuitem;

		menu = new JMenu("File");
		menuitem = new JMenuItem("Reset Game");
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Exit");
		menu.add(menuitem);
		bar.add(menu);

		menu = new JMenu("Edit");
		bar.add(menu);

		menu = new JMenu("View");
		menuitem = new JMenuItem("Flip View");
		menuitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Settings");
		menu.add(menuitem);
		bar.add(menu);

		menu = new JMenu("Step");
		bar.add(menu);

		menu = new JMenu("Help");
		menuitem = new JMenuItem("Hint...");
		menu.add(menuitem);
		menuitem = new JMenuItem("Book...");
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("About JBoard");
		menu.add(menuitem);
		bar.add(menu);

		return bar;
	}
}
