#include <stdio.h>

typedef int (*fp_void)();
typedef int (*fp_onearg)(int);
typedef int (*fp_twoarg)(int,int);
typedef int (*fp_threearg)(int,int,int);
typedef int (*fp_fourarg)(int,int,int,int);
typedef int (*fp_fivearg)(int,int,int,int,int);
typedef int (*fp_sixarg)(int,int,int,int,int,int);
typedef int (*fp_sevenarg)(int,int,int,int,int,int,int);

typedef union _fparam {
    void *      func;
    fp_void     func_0;
    fp_onearg   func_1;
    fp_twoarg   func_2;
    fp_threearg func_3;
    fp_fourarg  func_4;
    fp_fivearg  func_5;
    fp_sixarg   func_6;
    fp_sevenarg func_7;
} fparam_t;

typedef enum {
    VOID_ARG,
    ONE_ARG,
    TWO_ARG,
    THREE_ARG,
    FOUR_ARG,
    FIVE_ARG,
    SIX_ARG,
    SEVEN_ARG
} fparam_type_t;

typedef struct _func_template {
    fparam_t fp;
    fparam_type_t type;
} func_template_t;

char *foo(char *p) {
    return p+1;
}

char *bar(char *p, char *q, char *r, char *s, int i, int j, int k) {
    return s+1;
}

// whats the goal here?  I want to take a string, that contains an address
// and a list of space separated arguments, break each argument into an
// int or char and call the appropriate function based on the number of
// arguments.
// The big assumption is that int and char* are passed in exactly the
// same way.
int main(int argc, char *argv[])
{
    func_template_t t;
    t.fp.func = (void *)bar;
    t.type = SEVEN_ARG;

    switch (t.type) {
        case VOID_ARG:
            printf("%s\n", (char *)t.fp.func_0());
            break;
        case ONE_ARG:
            printf("%s\n", (char *)t.fp.func_1((int)"test"));
            break;
        case TWO_ARG:
            printf("%s\n", (char *)t.fp.func_2((int)"test", 3992));
            break;
        case THREE_ARG:
            printf("%s\n", (char *)t.fp.func_3((int)"test", (int)"goop", 2));
            break;
        case FOUR_ARG:
            printf("%s\n", (char *)t.fp.func_4((int)"test", (int)"buzy", (int)"vim", 3));
            break;
        case FIVE_ARG:
            printf("%s\n", (char *)t.fp.func_5((int)"test", (int)"gone", (int)"gem", 5, 5));
            break;
        case SIX_ARG:
            printf("%s\n", (char *)t.fp.func_6((int)"test", (int)"foo", (int)"baz", (int)"buz", 3, 2));
            break;
        case SEVEN_ARG:
            printf("%s\n", (char *)t.fp.func_7((int)"test", (int)"foo", (int)"bar", (int)"baz", 7, 3, 1));
            break;
        default:
            break;
    }

    return 0;
}
