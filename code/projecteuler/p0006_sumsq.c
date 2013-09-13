#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[])
{
    int sumsq = 0;
    int sqsum = 0;
    int i;

    for (i = 1; i <= 100; i++) {
        sumsq += (i * i);
        sqsum += i;
    }
    sqsum *= sqsum;

    printf("diff = %d\n", sqsum - sumsq);
    return 0;
}

/* vim: set ts=4: */
