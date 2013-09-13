/*
 * sudoku.cpp
 *
 * Developed by Andrew Frezell <dfrezell@gmail.com>
 * Copyright (c) 2012 Andrew Frezell
 * All rights reserved.
 *
 */

#include <assert.h>
#include <string.h>
#include <stdio.h>

#include "sudoku_board.hpp"
#include "hash.hpp"

sudoku_board::sudoku_board()
{
    //memset(b, 0, sizeof(b));
    init_board(b);
    update();
}

sudoku_board::sudoku_board(const char *a)
{
    //memcpy(b, a, sizeof(b));
    update();
}

sudoku_board::~sudoku_board()
{
}

sudoku_board::sudoku_board(const sudoku_board& p)
{
    memcpy(b, p.b, sizeof(b));
    solved = p.solved;
    valid = p.valid;
    dist = p.dist;
    crc = p.crc;
}

void sudoku_board::set_cell(int x, int y, char val)
{
    assert(x >= 0 && x < BOARD_SIZE);
    assert(y >= 0 && y < BOARD_SIZE);
    b[x + y * BOARD_SIZE] = val;
    update();
}

char sudoku_board::get_cell(int x, int y)
{
    assert(x >= 0 && x < BOARD_SIZE);
    assert(y >= 0 && y < BOARD_SIZE);
    return b[x + y * BOARD_SIZE];
}

void sudoku_board::print()
{
    for (int i = 0; i < BOARD_DIM; i++)
    {
        printf("%d%c", b[i], (i % BOARD_SIZE) == (BOARD_SIZE - 1) ? '\n' : ' ');
    }
    printf("d = %u, v = %d, s = %d, crc = %8.8x\n", dist, valid, solved, crc);
}

char quad_lookup[] = {
    0, 0, 0, 1, 1, 1, 2, 2, 2,
    0, 0, 0, 1, 1, 1, 2, 2, 2,
    0, 0, 0, 1, 1, 1, 2, 2, 2,
    3, 3, 3, 4, 4, 4, 5, 5, 5,
    3, 3, 3, 4, 4, 4, 5, 5, 5,
    3, 3, 3, 4, 4, 4, 5, 5, 5,
    6, 6, 6, 7, 7, 7, 8, 8, 8,
    6, 6, 6, 7, 7, 7, 8, 8, 8,
    6, 6, 6, 7, 7, 7, 8, 8, 8,
};
void sudoku_board::update()
{
    uint16_t row[BOARD_SIZE] = { 0 };
    uint16_t col[BOARD_SIZE] = { 0 };
    uint16_t qad[BOARD_SIZE] = { 0 };
    memset(row, 0, sizeof(qad));
    int x, y, q;
    int bit;

    solved = true;
    valid = true;
    dist = 0;
    crc = hash(b, BOARD_DIM);

    // check if solved or valid.
    for (int i = 0; i < BOARD_DIM; i++)
    {
        x = i % BOARD_SIZE;
        y = i / BOARD_SIZE;
        q = quad_lookup[i];
        bit = 1 << b[i];
        if (bit == 1)
        {
            solved = false;
            dist++;
            continue;
        }
        if (((row[y] & bit) == bit) || ((col[x] & bit) == bit) || 
                (qad[q] & bit) == bit)
        {
            solved = valid = false;
            break;
        }
        row[y] |= bit;
        col[x] |= bit;
        qad[q] |= bit;
    }
}


void sudoku_board::successors(std::list<sudoku_board *> &s)
{
    if (solved)
        return;

    int i;
    // find the first free cell
    for (i = 0; i < BOARD_DIM; i++)
    {
        if (b[i] == 0)
            break;
    }

    sudoku_board tmp(*this);
    // create a new object 
    for (int v = 1; v <= BOARD_SIZE; v++)
    {
        tmp.b[i] = v;
        tmp.update();
        if (tmp.is_valid())
        {
            sudoku_board *child = new sudoku_board(tmp);
            s.push_back(child);
        }
    }
}


int quad_next_lut[] = {
     1,  2,  9,   4,  5, 12,   7,  8, 15,
    10, 11, 18,  13, 14, 21,  16, 17, 24,
    19, 20,  0,  22, 23,  3,  25, 26,  6,
                              
    28, 29, 36,  31, 32, 39,  34, 35, 42,
    37, 38, 45,  40, 41, 48,  43, 44, 51,
    46, 47, 27,  49, 50, 30,  52, 53, 33,

    55, 56, 63,  58, 59, 66,  61, 62, 69,
    64, 65, 72,  67, 68, 75,  70, 71, 78,
    73, 74, 54,  76, 77, 57,  79, 80, 60,
};

int quad_prev_lut[] = {
    20,  0,  1,  23,  3,  4,  26,  6,  7,
     2,  9, 10,   5, 12, 13,   8, 15, 16,
    11, 18, 19,  14, 21, 22,  17, 24, 25,
      
    47, 27, 28,  50, 30, 31,  53, 33, 34,
    29, 36, 37,  32, 39, 40,  35, 42, 43,
    38, 45, 46,  41, 48, 49,  44, 51, 52,
      
    74, 54, 55,  77, 57, 58,  80, 60, 61,
    56, 63, 64,  59, 66, 67,  62, 69, 70,
    65, 72, 73,  68, 75, 76,  71, 78, 79,
};

void sudoku_board::init_board(char *v)
{
    int i;

    for (int y = 0; y < BOARD_SIZE; y++)
    {
        for (int x = 0; x < BOARD_SIZE; x++)
        {
            i = x + y * BOARD_SIZE;
            b[i].rnext = &b[(y * BOARD_SIZE) + ((x + 1) % BOARD_SIZE)];
            b[i].rprev = &b[(y * BOARD_SIZE) + ((x - 1) % BOARD_SIZE)];

            b[i].cnext = &b[(i + BOARD_SIZE) % BOARD_DIM];
            b[i].cprev = &b[(i - BOARD_SIZE) % BOARD_DIM];

            b[i].qnext = &b[quad_next_lut[i]];
            b[i].qprev = &b[quad_prev_lut[i]];

            if (v)
                b[i].cell.set(v);
        }
    }
}


void sudoku_board::link_board()
{
}

// vim: set ts=4:

