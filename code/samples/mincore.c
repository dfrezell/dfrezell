/*
 * mincore.c
 *
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/mman.h>

int bizbux;
int barfoo = 5;

static int test1(int c) {
    return c;
}

int main(int argc, char *argv[])
{
    int foobar;
    int i = 3;
    void *p[] = {
        (void *)0x01,
        main,
        &foobar,
        &barfoo,
        test1,
        &bizbux,
        (void *)0x39929321,
        (void *)0x08925100,
        0x0,
        &i,
        strncpy
    };
    int pg = sysconf(_SC_PAGESIZE);
    unsigned char vec = 0;
    int mask = ~(pg - 1);

    int len = 10000;
    printf("vec size = %d\n", (len + (pg - 1)) / pg);

    for (i = 0; i < sizeof(p)/sizeof(p[0]); i++) {
        int addr = (int)p[i];
        mincore((void *)(addr & mask), 1, &vec);
        printf("[0x%8.8x] memory %s accessible\n", addr, (vec & 0x01) ? "is" : "is not");
        printf("\toffset = %d\n", addr & (pg - 1));
        vec = 0;
    }
    return 0;
}

/* vim: set ts=4: */

