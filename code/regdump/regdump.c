/*
 * regdump.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#define PHEX(x) printf("%s : %x\n", #x, x)
#define PDEC(x) printf("%s : %d\n", #x, x)
#define PSTR(x) printf("%s : %s\n", #x, x)

typedef struct _regdump {
    int fd;
    off_t offset;
    size_t length;
    immap_t *regs;
} regdump_t;

int main(int argc, char *argv[])
{
    regdump_t rd;
    // int pagesize = sysconf(_SC_PAGE_SIZE);

    rd.fd = open("/dev/mem", O_RDONLY);
    rd.offset = 0xff400000;
    rd.length = sizeof(immap_t);
    rd.regs = mmap(NULL, rd.length, PROT_READ, MAP_PRIVATE, rd.fd, rd.offset);

    PHEX(rd.regs->sysconf.immrbar);
    PHEX(rd.regs->sysconf.altcbar);
    PHEX(rd.regs->sysconf.lblaw[0].bar);
    PHEX(rd.regs->sysconf.lblaw[0].ar);
    PHEX(rd.regs->sysconf.lblaw[1].bar);
    PHEX(rd.regs->sysconf.lblaw[1].ar);
    PHEX(rd.regs->sysconf.lblaw[2].bar);
    PHEX(rd.regs->sysconf.lblaw[2].ar);
    PHEX(rd.regs->sysconf.lblaw[3].bar);
    PHEX(rd.regs->sysconf.lblaw[3].ar);
    PHEX(rd.regs->sysconf.pcilaw[0].bar);
    PHEX(rd.regs->sysconf.pcilaw[0].ar);
    PHEX(rd.regs->sysconf.pcilaw[1].bar);
    PHEX(rd.regs->sysconf.pcilaw[1].ar);
    PHEX(rd.regs->sysconf.ddrlaw[0].bar);
    PHEX(rd.regs->sysconf.ddrlaw[0].ar);
    PHEX(rd.regs->sysconf.ddrlaw[1].bar);
    PHEX(rd.regs->sysconf.ddrlaw[1].ar);

    munmap(rd.regs, rd.length);
    close(rd.fd);
    return 0;
}

/* vim: set ts=4: */

