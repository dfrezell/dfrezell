/*
 * main.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    const char *lib = "libdllib.so";
    void *hdl = dlopen(lib, RTLD_NOW | RTLD_GLOBAL);
    if (hdl == NULL)
    {
        fprintf(stderr, "invalid library: %s\n", lib);
        exit(-1);
    }
    void (*sym)(void *) = dlsym(hdl, "crash_bad_address");
    if (sym == NULL)
    {
        fprintf(stderr, "invalid symbol: crash_bad_address\n");
        exit(-1);
    }
    sym((void *)0xbffc7000);
    dlclose(hdl);
    return 0;
}

