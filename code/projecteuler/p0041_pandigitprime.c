/*
 * p0041_pandigitprime.c
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "euler.h"

void swap(int v[], int i, int j) {
    int t;
    t = v[i];
    v[i] = v[j];
    v[j] = t;
}

void perm(int v[], int n, int i) {
    int j;

    if (i == n) {
        int tenpow = pow(10, n - 1);
        int num = 0;
        for (j = 0; j < n; j++) {
            num += tenpow * v[j];
            tenpow /= 10;
        }
        if (isprime(num)) {
            printf("%d\n", num);
            exit(0);
        }
    } else {
        for (j = i; j < n; j++) {
            swap(v, i, j);
            perm(v, n, i + 1);
            swap(v, i, j);
        }
    }
}

/*
There is a cute little formula about divisibilty:
	A number is divisible by 3 if the sum of the digits is divisible by 3.

So, with pandigital numbers:
	sum(1:2) = 3
	sum(1:3) = 6
	sum(1:4) = 10
	sum(1:5) = 15
	sum(1:6) = 21
	sum(1:7) = 28
	sum(1:8) = 36
	sum(1:9) = 45

From this, a pandigital prime has to be 7 digits or 4 digits, we were given
the larget 4 digit pandigital prime, so we just need to check the 7 digit
numbers
*/
#define DIGITS 7
int main(int argc, char *argv[])
{
    int v[DIGITS];
    int i;

    for (i = 0; i < DIGITS; i++)
        v[i] = DIGITS - i;

    perm(v, DIGITS, 0);
    return 0;
}

/* vim: set ts=4: */
