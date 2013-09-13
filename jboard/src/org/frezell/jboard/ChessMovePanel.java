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

import org.frezell.jboard.event.*;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Drew Frezell
 * @version $Revision: 1.5 $
 */
public class ChessMovePanel extends JPanel implements MoveListener {
	private ChessMoveTableModel m_model;
	private JTable m_table;

	public ChessMovePanel(Board board) {
		init();
		board.addMoveListener(this, Chess.WHITE);
		board.addMoveListener(this, Chess.BLACK);
	}

	public void init() {
		m_table = new JTable();
		JScrollPane scrollPane = new JScrollPane(m_table);

		m_model = new ChessMoveTableModel();
		m_table.setModel(m_model);
		m_table.setColumnSelectionAllowed(false);
		m_table.getColumnModel().getColumn(0).setPreferredWidth(40);
		m_table.getColumnModel().getColumn(0).setMaxWidth(60);
		m_table.getColumnModel().getColumn(0).sizeWidthToFit();

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
		if (!(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		Point pt = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		rect.setLocation(rect.x - pt.x, rect.y - pt.y);

		// Scroll the area into view
		viewport.scrollRectToVisible(rect);
	}

	public void movePerformed(MoveEvent evt) {
		Move move = evt.getMove();
		m_model.addRow(move.getDescrip());
		scrollToVisible(m_table, m_model.getRowCount() - 1, 0);
	}
}
