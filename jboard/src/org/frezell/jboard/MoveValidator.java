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

import org.frezell.util.*;

import java.text.*;
import java.util.logging.*;

public class MoveValidator implements Singleton {
	public static final int INVALID = 0;
	public static final int VALID = 1;
	public static final int PAWN_ENPASSANT = 2;
	public static final int PAWN_PROMOTE = 4;
	public static final int KING_CASTLE_SHORT = 8;
	public static final int KING_CASTLE_LONG = 16;
	public static final int CAPTURE = 32;
	public static final int CHECK = 64;
	public static final int CHECKMATE = 128;

	/** The increment value for moving along a rank */
	public static final int RANK_INC = 1;
	/** The increment value for moving along a file */
	public static final int FILE_INC = 8;
	/** The increment value for moving along an upper-left to lower-right
	 * diagonal (\)*/
	public static final int DIAG_LR_INC = 7;
	/** The increment value for moving along an upper-right to lower-left
	 * diagonal (/)*/
	public static final int DIAG_RL_INC = 9;

	private static final int[] KNIGHT_MOVES = {
		-17, -15, -10, -6, 6, 10, 15, 17
	};

	private static final int[] KING_MOVES = {
		-9, -8, -7, -1, 1, 7, 8, 9
	};

	private static MoveValidator g_self = new MoveValidator();

	private MoveValidator() {
	}

	public static MoveValidator instance() {
		return g_self;
	}

	public int isValidMove(GameState state, Piece piece, Square start, Square end) {
		int canMove = INVALID;
		int sPos = start.getPosition();
		int ePos = end.getPosition();

		if (piece.getColor() != state.getTurn()) {
			return INVALID;
		}
		if (end.getPiece() != null) {
			if (piece.getColor() == end.getPiece().getColor()) {
				return INVALID;
			} else {
				canMove |= CAPTURE;
			}
		}

		switch (piece.getType()) {
			case Chess.PAWN:
				canMove |= validatePawn(state, piece, sPos, end);
				break;
			case Chess.KNIGHT:
				canMove |= validateKnight(sPos, ePos);
				break;
			case Chess.BISHOP:
				canMove |= validateBishop(state, sPos, ePos);
				break;
			case Chess.ROOK:
				canMove |= validateRook(state, sPos, ePos);
				break;
			case Chess.QUEEN:
				canMove |= validateQueen(state, sPos, ePos);
				break;
			case Chess.KING:
				canMove |= validateKing(state, piece, sPos, ePos);
				break;
		}

		if (willBeInCheck(state, piece, end.getPosition())) {
			return INVALID;
		}

		return canMove;
	}

	private int validatePawn(GameState state, Piece piece, int start, Square endSq) {
		int dir = (piece.getColor() == Chess.WHITE) ? 8 : -8;
		int canMove = INVALID;
		int end = endSq.getPosition();

		if (endSq.getPiece() == null) {
			if ((start + dir) == end) {
				canMove |= VALID;
			} else if (!piece.hasMoved() &&
					(start + (dir * 2)) == end) {
				canMove |= VALID;
			} else if (isEnpassant(state, piece, start)) {
				canMove |= VALID | PAWN_ENPASSANT | CAPTURE;
			}
		} else {
			// Check to see if it is a capture.
			int traj = start + dir;
			if ((traj + 1) == end || (traj - 1) == end) {
				canMove |= VALID;
			}
		}

		if (((canMove & VALID) == VALID) && isQueening(end)) {
			canMove |= PAWN_PROMOTE;
		}

		return canMove;
	}

	private int validateKnight(int start, int end) {
		int allow = INVALID;

		for (int i = 0; i < KNIGHT_MOVES.length; i++) {
			if (start + KNIGHT_MOVES[i] == end) {
				allow |= VALID;
				break;
			}
		}

		return allow;
	}

	private int validateBishop(GameState state, int start, int end) {
		int allow = INVALID;
		int dir = Math.abs(end - start);
		boolean rank = (start / 8) == (end / 8);
		boolean diag = dir % DIAG_LR_INC == 0 || dir % DIAG_RL_INC == 0;

		// make sure it is a diagonal move, the way the math works a move from
		// the far end of a rank to the other end looks like a diagonal move
		// also, so if it is on the same rank, it's not a diagonal move.
		if (diag && !rank) {
			if (isPathClear(state, start, end)) {
				allow |= VALID;
			}
		}
		return allow;
	}

	private int validateRook(GameState state, int start, int end) {
		int allow = INVALID;
		boolean rank = (start / 8) == (end / 8);
		boolean file = (start % 8) == (end % 8);

		if (rank || file) {
			if (isPathClear(state, start, end)) {
				allow |= VALID;
			}
		}
		return allow;
	}

