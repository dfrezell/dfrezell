/**
 * Copyright (c) 2002, 2003 Drew Frezell
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

import org.frezell.jboard.engine.*;
import org.frezell.jboard.event.*;

import java.awt.*;
import java.util.*;

public class GameState {
	private int m_turn = Chess.WHITE;
	private Board m_board;
	private Move m_lastMove;
	private Vector m_listenersWhite;
	private Vector m_listenersBlack;
	private Player m_whitePlayer;
	private Player m_blackPlayer;

	public GameState(Board board) {
		m_board = board;
		m_listenersWhite = new Vector();
		m_listenersBlack = new Vector();
		newGame();
	}

	public void newGame() {
		// hackish, but it will do for now.  i just want to make sure that players
		// arent added a whole bunch.
		if (m_whitePlayer == null) {
			m_whitePlayer = new Player(Chess.WHITE, EngineFactory.createHumanEngine());
			addMoveListener(m_whitePlayer, Chess.BLACK);
		}
		if (m_blackPlayer == null) {
			m_blackPlayer = new Player(Chess.WHITE, EngineFactory.createHumanEngine());
			addMoveListener(m_blackPlayer, Chess.WHITE);
		}
	}

	public int getTurn() {
		return m_turn;
	}

	public Move getLastMove() {
		return m_lastMove;
	}

	public boolean pieceMoved(int loc) {
		Piece piece = m_board.getSquareAt(loc).getPiece();

		// kinda weird, if there is no piece on the square then we assume
		// the piece has moved, cause it's no longer there silly...so it had to
		// move somewhere.
		return piece == null || piece.hasMoved();
	}

	public boolean areSquaresEmpty(int[] squares) {
		for (int i = 0; i < squares.length; i++) {
			if (m_board.getSquareAt(squares[i]).getPiece() != null) {
				return false;
			}
		}

		return true;
	}

	public void move(Move move) {
		move(move.getFrom(), move.getTo());
	}

	public void move(int startPos, int endPos) {
		move(m_board.getSquareAt(startPos).getPiece(), startPos, endPos);
	}

	public void move(Piece piece, int startPos, int endPos) {
		Square start = m_board.getSquareAt(startPos);
		Square end = m_board.getSquareAt(endPos);
		int valid = MoveValidator.instance().isValidMove(this, piece, start, end);

		if ((valid & MoveValidator.VALID) == MoveValidator.VALID) {
			int color = m_turn;
			piece.setMoved(true);

			// do the removal and placing of the pieces, then we will tell the
			// board to repaint with the new squares.
			start.removePiece();
			end.setPiece(piece);

			m_turn = (m_turn == Chess.WHITE) ? Chess.BLACK : Chess.WHITE;
			m_lastMove = new Move(piece, start.getPosition(), end.getPosition());
			m_board.updateSquares(m_lastMove.getFrom(), m_lastMove.getTo());
			// We have a special move to perform
			if (valid != MoveValidator.VALID) {
				doSpecialMove(valid, piece, m_lastMove.getFrom(), m_lastMove.getTo());
			}
			fireMoveEvent(color, m_lastMove);
		} else {
			start.setPiece(piece);
			m_board.updateSquares(end.getPosition(), start.getPosition());
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public void addMoveListener(MoveListener listener, int color) {
		Vector listeners = (color == Chess.WHITE) ? m_listenersWhite : m_listenersBlack;
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeMoveListener(MoveListener listener, int color) {
		Vector listeners = (color == Chess.WHITE) ? m_listenersWhite : m_listenersBlack;
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	private void fireMoveEvent(int color, Move move) {
		Vector listeners = (color == Chess.WHITE) ? m_listenersWhite : m_listenersBlack;
		MoveListener[] list = (MoveListener[]) listeners.toArray(new MoveListener[0]);
		MoveEvent event = new MoveEvent(this, color, move);
		for (int i = 0; i < list.length; i++) {
			list[i].movePerformed(event);
		}
	}

	private void doSpecialMove(int flags, Piece piece, int from, int to) {
		if ((flags & MoveValidator.PAWN_ENPASSANT) == MoveValidator.PAWN_ENPASSANT) {
			int p = (piece.getColor() == Chess.WHITE) ? -8 : 8;
			m_board.captureEnpassant(to + p);
		} else if ((flags & MoveValidator.PAWN_PROMOTE) == MoveValidator.PAWN_PROMOTE) {
			m_board.promotePawn(piece, to);
		} else if ((flags & MoveValidator.KING_CASTLE_SHORT) == MoveValidator.KING_CASTLE_SHORT) {
			Piece king = piece;
			Piece rook = m_board.getSquareAt(from + 3).getPiece();
			// Move the king
			m_board.getSquareAt(to).setPiece(king);
			m_board.getSquareAt(from).setPiece(null);
			// Move the rook
			m_board.getSquareAt(to - 1).setPiece(rook);
			m_board.getSquareAt(from + 3).setPiece(null);
			// update the king position
			m_board.updateSquares(from, to);
			// update the rook position
			m_board.updateSquares(from + 3, to - 1);
		} else if ((flags & MoveValidator.KING_CASTLE_LONG) == MoveValidator.KING_CASTLE_LONG) {
			Piece king = piece;
			Piece rook = m_board.getSquareAt(from - 4).getPiece();
			// Move the king
			m_board.getSquareAt(to).setPiece(king);
			m_board.getSquareAt(from).setPiece(null);
			// Move the rook
			m_board.getSquareAt(to + 1).setPiece(rook);
			m_board.getSquareAt(from - 4).setPiece(null);
			// update the king position
			m_board.updateSquares(from, to);
			// update the rook position
			m_board.updateSquares(from - 4, to + 1);
		}
	}
}
