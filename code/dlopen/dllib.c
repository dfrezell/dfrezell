/*
 * dllib.c
 *
 * Developed by Drew Frezell
 *
 */

void crash_bad_address(void *addr)
{
    *((int *) addr) = 0;
}
