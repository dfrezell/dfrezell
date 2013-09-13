/*
 * elf.c
 *
 * Developed by Drew Frezell
 *
 */

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>

#include <error.h>
#include <fcntl.h>
#include <libelf.h>

int main(int argc, char *argv[])
{
    int fd;
    Elf *e;
    char *k;
    Elf_Kind ek;

    if (argc != 2)
        error(-1, 0, "usage: %s file-name\n", program_invocation_short_name);

    if (elf_version(EV_CURRENT) == EV_NONE)
        error(-1, 0, "elf library init failed\n");

    if ((fd = open(argv[1], O_RDONLY, 0)) < 0)
        error(-1, 1, "open %s failed\n", argv[1]);

    if ((e = elf_begin(fd, ELF_C_READ, NULL)) == NULL)
        error(-1, 0, "elf_begin failed : %s\n", elf_errmsg(-1));

    ek = elf_kind(e);

    switch (ek) {
        case ELF_K_AR:
            k = "ar(1) archive";
            break;
        case ELF_K_ELF:
            k = "elf object";
            break;
        case ELF_K_NONE:
            k = "data";
            break;
        default:
            k = "unknown";
            break;
    }

    printf("%s : %s\n", argv[1], k);

    elf_end(e);
    close(fd);

    return 0;
}

/* vim: set ts=4: */

