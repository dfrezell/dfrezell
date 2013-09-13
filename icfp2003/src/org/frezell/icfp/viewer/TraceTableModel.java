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

import org.frezell.icfp.util.*;

import javax.swing.table.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.6 $
 */
public class TraceTableModel extends DefaultTableModel {
    public static final int COL_COUNT = 0;
    public static final int COL_COMMAND = 1;

    public TraceTableModel() {
        addColumn("Count");
        addColumn("Command");
    }

    public void addRow(String command) {
        int count = getRowCount();
        if (command == null) {
            throw new NullPointerException("command can't be null.");
        }
        if (count != 0 && command.equals(getValueAt(count - 1, COL_COMMAND))) {
            ((MutableInteger) getValueAt(count - 1, COL_COUNT)).add(1);
            fireTableCellUpdated(count - 1, COL_COUNT);
        } else {
            addRow(new Object[] {new MutableInteger(1), command });
        }
    }
}
