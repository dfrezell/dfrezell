/*
 * hexdump.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
/*
         1         2         3         4         5         6         7         8
12345678901234567890123456789012345678901234567890123456789012345678901234567890
00000000  23 21 2f 62 69 6e 2f 62  61 73 68 0a 0a 43 4d 44  |#!/bin/bash..CMD|
00000010  5f 50 41 54 48 3d 22 24  28 64 69 72 6e 61 6d 65  |_PATH="$(dirname|
00000020  20 24 28 72 65 61 64 6c  69 6e 6b 20 2d 66 20 24  | $(readlink -f $|
00000030  30 29 29 22 0a 43 4d 44  5f 4e 41 4d 45 3d 22 2e  |0))".CMD_NAME=".|
00000040  2e 2f 73 72 63 2f 73 69  6d 75 6c 61 74 69 6f 6e  |./src/simulation|
00000050  2f 73 63 2f 73 69 6d 63  74 72 6c 2e 70 79 22 0a  |/sc/simctrl.py".|
00000060  43 4d 44 3d 22 24 43 4d  44 5f 50 41 54 48 2f 24  |CMD="$CMD_PATH/$|
00000070  43 4d 44 5f 4e 41 4d 45  22 0a 65 78 70 6f 72 74  |CMD_NAME".export|
00000080  20 50 59 54 48 4f 4e 44  4f 4e 54 57 52 49 54 45  | PYTHONDONTWRITE|
00000090  42 59 54 45 43 4f 44 45  3d 31 0a 65 78 65 63 20  |BYTECODE=1.exec |
000000a0  24 43 4d 44 20 24 40 0a                           |$CMD $@.|
000000a8

*/

#define XX(x) ((((x) >= 0x20) && ((x) <= 0x7e)) ? (x) : '.')
#define HEXSET_LEN 49
#define ASCIISET_LEN 17
static void hexdump(unsigned char *data, int len)
{
    int i;
    unsigned char *p = data;
    char hexset[HEXSET_LEN];
    char asciiset[ASCIISET_LEN];

    memset(hexset, 0, HEXSET_LEN);
    memset(asciiset, 0, ASCIISET_LEN);
    for (i = 0; i < len; i += 16)
    {
        if ((i + 16) <= len)
        {
            sprintf(hexset, "%2.2x %2.2x %2.2x %2.2x %2.2x %2.2x %2.2x %2.2x  "
                            "%2.2x %2.2x %2.2x %2.2x %2.2x %2.2x %2.2x %2.2x",
                            p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], 
                            p[8], p[9], p[10], p[11], p[12], p[13], p[14], p[15]);
            sprintf(asciiset, "%c%c%c%c%c%c%c%c%c%c%c%c%c%c%c%c",
                    XX(p[0]), XX(p[1]), XX(p[2]), XX(p[3]), XX(p[4]), XX(p[5]), XX(p[6]), XX(p[7]),
                    XX(p[8]), XX(p[9]), XX(p[10]), XX(p[11]), XX(p[12]), XX(p[13]), XX(p[14]), XX(p[15]));
        }
        else
        {
            int j = 0;
            int tlen = len - i;
            char *h = hexset;
            char *a = asciiset;
            for (j = 0; j < tlen; j++)
            {
                h += sprintf(h, "%2.2x ", p[j]);
                if (j == 7)
                    h += sprintf(h, " ");
                a += sprintf(a, "%c", XX(p[j]));
            }
            if (j < 7)
                h += sprintf(h, " ");
            for (j = 0; j < (3 * (16 - (len - i))) - 1; j++)
                h += sprintf(h, " ");
        }
        printf("%8.8x  %s  |%s|\n", i, hexset, asciiset);
        p += 16;
    }
    printf("%8.8x\n", len);
}

unsigned char buf[BUFSIZ];
int main(int argc, char *argv[])
{
    FILE *f = fopen(argv[1], "r");
    int r = 0;

    while ((r = fread(buf, sizeof(char), BUFSIZ, f)) != 0)
    {
        hexdump(buf, r);
    }

    return 0;
}

