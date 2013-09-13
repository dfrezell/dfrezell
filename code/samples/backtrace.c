/*
 * bactrace.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>


#define BOUNDED_1(PTR) BOUNDED_N(PTR, 1)

struct layout {
    void *next;
    void *return_address;
};

extern void *__libc_stack_end;

int backtrace(void **array, int size) {
    // define a variable that will give us an address to the stack frame
    int stackframe;
    struct layout *current;
    void *top_frame;
    void *top_stack;
    int cnt = 0;

    top_frame = __builtin_frame_address(0);
    top_stack = &stackframe;

    current = (struct layout *) top_frame;

    while (cnt < size) {
        if ((void *) current < top_stack ||
                !((void *) current < __libc_stack_end)) {
            // address is out of range.  Note that for the toplevel we see
            // a frame pointer with value NULL which clearly is out of range
            break;
        }
        array[cnt++] = current->return_address;
        current = (struct layout *) current->next;
    }

    return cnt;
}

// assume the worst for the width of an address
#define WORD_WIDTH 16

char **backtrace_symbols(void *const *array, int size) {
    int cnt;
    size_t total = 0;
    char **result;

    // We can compute the text size needed for the symbols since we print
    // them all as "[+0x<addr>]".
    total = size * (WORD_WIDTH + 6);
    result = malloc(size * sizeof(char *) + total);
    if (result != NULL) {
        char *last = (char *) (result + size);

        for (cnt = 0; cnt < size; cnt++) {
            result[cnt] = last;
            last += 1 + sprintf(last, "[+%p]", array[cnt]);
        }
    }

    return result;
}

#define BT_DEPTH 100
int do_backtrace(int n) {
    void *bt[BT_DEPTH];
    char **btsym;
    int depth;
    int i;

    depth = backtrace(bt, BT_DEPTH);
    btsym = backtrace_symbols(bt, depth);

    printf("backtrace for n = %d\n", n);
    for (i = 0; i < depth; i++) {
        printf("\t%p\n", btsym[i]);
    }

    free(btsym);
    return 0;
}

int recurse(int n) {
    do_backtrace(n);
    if (n == 1) return 1;
    return n + recurse(n - 1);
}

int foo(int n) {
    do_backtrace(n);

    if (n > 0) foo(n-1);
    return 0;
}

int main(int argc, char *argv[])
{
    int n = 10;

    //foo(n);
    //printf("fibo(%d) : %d\n", n, recurse(n));
    return 0;
}

/* vim: set ts=4: */

