
/*
 * sudoku.hpp
 *
 * Developed by Andrew Frezell <dfrezell@gmail.com>
 * Copyright (c) 2012 Andrew Frezell
 * All rights reserved.
 *
 */

#ifndef SUDOKU_HPP
#  define SUDOKU_HPP

#include <stdint.h>
#include <list>

#include "sudoku_cell.hpp"

#define BOARD_SIZE 9
#define BOARD_DIM  (BOARD_SIZE * BOARD_SIZE)

/**
 * board layout:
 *      x0   x1   x2   x3   x4   x5   x6   x7   x8
 *    +----+----+----++----+----+----++----+----+----+
 * y0 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y1 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y2 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 *    +----+----+----++----+----+----++----+----+----+
 * y3 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y4 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y5 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 *    +----+----+----++----+----+----++----+----+----+
 * y6 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y7 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 * y8 |    |    |    ||    |    |    ||    |    |    |
 *    |    |    |    ||    |    |    ||    |    |    |
 *    +----+----+----++----+----+----++----+----+----+
 */
class sudoku_board
{
public:
    sudoku_board();
    sudoku_board(const char *a);
    ~sudoku_board();

    bool is_solved() { return solved; }
    bool is_valid() { return valid; }
    uint32_t get_dist() { return dist; }
    uint32_t get_crc() { return crc; }

    char get_cell(int x, int y);
    void set_cell(int x, int y, char val);
    void print();

    void successors(std::list<sudoku_board *> &s);

    bool operator==(const sudoku_board& o) const {
        return dist == o.dist && crc == o.crc;
    }
    bool operator!=(const sudoku_board& o) const {
        return dist != o.dist || crc != o.crc;
    }
    bool operator>(const sudoku_board& o) const {
        return dist > o.dist || (dist == o.dist && crc > o.crc);
    }
    bool operator>=(const sudoku_board& o) const {
        return dist >= o.dist;
    }
    bool operator<(const sudoku_board& o) const {
        return dist < o.dist || (dist == o.dist && crc < o.crc);
    }
    bool operator<=(const sudoku_board& o) const {
        return dist <= o.dist;
    }
private:
    sudoku_board(const sudoku_board& p);
    sudoku_board& operator=(const sudoku_board &src);

    void update();
    void init_board();
    void link_board();

    //char b[BOARD_SIZE * BOARD_SIZE];
    sudoku_dll b[BOARD_DIM];
    bool solved;
    bool valid;
    uint32_t dist;
    uint32_t crc;
};

#endif /* #ifndef SUDOKU_HPP */


