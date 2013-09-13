/*
 * p0187_primedoublets.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <math.h>

#include "euler.h"

int main(int argc, char *argv[])
{
    int i;
    int pn[PRIME_TBL_LEN];
    int pk;

    for (i = 0; i < PRIME_TBL_LEN; i++) {
        pk = pow(10, 8) / prime_table[i];
        if ((pk & 0x01) == 0) {
            pk -= 1;
        }

        for ( ; pk > 0; pk -= 2) {
            if (isprime(pk)) {
                pn[i] = pk;
                break;
            }
        }
    }

    for (i = 0; i < PRIME_TBL_LEN; i++) {
        printf("%d * %d = %d\n", prime_table[i], pn[i], prime_table[i] * pn[i]);
    }

    return 0;
}

/* vim: set ts=4: */

