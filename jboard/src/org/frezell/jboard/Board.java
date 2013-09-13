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
import java.awt.event.*;

public class Board extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * This is based on the max size of a Square (256 pixels).  There are 8
	 * Squares plus the 1 pixel space of the border and cell spacing.  There
	 * are 9 such border/spacers.  So, 8 * 256 + 9 * 1 = 2057.  If we don't
	 * set, the panel assumes a max size of Short.MAX_VALUE, ignoring the
	 * max size of each cell in a GridLayout.
	 */
	private static final Dimension MAX_SIZE = new Dimension(2057, 2057);
	private static final int[] RESIZE = {-7, 0, -1, -2, -3, -4, -5, -6};

	private Square[] m_squares;
	private GameState m_state;
	private Piece m_movingPiece;
	private Square m_startSquare;
	private Point m_mouse;
	private Point m_oldMouse;


	public Board() {
		init();
	}

	public void init() {
		setLayout(new GridLayout(8, 8, 1, 1));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addMouseMotionListener(this);
		addMouseListener(this);
		setMaximumSize(MAX_SIZE);
		initSquares();
		m_state = new GameState(this);
	}

	public Square getSquareAt(int pos) {
		return m_squares[pos];
	}

	public void updateSquares(int from, int to) {
		m_squares[from].repaint();
		doRepaintPutdown(m_squares[to]);
	}

	public void captureEnpassant(int pos) {
		m_squares[pos].setPiece(null);
		m_squares[pos].repaint();
	}

	public void promotePawn(Piece piece, int end) {
		// TODO: check if auto queening, if not show dialog with promote choices
		piece.promoteTo(Chess.QUEEN);
		m_squares[end].repaint();
	}

	public void addMoveListener(MoveListener listener, int color) {
		m_state.addMoveListener(listener, color);
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (m_movingPiece != null && m_mouse != null) {
			m_movingPiece.paint(g, this, m_mouse.x, m_mouse.y,
					m_startSquare.getWidth(), m_startSquare.getHeight());
		}
	}

	public void setBounds(int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) {
			super.setBounds(x, y, w, h);
			return;
		}

		if (w != h) {
			w = h = Math.min(w, h);
		}
		h = w += RESIZE[w % 8];

		super.setBounds(x, y, w, h);
	}

	private void initSquares() {
		int pos;
		m_squares = new Square[64];

		// This draws the board correctly, with the first squre in the lower
		// left corner.  If we wanted to construct a rotated board (i.e. with
		// the black player perspective, we can change the for loops to:
		//
		//		for (int i = 0; i < 8; i++) {
		//			for (int j = 7; j >= 0; j--) {
		//				// construct
		//			}
		//		}
		int even = Square.LIGHT_COLOR;
		int odd = Square.DARK_COLOR;
		int swap;
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				pos = (i << 3) + j;
				m_squares[pos] = new Square((j % 2 == 0) ? even : odd, pos, this);
				add(m_squares[pos]);
			}
			swap = even;
			even = odd;
			odd = swap;
		}

		m_squares[Chess.A1].initPiece(new Piece(Chess.WHITE, Chess.ROOK));
		m_squares[Chess.B1].initPiece(new Piece(Chess.WHITE, Chess.KNIGHT));
		m_squares[Chess.C1].initPiece(new Piece(Chess.WHITE, Chess.BISHOP));
		m_squares[Chess.D1].initPiece(new Piece(Chess.WHITE, Chess.QUEEN));
		m_squares[Chess.E1].initPiece(new Piece(Chess.WHITE, Chess.KING));
		m_squares[Chess.F1].initPiece(new Piece(Chess.WHITE, Chess.BISHOP));
		m_squares[Chess.G1].initPiece(new Piece(Chess.WHITE, Chess.KNIGHT));
		m_squares[Chess.H1].initPiece(new Piece(Chess.WHITE, Chess.ROOK));
		m_squares[Chess.A2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.B2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.C2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.D2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.E2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.F2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.G2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));
		m_squares[Chess.H2].initPiece(new Piece(Chess.WHITE, Chess.PAWN));

		m_squares[Chess.A7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.B7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.C7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.D7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.E7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.F7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.G7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.H7].initPiece(new Piece(Chess.BLACK, Chess.PAWN));
		m_squares[Chess.A8].initPiece(new Piece(Chess.BLACK, Chess.ROOK));
		m_squares[Chess.B8].initPiece(new Piece(Chess.BLACK, Chess.KNIGHT));
		m_squares[Chess.C8].initPiece(new Piece(Chess.BLACK, Chess.BISHOP));
		m_squares[Chess.D8].initPiece(new Piece(Chess.BLACK, Chess.QUEEN));
		m_squares[Chess.E8].initPiece(new Piece(Chess.BLACK, Chess.KING));
		m_squares[Chess.F8].initPiece(new Piece(Chess.BLACK, Chess.BISHOP));
		m_squares[Chess.G8].initPiece(new Piece(Chess.BLACK, Chess.KNIGHT));
		m_squares[Chess.H8].initPiece(new Piece(Chess.BLACK, Chess.ROOK));
	}

	private final void doRepaintPickup(Square pickup) {
		repaint(m_mouse.x, m_mouse.y, pickup.getWidth(), pickup.getHeight());
		pickup.repaint();
	}

	private final void doRepaintDrag() {
		repaint(m_mouse.x, m_mouse.y, m_startSquare.getWidth(),
				m_startSquare.getHeight());
		repaint(m_oldMouse.x, m_oldMouse.y, m_startSquare.getWidth(),
				m_startSquare.getHeight());
	}

	private final void doRepaintPutdown(Square putdown) {
		if (m_mouse != null) {
			repaint(m_mouse.x, m_mouse.y, putdown.getWidth(), putdown.getHeight());
		}
		if (m_oldMouse != null) {
			repaint(m_oldMouse.x, m_oldMouse.y, putdown.getWidth(),
					putdown.getHeight());
		}
		putdown.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (m_mouse != null) {
			m_oldMouse = m_mouse;
			m_mouse = e.getPoint();
			m_mouse.translate(-(m_startSquare.getWidth() >> 1),
					-(m_startSquare.getWidth() >> 1));
			doRepaintDrag();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && m_movingPiece == null) {
			m_mouse = e.getPoint();
			Component c = getComponentAt(m_mouse);
			if (c instanceof Board) {
				c = getComponentAt(m_mouse.x - 1, m_mouse.y - 1);
			}
			if (c instanceof Square) {
				m_startSquare = (Square) c;
				m_movingPiece = m_startSquare.getPiece();
				m_mouse.translate(-(m_startSquare.getWidth() >> 1),
						-(m_startSquare.getWidth() >> 1));
				if (m_movingPiece != null) {
					m_startSquare.removePiece();
					doRepaintPickup(m_startSquare);
				} else {
					m_startSquare = null;
					m_mouse = null;
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		try {
			if (e.getButton() == MouseEvent.BUTTON1 && m_movingPiece != null) {
				Point p = e.getPoint();
				Component c = getComponentAt(p);
				if (c instanceof Board) {
					c = getComponentAt(p.x - 1, p.y - 1);
				}
				if (c instanceof Square) {
					Square endSquare = (Square) c;
					m_state.move(m_movingPiece, m_startSquare.getPosition(), endSquare.getPosition());
				} else {
					// We couldn't find the square they are droping on...so
					// we reset the position.
					m_startSquare.setPiece(m_movingPiece);
					doRepaintPutdown(m_startSquare);
				}
			}
		} finally {
			m_startSquare = null;
			m_mouse = null;
			m_oldMouse = null;
			m_movingPiece = null;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}