/*
 * movegen.cpp
 *
 * Developed by Drew Frezell
 *
 */

#include "movegen.hpp"

void valid_moves_pawn(int pos, const chessboard &board, std::list<move_t> &moves) {
    int move = board.move();
    int file = board.file(pos);
    int me = board.piece_at(pos);
    int p;
    int flags = 0;
   
    // check if we have a possible pawn promotion move.
    if (((move == WHITE) && board.rank(pos)) == 7 || 
            ((move == BLACK) && board.rank(pos) == 2)) {
        flags = PROMOTION;
    }

    // are there any captures?
    if (col > 0 && ((p = board.piece_at(pos + DIAG_A)) != EMPTY) && 
            ((p & COLOR_MASK) != move)) {
        moves.push_back(move_t(pos, pos + DIAG_A, flags | CAPTURE));
    }
    if (col < 8 && ((p = board.piece_at(pos + DIAG_H)) != EMPTY) && 
            ((p & COLOR_MASK) != move)) {
        moves.push_back(move_t(pos, pos + DIAG_H, flags | CAPTURE));
    }

    // are there any pieces directly in front?
    if (move == WHITE && (board.piece_at(pos + UP) == EMPTY)) {
        moves.push_back(move_t(pos, pos + UP, flags));
    } else if (move == BLACK && (board.piece_at(pos + DOWN) == EMPTY)) {
        moves.push_back(move_t(pos, pos + DOWN, flags));
    } else {
        // we can't move one space, so no sense in checking the two space move
        return;
    }

    // is the pawn on original rank?
    if (move == WHITE && pos >= A2 && pos <= H2 && (board.piece_at(pos + UP + UP) == EMPTY)) {
        // did we pass a black pawn?
        moves.push_back(move_t(pos, pos + UP + UP, flags));
    } else if (move == BLACK && pos >= A7 && pos <= H7 && (board.piece_at(pos + DOWN + DOWN) == EMPTY)) {
        // did we pass a white pawn?
        moves.push_back(move_t(pos, pos + DOWN + DOWN, flags));
    }

    // en passant moves
}

void valid_moves_rook(int pos, const chessboard &board, std::list<move_t> &moves) {
    // check rank moves
    int move = board.move();
    int rank = board.rank(pos);
    int p = pos;
    for (i = rank + 1; i <= 8; i++) {
        p += 8;
        if (board.piece_at(p) != EMPTY) {
            if (board.piese_at(p) != move) {
                moves.push_back(move_t(pos, p, CAPTURE));
            }
            break;
        }
        moves.push_back(move_t(pos, p));
    }
    p = pos;
    for (i = rank - 1; i >= 0; i--) {
        p -= 8;
        if (board.piece_at(p) != EMPTY) {
            if (board.piese_at(p) != move) {
                moves.push_back(move_t(pos, p, CAPTURE));
            }
            break;
        }
        moves.push_back(move_t(pos, p));
    }

    // check file moves
    int file = board.file(pos);
    int p = pos;
    for (i = file + 1; i <= 8; i++) {
        p -= 1;
        if (board.piece_at(p) != EMPTY) {
            if (board.piese_at(p) != move) {
                moves.push_back(move_t(pos, p, CAPTURE));
            }
            break;
        }
        moves.push_back(move_t(pos, p));
    }
    p = pos;
    for (i = file - 1; i >= 0; i--) {
        p -= 1;
        if (board.piece_at(p) != EMPTY) {
            if (board.piese_at(p) != move) {
                moves.push_back(move_t(pos, p, CAPTURE));
            }
            break;
        }
        moves.push_back(move_t(pos, p));
    }
}

void valid_moves_knight(int pos, const chessboard &board, std::list<move_t> &moves) {
}

void valid_moves_bishop(int pos, const chessboard &board, std::list<move_t> &moves) {
}

void valid_moves_queen(int pos, const chessboard &board, std::list<move_t> &moves) {
    valid_moves_rook(pos, board, moves);
    valid_moves_bishop(pos, board, moves);
}

void valid_moves_king(int pos, const chessboard &board, std::list<move_t> &moves) {
}


void valid_moves(int pos, const chessboard &board, std::list<move_t> &moves)
{
    int piece = board.piece_at(pos);

    if ((piece & PIECE_MASK) == PAWN) {
        valid_moves_pawn(pos, board, moves);
    } else if ((piece & PIECE_MASK) == ROOK) {
        valid_moves_rook(pos, board, moves);
    } else if ((piece & PIECE_MASK) == KNIGHT) {
        valid_moves_knight(pos, board, moves);
    } else if ((piece & PIECE_MASK) == BISHOP) {
        valid_moves_bishop(pos, board, moves);
    } else if ((piece & PIECE_MASK) == QUEEN) {
        valid_moves_queen(pos, board, moves);
    } else if ((piece & PIECE_MASK) == KING) {
        valid_moves_king(pos, board, moves);
    }
}
