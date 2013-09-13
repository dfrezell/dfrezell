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

package org.frezell.jboard;

import javax.swing.table.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.2 $
 */
public class ChessMoveTableModel extends DefaultTableModel {
	public static final int COL_COUNT = 0;
	public static final int COL_WHITE = 1;
	public static final int COL_BLACK = 2;

	public ChessMoveTableModel() {
		addColumn("#");
		addColumn("White");
		addColumn("Black");
	}

	public void addRow(String move) {
		int count = getRowCount();
		if (move == null) {
			throw new NullPointerException("command can't be null.");
		}
		if (count > 0 && getValueAt(count - 1, COL_BLACK) == null) {
			setValueAt(move, count - 1, COL_BLACK);
			fireTableCellUpdated(count - 1, COL_BLACK);
		} else {
			addRow(new Object[]{new Integer(count + 1), move, null});
		}
	}
}
