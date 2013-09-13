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

import javax.swing.*;
import java.awt.*;

import org.frezell.jboard.menu.*;

public class JBoard {
	private JFrame m_frame;

	public void init() {
		SpringLayout layout = new SpringLayout();

		m_frame = new JFrame("JBoard");
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.getContentPane().setLayout(layout);
		m_frame.setJMenuBar(MenuFactory.createMenuBar());

		Board board = new Board();
		ChessMovePanel moves = new ChessMovePanel(board);
		board.addMoveListener(moves, Chess.WHITE);
		board.addMoveListener(moves, Chess.BLACK);
		m_frame.getContentPane().add(board);
		m_frame.getContentPane().add(moves);

		doLayout(layout, board, moves);
		setContainerSize(m_frame.getContentPane());

		m_frame.pack();
		m_frame.setLocationRelativeTo(null);
	}

	public void setVisible(boolean vis) {
		m_frame.setVisible(vis);
	}

	public static void setContainerSize(Container parent) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component[] components = parent.getComponents();
		Spring maxHeightSpring = Spring.constant(0);
		SpringLayout.Constraints pCons = layout.getConstraints(parent);

		//Set the container's max X to the max X
		//of its rightmost component + padding.
		Component rightmost = components[components.length - 1];
		SpringLayout.Constraints rCons = layout.getConstraints(rightmost);
		pCons.setConstraint(SpringLayout.EAST, Spring.sum(Spring.constant(0),
				rCons.getConstraint(SpringLayout.EAST)));

		//Set the container's max Y to the max Y of its tallest
		//component + padding.
		for (int i = 0; i < components.length; i++) {
			SpringLayout.Constraints cons = layout.getConstraints(components[i]);
			maxHeightSpring = Spring.max(maxHeightSpring,
					cons.getConstraint(SpringLayout.SOUTH));
		}
		pCons.setConstraint(SpringLayout.SOUTH, Spring.sum(Spring.constant(0),
				maxHeightSpring));
	}

	private void doLayout(SpringLayout layout, Container board, Container moves) {
		// Set the position of the board to the upper right corner.
		SpringLayout.Constraints boardCons = layout.getConstraints(board);
		boardCons.setX(Spring.constant(0));
		boardCons.setY(Spring.constant(0));

		// Set the table to the right of the board.
		SpringLayout.Constraints movesCons = layout.getConstraints(moves);
		movesCons.setX(Spring.sum(Spring.constant(0),
				boardCons.getConstraint(SpringLayout.EAST)));
		movesCons.setY(Spring.constant(0));
		movesCons.setHeight(Spring.constant(board.getMinimumSize().height,
				board.getPreferredSize().height,
				board.getMaximumSize().height));
		movesCons.setWidth(Spring.constant((int) (board.getMinimumSize().width / 2.5),
				(int) (board.getPreferredSize().width / 2.5),
				(int) (board.getMaximumSize().width / 2.5)));
	}

	public static void main(String[] args) {
		JBoard app = new JBoard();
		app.init();
		app.setVisible(true);
	}
}