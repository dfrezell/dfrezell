/*
 * sudoku.cpp
 *
 * Developed by Andrew Frezell <dfrezell@gmail.com>
 * Copyright (c) 2012 Andrew Frezell
 * All rights reserved.
 *
 */

#include "sudoku_board.hpp"

#include <list>
#include <queue>
#include <set>

struct sudoku_board_less
{
    bool operator() (const sudoku_board* lhs, const sudoku_board* rhs) const
    {
        return *lhs < *rhs;
    }
};


char m[] = {
    9, 2, 1, 0, 0, 0, 8, 0, 0,
    8, 0, 0, 0, 1, 0, 0, 4, 0,
    0, 0, 6, 0, 9, 3, 7, 0, 1,
    0, 0, 0, 4, 8, 1, 0, 0, 6,
    0, 0, 5, 9, 0, 2, 4, 0, 0,
    4, 0, 0, 6, 3, 5, 0, 0, 0,
    6, 0, 7, 1, 2, 0, 9, 0, 0,
    0, 5, 0, 0, 6, 0, 0, 0, 4,
    0, 0, 4, 0, 0, 0, 6, 8, 2,
};

char hard[] = {
    8, 5, 0, 0, 0, 2, 4, 0, 0,
    7, 2, 0, 0, 0, 0, 0, 0, 9,
    0, 0, 4, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 1, 0, 7, 0, 0, 2,
    3, 0, 5, 0, 0, 0, 9, 0, 0,
    0, 4, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 8, 0, 0, 7, 0,
    0, 1, 7, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 3, 6, 0, 4, 0,
};

char hard2[] = {
    0, 0, 0, 0, 0, 6, 0, 0, 0,
    0, 5, 9, 0, 0, 0, 0, 0, 8,
    2, 0, 0, 0, 0, 8, 0, 0, 0,
    0, 4, 5, 0, 0, 0, 0, 0, 0,
    0, 0, 3, 0, 0, 0, 0, 0, 0,
    0, 0, 6, 0, 0, 3, 0, 5, 4,
    0, 0, 0, 3, 2, 5, 0, 0, 6,
    0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 
};

int main(int argc, char *argv[])
{
    sudoku_board b(hard2);

    if (!b.is_valid())
    {
        b.print();
        return -1;
    }

    std::list<sudoku_board *> s;
    std::list<sudoku_board *>::iterator child;

    std::priority_queue<sudoku_board *, std::vector<sudoku_board *>, sudoku_board_less> frontier;
    std::set<sudoku_board *, sudoku_board_less> visited;
    std::set<sudoku_board *, sudoku_board_less>::iterator vit;

    frontier.push(&b);

    sudoku_board *curr;
    while (!frontier.empty())
    {
        curr = frontier.top();
        frontier.pop();
        if (curr->is_solved())
            break;
        visited.insert(curr);
        curr->successors(s);
        for (child = s.begin(); child != s.end(); child++)
        {
            vit = visited.find(*child);
            if (vit == visited.end())
            {
                frontier.push(*child);
            }
            else
            {
                delete *vit;
            }
        }
        s.clear();
        //printf("f.size : %ld, v.size : %ld\n", frontier.size(), visited.size());
        if (frontier.size() % 100000 == 99999)
        {
            curr->print();
            printf("frontier.size() = %ld\n", frontier.size());
        }
    }

    printf("f.size : %ld, v.size : %ld\n", frontier.size(), visited.size());
    curr->print();

    return 0;
}

// vim: set ts=4:

