/*
 * dlmalloc.c
 *
 * Developed by Drew Frezell
 *
 */

// need to define this if we want to use RTLD_NEXT
#define _GNU_SOURCE

#include <unistd.h>
#include <dlfcn.h>
#include <execinfo.h>
#include <string.h>

#define BT_DEPTH 8
#define KEY 'CIEN'

struct malloc_hdr {
    size_t size;
    void *bt[BT_DEPTH];
    int key;
};

void *malloc(size_t size)
{
    static void *(*mallocp)(size_t);
    void *buf = NULL;
    struct malloc_hdr *hdr = NULL;

    if (!mallocp)
        mallocp = dlsym(RTLD_NEXT, "malloc");

    buf = (*mallocp)(size + sizeof(struct malloc_hdr) + 4);
    hdr = (struct malloc_hdr *)buf;
    hdr->size = size;
    hdr->key = KEY;
    backtrace(hdr->bt, BT_DEPTH);

    return (*mallocp)(size);
}

void free(void *ptr)
{
    static void (*freep)(void *);

    if (!freep)
        freep = dlsym(RTLD_NEXT, "free");

    if (ptr)
    {
        struct malloc_hdr *hdr = (struct malloc_hdr *)(((char *)ptr) - sizeof(struct malloc_hdr));

        if (hdr->key == KEY)
        {
            // this memory needs to be aligned to a 4 byte boundary
            int *foot = (int *)((char *)ptr) + hdr->size;

            if (*foot == KEY)
            {
                // our footer was intact, so no memory overrun
            }
            else
            {
                // exit the app with memory dump and backtrace of the
                // offending caller.
            }
            ptr = (void *) hdr;
        }
        else
        {
            // we don't have a valid header key, so two things could have
            // happened.  we somehow got memory allocated that bypassed our
            // code or something corrupted the header, in either case we can't
            // trust the header so leave it untouched and call free with the
            // original pointer...which may blow up.
        }
    }
    (*freep)(ptr);
}

void *calloc(size_t nmemb, size_t size)
{
    int len = size *nmemb;
    void *ptr = malloc(len);
    memset(ptr, 0, len);
    return ptr;
}

void *realloc(void *ptr, size_t size)
{
    static void *(*reallocp)(void *, size_t);
    void *buf = NULL;
    struct malloc_hdr *hdr = NULL;

    if (!reallocp)
        reallocp = dlsym(RTLD_NEXT, "realloc");

    if (ptr)
    {
        ptr = ptr - sizeof(struct malloc_hdr);
    }
    else
    {
    }

    buf = (*reallocp)(ptr, size);
    hdr = (struct malloc_hdr *)buf;
    hdr->size = size;
    hdr->key = KEY;
    backtrace(hdr->bt, BT_DEPTH);

    return (((char *)buf) + sizeof(struct malloc_hdr));
}

/* vim: set ts=4: */