	private int validateQueen(GameState state, int start, int end) {
		int allow = INVALID;

		if (isPathClear(state, start, end)) {
			allow |= VALID;
		}
		return allow;
	}

	private int validateKing(GameState state, Piece piece, int start, int end) {
		int allow = INVALID;

		// Check to see if this is a castling move, this is usually the first
		// King move in a game.
		if (Math.abs(start - end) != 2) {
			for (int i = 0; i < KING_MOVES.length; i++) {
				if (start + KING_MOVES[i] == end) {
					allow = VALID;
				}
			}
		} else if (canCastle(state, piece, start, end)) {
			allow = VALID;
			allow |= (end - start == 2) ? KING_CASTLE_SHORT : KING_CASTLE_LONG;
		}

		return allow;
	}

	private boolean isEnpassant(GameState state, Piece piece, int start) {
		boolean allow = false;
		// Check to make sure the pawn is on the right rank.
		if (((piece.getColor() == Chess.WHITE) && (start >= Chess.A5 && start <= Chess.H5)) ||
				((piece.getColor() == Chess.BLACK) && (start >= Chess.A4 && start <= Chess.H4))) {
			Move lastMove = state.getLastMove();
			if (lastMove.getPiece().getType() == Chess.PAWN) {
				// Check if a pawn moved two spaces
				if (Math.abs(lastMove.getFrom() - lastMove.getTo()) == 16) {
					// Is the pawn next to our pawn.
					if (Math.abs(state.getLastMove().getTo() - start) == 1) {
						allow = true;
					}
				}
			}
		}
		return allow;
	}

	private boolean isQueening(int end) {
		// Check to see if the pawn is moving to the first or last rank
		return ((end >= Chess.A8 && end <= Chess.H8) || (end >= Chess.A1 && end <= Chess.H1));
	}

	/**
	 * Determine if the king is allowed to castle.  There are several
	 * requirements that need to be met before a king can castle.
	 * <ul>
	 * <li>The king has not moved.</li>
	 * <li>The rook to the side the king is castling has not moved.</li>
	 * <li>The path between the king and rook is not blocked by any pieces (i.e.
	 * 	The king or rook cannot "jump" any pieces during a castling)</li>
	 * <li>The king is not in check.</li>
	 * <li>The king cannot pass over any squares during the castling that
	 * 	would put him in check.</li>
	 * <li>After the castle is complete, the king cannot be in check.</li>
	 * </ul>
	 * The last case is determined in <code>isValidMove</code> since
	 * we see if the king is in check after every move is performed.
	 * @param state
	 * @param piece
	 * @param start
	 * @param end
	 * @return If the king can castle or not.
	 */
	private boolean canCastle(GameState state, Piece piece, int start, int end) {
		int dir = end - start;
		int rookPos = start + ((dir == 2) ? 3 : -4);

		if (!piece.hasMoved() && !state.pieceMoved(rookPos)) {
			if (isPathClear(state, start, rookPos)) {
				if (!willBeInCheck(state, piece, (start + end) / 2)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean willBeInCheck(GameState state, Piece piece, int square) {
		if (piece.getType() == Chess.KING) {
			// TODO: Check if any pieces are attacking the sqare the king is
			// trying to move to
		} else {
			// TODO: need to find the king and see who can attack him now a
			// piece has moved.
		}
		return false;
	}

	private boolean isPathClear(GameState state, int start, int end) {
		int dir = Math.abs(end - start);
		boolean rank = (start / 8) == (end / 8);
		boolean file = (start % 8) == (end % 8);
		boolean diagLR = dir % DIAG_LR_INC == 0;
		boolean diagRL = dir % DIAG_RL_INC == 0;
		int[] between = null;
		int inc = rank ? RANK_INC : file ? FILE_INC : diagLR ? DIAG_LR_INC : diagRL ? DIAG_RL_INC : 0;

		if (!rank && !file && !diagLR && !diagRL) {
			Logger.getAnonymousLogger().warning("Impossible move path");
			return false;
		}

		if (rank) {
			between = new int[Math.abs(end - start) - 1];
		} else if (file) {
			between = new int[Math.abs(end / 8 - start / 8) - 1];
		} else {
			between = new int[Math.abs(end / inc - start / inc) - 1];
		}

		int min = Math.min(start, end);
		for (int i = 0; i < between.length; i++) {
			min += inc;
			between[i] = min;
		}

		return state.areSquaresEmpty(between);
	}
}
