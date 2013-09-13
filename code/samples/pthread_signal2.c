/*
 * signals.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include <errno.h>
#include <string.h>
#include <syscall.h>

#define SIGITIMER (SIGRTMIN + 1)
#define SIGTTIMER (SIGRTMIN + 2)

#define DBGS(x) printf("%s : %s\n",  #x, (x))
#define DBGD(x) printf("%s : %d\n",  #x, (x))
#define DBGL(x) printf("%s : %ld\n", #x, (x))
#define DBGX(x) printf("%s : %x\n",  #x, (x))
#define DBGP(x) printf("%s : %p\n",  #x, (x))

typedef struct _timer_func_t {
    pthread_t id;
    pid_t tid;
    void *(*timeout_function)(void *);
    void *pthis;
} timer_func_t;

void *timeout_func(void *arg) {
    printf("timer expired for %p in thread %p\n", arg, pthread_self());
    return NULL;
}

void alarm_function(int sig, siginfo_t *info, void *arg) {
    //timer_func_t *ptf = (timer_func_t *)info->si_value.sival_ptr;
    //ptf->timeout_function(ptf->id);
    printf("timer expired for %p in thread %p\n", arg, pthread_self());
}

int start_timer(int duration, timer_t *timerid, void *payload) {
    struct sigevent evp;
    struct itimerspec it;

    evp.sigev_notify = SIGEV_SIGNAL;
    evp.sigev_signo = SIGTTIMER;
    evp.sigev_value.sival_ptr = payload;

    it.it_value.tv_sec = 0;
    it.it_value.tv_nsec = duration;
    it.it_interval.tv_sec = 0;
    it.it_interval.tv_nsec = 0;

    timer_create(CLOCK_MONOTONIC, &evp, timerid);
    timer_settime(*timerid, 0, &it, NULL);

    return 0;
}

void install_sigalrm_handler() {
    struct sigaction sigact;

    sigact.sa_sigaction = alarm_function;
    sigact.sa_flags = SA_SIGINFO;
    sigfillset(&sigact.sa_mask);

    sigaction(SIGALRM, &sigact, NULL);
}

void *thread_function(void *arg) {
    int timeout = (int)arg * 10;
    int i;
    timer_func_t payload;
    timer_t tid;

    memset(&payload, 0, sizeof(payload));
    payload.id = pthread_self();
    payload.tid = syscall(SYS_gettid);
    payload.timeout_function = timeout_func;
    payload.pthis = 0;
    printf("pthread(%p) : payload(%p)\n", (void *)pthread_self(), &payload);

    install_sigalrm_handler();

    for (i = 0; i < 10; i++) {
        start_timer(timeout, &tid, &payload);
        usleep(10000);
        timer_delete(tid);
    }

    return 0;
}

void *sig_timer_catcher(void *arg) {
    sigset_t sigmask;
    int sig = (int)arg;
    siginfo_t siginfo;
    timer_func_t *ptf;

    sigemptyset(&sigmask);
    sigaddset(&sigmask, sig);

    printf("sig_catcher : %p\n", pthread_self());
    memset(&siginfo, 0, sizeof(siginfo));
    while (1) {
        sig = sigwaitinfo(&sigmask, &siginfo);
        if (sig == SIGITIMER) {
            // call the timout function for the thread the set the timer.
            //printf("SIGITIMER fired : %d\n", ++count);
            ptf = (timer_func_t *) siginfo.si_value.sival_ptr;
            ptf->timeout_function((void *)ptf->id);
        } else if (sig == SIGTTIMER) {
            // do a pthread kill after finding the thread that set the timer.
            ptf = (timer_func_t *) siginfo.si_value.sival_ptr;
            //sigqueue(ptf->tid, SIGALRM, siginfo.si_value);
            pthread_kill(ptf->id, SIGALRM);
        } else if (sig == -1) {
            //if (errno == EINTR)
            //    continue;
            // sigwaitinfo returned an error, how do we handle this?
            printf("error in sigwaitinfo : %s\n", strerror(errno));
        }
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t ptid;
    sigset_t sigmask;
    siginfo_t siginfo;
    int sig;

    sigfillset(&sigmask);
    sigdelset(&sigmask, SIGALRM);
    pthread_sigmask(SIG_BLOCK, &sigmask, NULL);

    //printf("main : 0x%8.8x\n", pthread_self());
    pthread_create(&ptid, NULL, sig_timer_catcher, (void *)SIGITIMER);
    pthread_create(&ptid, NULL, sig_timer_catcher, (void *)SIGTTIMER);

    //sleep(1);
    for (i = 1; i <= 1; i++) {
        pthread_create(&ptid, NULL, thread_function, (void *)i);
    }

    sigfillset(&sigmask);
    sigdelset(&sigmask, SIGALRM);
    sigdelset(&sigmask, SIGITIMER);
    sigdelset(&sigmask, SIGTTIMER);

    while (1) {
        sig = sigwaitinfo(&sigmask, &siginfo);
        switch (sig) {
            case SIGINT:
                printf("got SIGINT\n");
                exit(0);
                break;
            case SIGQUIT:
                printf("got SIGQUIT\n");
                exit(-1);
                break;
            default:
                printf("got signo : %d\n", sig);
                break;
        }
    }

    return 0;
}

/* vim: set ts=4: */

