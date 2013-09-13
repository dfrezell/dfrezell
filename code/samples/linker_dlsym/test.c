/*
 * test.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[])
{
    char *buf = malloc(100);
    strcpy(buf, "hello");
    printf("buf = '%s'\n", buf);
    free(buf);
    return 0;
}

/* vim: set ts=4: */

