
/*
 * sudoku_cell.hpp
 *
 * Developed by Andrew Frezell <dfrezell@gmail.com>
 * Copyright (c) 2012 Andrew Frezell
 * All rights reserved.
 *
 */

#ifndef SUDOKU_CELL_HPP
#  define SUDOKU_CELL_HPP

typedef struct sudoku_cell {
    sudoku_cell() : val(0), possible(0x03fe) {}
    bool valid() { return possible != 0; }
    void constrain(int n) { 
        possible &= ~(1 << n);
    }
    void set(int n) {
        val = n;
        possible = 1 << n;
    }
    int val;
    int possible;
} sudoku_cell;

typedef struct sudoku_dll {
    sudoku_dll() : cell(), rnext(NULL), rprev(NULL), cnext(NULL), cprev(NULL),
            qnext(NULL), qprev(NULL) {}

    sudoku_cell cell;
    sudoku_dll* rnext;
    sudoku_dll* rprev;
    sudoku_dll* cnext;
    sudoku_dll* cprev;
    sudoku_dll* qnext;
    sudoku_dll* qprev;
} sudoku_dll;


#endif /* #ifndef SUDOKU_CELL_HPP */


