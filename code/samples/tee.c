/*
 * tee.c
 *
 * Developed by Drew Frezell
 *
 */

#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <assert.h>
#include <errno.h>
#include <limits.h>

int main(int argc, char *argv[])
{
    int fd;
    int len;
    int slen;
    struct stat sb;

    if (argc < 2) {
        fprintf(stderr, "%s: outfile\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if ((fstat(STDIN_FILENO, &sb) < 0) || !S_ISFIFO(sb.st_mode)) {
        perror("fstat");
        fprintf(stderr, "stdin must be a pipe\n");
        exit(EXIT_FAILURE);
    }
    if ((fstat(STDOUT_FILENO, &sb) < 0) || !S_ISFIFO(sb.st_mode)) {
        perror("fstat");
        fprintf(stderr, "stdout must be a pipe\n");
        exit(EXIT_FAILURE);
    }

    if ((fd = open(argv[1], O_WRONLY | O_CREAT | O_TRUNC, 0644)) == -1) {
        perror("open");
        exit(EXIT_FAILURE);
    }

    do {
        // tee stdin to stdout
        if ((len = tee(STDIN_FILENO, STDOUT_FILENO, 1024, 0)) < 0) {
            perror("tee");
            exit(EXIT_FAILURE);
        } else {
            if (len == 0)
                break;
            // consume stdin by splicing it to a file
            while (len > 0) {
                if ((slen = splice(STDIN_FILENO, NULL, fd, NULL, len, SPLICE_F_MOVE)) < 0) {
                    perror("splice");
                    break;
                }
                len -= slen;
            }
        }
    } while (1);

    close(fd);

    return 0;
}

/* vim: set ts=4: */

