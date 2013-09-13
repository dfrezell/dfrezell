
/*
 * chessboard.hpp
 *
 * Developed by Drew Frezell
 *
 */

#ifndef CHESSBOARD_HPP
#  define CHESSBOARD_HPP

#include <list>
#include "types.hpp"

class chessboard {
public:
    chessboard();
    ~chessboard();

    inline int piece_at(int pos) const { return _board[pos]; }
    inline int move() const { return _move; }
    inline int rank(int pos) const { return pos / 8; }
    inline int col(int pos) const { return pos % 8; }

    void print();

    void move(int start, int end);
    void successors(std::list<chessboard> children);
private:
    int  _move;
    int  _white_flags;
    int  _black_flags;
    char _enpassant[2];
    char _board[NUM_SQUARES];
};

#endif /* #ifndef CHESSBOARD_HPP */


