#include <stdio.h>
#include <string.h>

#include "euler.h"

#define STR_LEN 64

int ispalindrome(int pal) {
    // convert number to string, then set pointers to beginning and
    // end of string, start comparing each pointer and moving in until
    // pbeg >= pend
    char *pbeg;
    char *pend;
    char str[STR_LEN];
    int len;
    int ispal = 1;

    memset(str, 0, STR_LEN);
    len = sprintf(str, "%d", pal);
    pbeg = str;
    pend = str + len - 1;

    while (pend > pbeg) {
        if (*pend-- != *pbeg++) {
            ispal = 0;
            break;
        }
    }

    return ispal;
}

int main(int argc, char *argv[])
{
    int i, j;
    int pal = 0;
    int cand = 0;

    for (i = 999; i >= 100; i--) {
        for (j = 999; j >= 100; j--) {
            cand = i * j;
            if (ispalindrome(cand)) {
                pal = max(pal, cand);
            }
        }
    }
    printf("pal = %d\n", pal);
    return 0;
}

/* vim: set ts=4: */
