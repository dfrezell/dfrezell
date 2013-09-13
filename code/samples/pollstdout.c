/*
 * pollstdout.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <pthread.h>
#include <poll.h>
#include <unistd.h>
#include <syscall.h>

#define LOG_LEN 1024

void *logger_thread(void *arg) {
    int i;
    int tid = syscall(SYS_gettid);
    while (1) {
        i++;
        if ((i % 100000) == 0)
            printf("[%d] : %d\n", tid, i);
    }
}

void *poll_thread(void *arg) {
    int i;
    int tid = syscall(SYS_gettid);
    while (1) {
        i++;
        if ((i % 100000) == 0)
            printf("[%d] : %d\n", tid, i);
    }
#if 0
    struct pollfd pfd;
    int ret;
    char log[LOG_LEN];

    pfd.fd = STDERR_FILENO;
    pfd.events = POLLIN;

    while ((ret = poll(&pfd, 1, -1)) > 0) {
        if (pfd.revents & POLLIN) {
            printf("reading stderr\n");
            fgets(log, LOG_LEN, stderr);
            printf("log message '%s'\n", log);
        }
        if (pfd.revents & POLLERR) {
            printf("poll err\n");
        }
        if (pfd.revents & POLLNVAL) {
            printf("poll nval\n");
        }
    }
#endif
}

int main(int argc, char *argv[])
{
    pthread_t t1, t2;

    pthread_create(&t1, NULL, logger_thread, NULL);
    pthread_create(&t2, NULL, poll_thread, NULL);

    pthread_join(t1, NULL);
    pthread_cancel(t2);
    return 0;
}

/* vim: set ts=4: */

