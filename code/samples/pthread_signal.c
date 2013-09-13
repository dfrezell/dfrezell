/*
 * pthread_signal.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <signal.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
    pthread_t tid;
    sigset_t sigmask;

    // We want all newly created threads to block all signals, by inheriting
    // our signal mask from the main thread.
    sigfillset(&sigmask);
    sigdelset(&sigmask, SIGINT);
    sigdelset(&sigmask, SIGALRM);
    pthread_sigmask(SIG_BLOCK, &sigmask, NULL);

    pthread_create(&tid, NULL, LnITimer_Catcher, NULL);
    pthread_create(&tid, NULL, LnTTimer_Catcher, NULL);

    return 0;
}

/* vim: set ts=4: */

