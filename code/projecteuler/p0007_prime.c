#include <stdio.h>
#include <stdint.h>

#include "euler.h"

int main(int argc, char *argv[])
{
    int pcount = 1;
    uint64_t num = 1;

    while (pcount < 10001) {
        num += 2;
        if (isprime(num)) {
            pcount++;
        }
    }

    printf("10001st prime is %lld\n", num);

    return 0;
}

/* vim: set ts=4: */
