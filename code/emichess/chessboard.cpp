/*
 * chessboard.cpp
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <string.h>

#include "chessboard.hpp"
#include "movegen.hpp"

#define P2C(x) (((x & PIECE_MASK) == PAWN) ? 'P' + (x & BLACK) : \
                ((x & PIECE_MASK) == ROOK) ? 'R' + (x & BLACK) : \
                ((x & PIECE_MASK) == KNIGHT) ? 'N' + (x & BLACK) : \
                ((x & PIECE_MASK) == BISHOP) ? 'B' + (x & BLACK) : \
                ((x & PIECE_MASK) == QUEEN) ? 'Q' + (x & BLACK) : \
                ((x & PIECE_MASK) == KING) ? 'K' + (x & BLACK) : ' ')


chessboard::chessboard() {
    memset(_board, EMPTY, NUM_SQUARES);

    _board[A1] = _board[H1] = ROOK | WHITE;
    _board[B1] = _board[G1] = KNIGHT | WHITE;
    _board[C1] = _board[F1] = BISHOP | WHITE;
    _board[D1] = QUEEN | WHITE;
    _board[E1] = KING | WHITE;
    memset(&_board[A2], PAWN | WHITE, A3 - A2);
    _white_flags = SHORT_CASTLE | LONG_CASTLE;

    _board[A8] = _board[H8] = ROOK | BLACK;
    _board[B8] = _board[G8] = KNIGHT | BLACK;
    _board[C8] = _board[F8] = BISHOP | BLACK;
    _board[D8] = QUEEN | BLACK;
    _board[E8] = KING | BLACK;
    memset(&_board[A7], PAWN | BLACK, A8 - A7);
    _black_flags = SHORT_CASTLE | LONG_CASTLE;

    _move = WHITE;
}

chessboard::~chessboard() {
}


void print_row_border() {
    printf("   +---+---+---+---+---+---+---+---+\n");
}

void print_col_marks() {
    printf("     a   b   c   d   e   f   g   h \n");
}

void print_row(char row_letter, int start, const char *board) {
    printf(" %c | %c | %c | %c | %c | %c | %c | %c | %c |\n", row_letter,
            P2C(board[start+0]), P2C(board[start+1]), P2C(board[start+2]), P2C(board[start+3]),
            P2C(board[start+4]), P2C(board[start+5]), P2C(board[start+6]), P2C(board[start+7]));
}

//
//   +---+---+---+---+---+---+---+---+
// h | r | n | b | q | k | b | n | r |
//   +---+---+---+---+---+---+---+---+
// g | p | p | p | p | p | p | p | p |
//   +---+---+---+---+---+---+---+---+
// f |   |   |   |   |   |   |   |   |
//   +---+---+---+---+---+---+---+---+
// e |   |   |   |   |   |   |   |   |
//   +---+---+---+---+---+---+---+---+
// d |   |   |   |   |   |   |   |   |
//   +---+---+---+---+---+---+---+---+
// c |   |   |   |   |   |   |   |   |
//   +---+---+---+---+---+---+---+---+
// b | P | P | P | P | P | P | P | P |
//   +---+---+---+---+---+---+---+---+
// a | R | N | B | Q | K | B | N | R |
//   +---+---+---+---+---+---+---+---+
//     1   2   3   4   5   6   7   8
//
void chessboard::print() {
    print_row_border();
    print_row('8', A8, _board);
    print_row_border();
    print_row('7', A7, _board);
    print_row_border();
    print_row('6', A6, _board);
    print_row_border();
    print_row('5', A5, _board);
    print_row_border();
    print_row('4', A4, _board);
    print_row_border();
    print_row('3', A3, _board);
    print_row_border();
    print_row('2', A2, _board);
    print_row_border();
    print_row('1', A1, _board);
    print_row_border();
    print_col_marks();
}


void chessboard::move(int start, int end) {
    // check if king castle move
    if ((_board[start] & PIECE_MASK) == KING) {
        if (end - start == 2) {
            // king side castle
            _board[start + 1] = _board[start + 3];
            _board[start + 3] = EMPTY;
        } else if (end - start == -2) {
            // queen side castle
            _board[start - 1] = _board[start - 4];
            _board[start - 4] = EMPTY;
        }
    }

    _board[end] = _board[start];
    _board[start] = EMPTY;
}

void chessboard::successors(std::list<chessboard> children) {
    std::list<move_t> moves;
    for (int i = A1; i <= H8; i++) {
        moves.clear();
        if ((_board[i] & COLOR_MASK) == _move) {
            valid_moves(i, *this, moves);
        }
    }
}
