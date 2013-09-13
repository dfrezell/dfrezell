#include <stdio.h>

#include "euler.h"

int main(int argc, char *argv[])
{
    int a, b, c;

    for (a = 1; a < 999; a++) {
        for (b = 1; b < 999; b++) {
            for (c = 1; c < 999; c++) {
                // check if pythagorean triplet
                if (((a * a) + (b * b)) == (c * c)) {
                    if ((a + b + c) == 1000) {
                        printf("prod = %d\n", a * b * c);
                        return 0;
                    }
                }
            }
        }
    }
    return 0;
}

/* vim: set ts=4: */
