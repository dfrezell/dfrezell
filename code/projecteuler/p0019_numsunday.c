/*
 * p0019_numsunday.c
 *
 */

#include <stdio.h>

#define NYEAR 365
#define LYEAR 366

#define JAN 31
#define FEB 28
#define MAR 31
#define APR 30
#define MAY 31
#define JUN 30
#define JUL 31
#define AUG 31
#define SEP 30
#define OCT 31
#define NOV 30
#define DEC 31

typedef enum _dow_t {
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    NUM_DOWS
} dow_t;

typedef struct _cal_t {
    int year;
    dow_t firstday;
    int leap;
} cal_t;


int main(int argc, char *argv[])
{
    // we start with knowning Jan 1, 1900 is on a Monday.  We then need to
    // compute the number of sundays on the first day of the month from
    // Jan 1, 1901 - Dec 31, 2000
    cal_t cal = { 1900, MON, 0 };
    int numdays = cal.leap ? LYEAR : NYEAR;
    int numsun = 0;

    cal.year += 1; // move ahead a year
    // set our new first day of the year to the right day of the week
    cal.firstday = (cal.firstday + (numdays % NUM_DOWS)) % NUM_DOWS;

    for (cal.year = 1901; cal.year < 2001; cal.year++) {
        // check for leap year
        if (((cal.year % 4) == 0) &&
                ((cal.year % 100) || ((cal.year % 400) == 0))) {
            cal.leap = 1;
        } else {
            cal.leap = 0;
        }
        numdays = cal.leap ? LYEAR : NYEAR;
        // go through each month to figure out the first day of the month
        // JAN
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (JAN % NUM_DOWS)) % NUM_DOWS;
        // FEB
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + ((FEB+cal.leap) % NUM_DOWS)) % NUM_DOWS;
        // MAR
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (MAR % NUM_DOWS)) % NUM_DOWS;
        // APR
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (APR % NUM_DOWS)) % NUM_DOWS;
        // MAY
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (MAY % NUM_DOWS)) % NUM_DOWS;
        // JUN
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (JUN % NUM_DOWS)) % NUM_DOWS;
        // JUL
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (JUL % NUM_DOWS)) % NUM_DOWS;
        // AUG
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (AUG % NUM_DOWS)) % NUM_DOWS;
        // SEP
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (SEP % NUM_DOWS)) % NUM_DOWS;
        // OCT
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (OCT % NUM_DOWS)) % NUM_DOWS;
        // NOV
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (NOV % NUM_DOWS)) % NUM_DOWS;
        // DEC
        numsun += (cal.firstday == SUN);
        cal.firstday = (cal.firstday + (DEC % NUM_DOWS)) % NUM_DOWS;

    }

    printf("num days = %d\n", numsun);
    return 0;
}

/* vim: set ts=4: */
