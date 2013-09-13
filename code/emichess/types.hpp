
/*
 * types.hpp
 *
 * Developed by Drew Frezell
 *
 */

#ifndef TYPES_H
#  define TYPES_H

enum FLAGS {
    SHORT_CASTLE = 1 << 0,
    LONG_CASTLE  = 1 << 1,
    EN_PASSANT   = 1 << 2,
    CHECK        = 1 << 3,
    CAPTURE      = 1 << 4,
    PROMOTION    = 1 << 5,
};

enum PIECES {
    EMPTY    = 0x00,
    // pieces
    PAWN     = 0x01,
    ROOK     = 0x02,
    KNIGHT   = 0x03,
    BISHOP   = 0x04,
    QUEEN    = 0x05,
    KING     = 0x06,
    PIECE_MASK = 0x0f,
    // color
    WHITE    = 0x10,
    BLACK    = 0x20,
    COLOR_MASK = 0x30
};             

enum SQUARES {
    A1, B1, C1, D1, E1, F1, G1, H1,
    A2, B2, C2, D2, E2, F2, G2, H2,
    A3, B3, C3, D3, E3, F3, G3, H3,
    A4, B4, C4, D4, E4, F4, G4, H4,
    A5, B5, C5, D5, E5, F5, G5, H5,
    A6, B6, C6, D6, E6, F6, G6, H6,
    A7, B7, C7, D7, E7, F7, G7, H7,
    A8, B8, C8, D8, E8, F8, G8, H8,
    NUM_SQUARES
};

class move_t {
public:
    move_t(int b, int e, int f = 0) : beg(b), end(e), flags(f) {}
    int beg;
    int end;
    int flags;
};

#endif /* #ifndef TYPES_H */

