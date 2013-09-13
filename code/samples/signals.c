/*
 * signals.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include <errno.h>

#define BUFSIZE 1024

#define SIGITIMER (SIGRTMIN + 1)
#define SIGTTIMER (SIGRTMIN + 2)

char *hex = "0123456789abcdef";

void LnITimerAction(int signo, siginfo_t *siginfo, void *ptr) {
    unsigned int self = (unsigned int)pthread_self();
    unsigned int other = (unsigned int)(siginfo->si_value.sival_ptr);
    char buf[33];

    buf[0] = 'l';
    buf[1] = 'n';
    buf[2] = 'i';
    buf[3] = 't';
    buf[4] = 'i';
    buf[5] = 'm';
    buf[6] = 'r';
    buf[7] = ' ';
    buf[8] = '=';
    buf[9] = ' ';
    buf[10] = '0';
    buf[11] = 'x';
    buf[12] = hex[(self >> 28) % 16];
    buf[13] = hex[(self >> 24) % 16];
    buf[14] = hex[(self >> 20) % 16];
    buf[15] = hex[(self >> 16) % 16];
    buf[16] = hex[(self >> 12) % 16];
    buf[17] = hex[(self >>  8) % 16];
    buf[18] = hex[(self >>  4) % 16];
    buf[19] = hex[(self >>  0) % 16];
    buf[20] = ' ';
    buf[21] = '0';
    buf[22] = 'x';
    buf[23] = hex[(other >> 28) % 16];
    buf[24] = hex[(other >> 24) % 16];
    buf[25] = hex[(other >> 20) % 16];
    buf[26] = hex[(other >> 16) % 16];
    buf[27] = hex[(other >> 12) % 16];
    buf[28] = hex[(other >>  8) % 16];
    buf[29] = hex[(other >>  4) % 16];
    buf[30] = hex[(other >>  0) % 16];
    buf[31] = '\n';
    buf[32] = '\0';

    write(STDOUT_FILENO, buf, 33);
}

void LnTTimerAction(int signo, siginfo_t *siginfo, void *ptr) {
    unsigned int self = (unsigned int)pthread_self();
    unsigned int other = (unsigned int)(siginfo->si_value.sival_ptr);
    char buf[33];

    buf[0] = 'l';
    buf[1] = 'n';
    buf[2] = 't';
    buf[3] = 't';
    buf[4] = 'i';
    buf[5] = 'm';
    buf[6] = 'r';
    buf[7] = ' ';
    buf[8] = '=';
    buf[9] = ' ';
    buf[10] = '0';
    buf[11] = 'x';
    buf[12] = hex[(self >> 28) % 16];
    buf[13] = hex[(self >> 24) % 16];
    buf[14] = hex[(self >> 20) % 16];
    buf[15] = hex[(self >> 16) % 16];
    buf[16] = hex[(self >> 12) % 16];
    buf[17] = hex[(self >>  8) % 16];
    buf[18] = hex[(self >>  4) % 16];
    buf[19] = hex[(self >>  0) % 16];
    buf[20] = ' ';
    buf[21] = '0';
    buf[22] = 'x';
    buf[23] = hex[(other >> 28) % 16];
    buf[24] = hex[(other >> 24) % 16];
    buf[25] = hex[(other >> 20) % 16];
    buf[26] = hex[(other >> 16) % 16];
    buf[27] = hex[(other >> 12) % 16];
    buf[28] = hex[(other >>  8) % 16];
    buf[29] = hex[(other >>  4) % 16];
    buf[30] = hex[(other >>  0) % 16];
    buf[31] = '\n';
    buf[32] = '\0';

    write(STDOUT_FILENO, buf, 33);
}

int start_timer(int duration, timer_t *timerid, struct sigevent *evp, struct itimerspec *it) {
    evp->sigev_notify = SIGEV_SIGNAL;
    evp->sigev_signo = SIGITIMER;
    evp->sigev_value.sival_ptr = (void *)pthread_self();

    it->it_value.tv_sec = 0;
    it->it_value.tv_nsec = duration;
    it->it_interval.tv_sec = 0;
    it->it_interval.tv_nsec = 0;

    //printf("timer set for %d ns\n", it->it_value.tv_nsec);
    timer_create(CLOCK_MONOTONIC, evp, timerid);
    timer_settime(*timerid, 0, it, NULL);

    return 0;
}

void *thread_function(void *arg) {
    int timeout = (int)arg * 10;
    int i;
    timer_t tid[10];
    struct sigevent evp;
    struct itimerspec it;

    //printf("timer thread %d : 0x%8.8x\n", timeout, pthread_self());
    for (i = 0; i < 10; i++) {
        start_timer(timeout, &(tid[i]), &evp, &it);
        usleep(10000);
        timer_delete(tid[i]);
    }
}

void *LnTTimer_Catcher(void *arg) {
    struct sigaction act;

    sigemptyset(&act.sa_mask);
    sigaddset(&act.sa_mask, SIGTTIMER);
    act.sa_flags = SA_SIGINFO;
    act.sa_sigaction = LnTTimerAction;

    pthread_sigmask(SIG_UNBLOCK, &act.sa_mask, NULL);
    sigaction(SIGTTIMER, &act, NULL);

    //printf("LnTTimer catcher : 0x%8.8x\n", pthread_self());

    while (1)
        pause();

    return NULL;
}

void *LnITimer_Catcher(void *arg) {
    sigset_t set;
    int sig;

    struct sigaction act;

    sigemptyset(&act.sa_mask);
    sigaddset(&act.sa_mask, SIGITIMER);
    act.sa_flags = SA_SIGINFO;
    act.sa_sigaction = LnITimerAction;

    pthread_sigmask(SIG_UNBLOCK, &act.sa_mask, NULL);
    sigaction(SIGITIMER, &act, NULL);

    //printf("LnITimer catcher : 0x%8.8x\n", pthread_self());

    while (1) {
        pause();
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t ptid;
    sigset_t signal_set;

    sigfillset(&signal_set);
    sigdelset(&signal_set, SIGINT);
    sigdelset(&signal_set, SIGALRM);
    pthread_sigmask(SIG_BLOCK, &signal_set, NULL);

    //printf("main : 0x%8.8x\n", pthread_self());
    pthread_create(&ptid, NULL, LnTTimer_Catcher, NULL);
    pthread_create(&ptid, NULL, LnITimer_Catcher, NULL);

    //sleep(1);
    for (i = 1; i <= 100; i++) {
        pthread_create(&ptid, NULL, thread_function, (void *)i);
    }

    pthread_join(ptid, NULL);

    return 0;
}

/* vim: set ts=4: */

