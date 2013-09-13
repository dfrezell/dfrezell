/*
 * linker_wrap.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <unistd.h>

#ifdef __cplusplus
extern "C" {
#endif

extern void *__real_calloc(size_t nmemb, size_t size);
extern void *__real_malloc(size_t size);
extern void __real_free(void *ptr);
extern void *__real_realloc(void *ptr, size_t size);

void print_call(const char *func)
{
    printf("called %s\n", func);
}

void *__wrap_calloc(size_t nmemb, size_t size)
{
    print_call(__PRETTY_FUNCTION__);
    return __real_calloc(nmemb, size);
}

void *__wrap_malloc(size_t size)
{
    print_call(__PRETTY_FUNCTION__);
    return __real_malloc(size);
}

void __wrap_free(void *ptr)
{
    print_call(__PRETTY_FUNCTION__);
    __real_free(ptr);
}

void *__wrap_realloc(void *ptr, size_t size)
{
    print_call(__PRETTY_FUNCTION__);
    return __real_realloc(ptr, size);
}

#ifdef __cplusplus
}
#endif
/* vim: set ts=4: */

