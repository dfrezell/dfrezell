
/*
 * movegen.hpp
 *
 * Developed by Drew Frezell
 *
 */

#ifndef MOVEGEN_H
#  define MOVEGEN_H

#include <list>
#include "chessboard.hpp"
#include "types.hpp"

#define DIAG_A 7
#define DIAG_H 9
#define LEFT -1
#define RIGHT 1
#define UP 8
#define DOWN -8

void valid_moves(int pos, const chessboard &board, std::list<move_t> &moves);

#endif /* #ifndef MOVEGEN_H */


