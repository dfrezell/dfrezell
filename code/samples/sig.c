/*
 * sig.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

void sig_memset(void *s, int c, size_t n)
{
    char *ptr = s;
    int i;
    for (i = 0; i < n; i++)
        ptr[i] = (char)c;
}

int sig_strlen(char *s)
{
    int len = 0;
    while (*s++ != '\0')
        len++;
    return len;
}

int sig_strcpy(char *d, char *s)
{
    int len = sig_strlen(s);
    int i;
    for (i = 0; i < len; i++)
        d[i] = s[i];
    return len;
}

int sig_strnum(char *d, int n)
{
    int len = 1;
    if (n < 10)
    {
        *d = '0' + n;
        return len;
    }
    len += sig_strnum(d, n / 10);
    d[len - 1] = '0' + (n % 10);
    return len;
}

int sig_strcode(char *d, int sig, int code)
{
    int len = 0;
    switch (code)
    {
        case SI_USER:
            len = sig_strcpy(d, "[SI_USER] ");
            break;
        case SI_KERNEL:
            len = sig_strcpy(d, "[SI_KERNEL] ");
            break;
        case SI_QUEUE:
            len = sig_strcpy(d, "[SI_QUEUE] ");
            break;
        case SI_TIMER:
            len = sig_strcpy(d, "[SI_TIMER] ");
            break;
        case SI_MESGQ:
            len = sig_strcpy(d, "[SI_MESGQ] ");
            break;
        case SI_ASYNCIO:
            len = sig_strcpy(d, "[SI_ASYNCIO] ");
            break;
        case SI_SIGIO:
            len = sig_strcpy(d, "[SI_SIGIO] ");
            break;
        case SI_TKILL:
            len = sig_strcpy(d, "[SI_TKILL] ");
            break;
        default:
            len = sig_strcpy(d, "[unknown] ");
            break;
    }
    return len;
}

int sig_write(char *out, char *file)
{
    char buf[128];
    int len = 0;
    int xread = 0;
    int fd = -1;

    if ((fd = open(file, O_RDONLY)) == -1)
    {
        len = sig_strcpy(out, "\t<missing file>");
        return len;
    }

    sig_memset(buf, 0, sizeof(buf));
    while ((xread = read(fd, buf, 127)) > 0)
    {
        len += sig_strcpy(out + len, buf);
        sig_memset(buf, 0, sizeof(buf));
    }
    close(fd);

    return len;
}

void sig_callback(int sig, siginfo_t *info, void *ctx)
{
    char output[2048];
    char file[256];
    char *ptr = NULL;
    int len = 0;
    sig_memset(file, 0, sizeof(file));
    len += sig_strcpy(file + len, "/proc/");
    len += sig_strnum(file + len, info->si_pid);
    len += sig_strcpy(file + len, "/stat");

    len = 0;
    sig_memset(output, 0, sizeof(output));
    len += sig_strcpy(output + len, "signal:");
    len += sig_strcpy(output + len, "\n\tsi_signo: ");
    len += sig_strnum(output + len, info->si_signo);
    len += sig_strcpy(output + len, "\n\tsi_errno: ");
    len += sig_strnum(output + len, info->si_errno);
    len += sig_strcpy(output + len, "\n\tsi_code: ");
    len += sig_strcode(output + len, info->si_signo, info->si_code);
    len += sig_strnum(output + len, info->si_code);
    len += sig_strcpy(output + len, "\n\tsi_pid: ");
    len += sig_strnum(output + len, info->si_pid);
    len += sig_strcpy(output + len, "\n\tsi_uid: ");
    len += sig_strnum(output + len, info->si_uid);
    len += sig_strcpy(output + len, "\n");
    len += sig_strcpy(output + len, file);
    len += sig_strcpy(output + len, ":\n");
    len += sig_write(output + len, file);
    len += sig_strcpy(output + len, "\n");
    write(STDOUT_FILENO, output, len);
}

int main(int argc, char *argv[])
{
    struct sigaction action;
    action.sa_sigaction = sig_callback;
    action.sa_flags = SA_RESETHAND | SA_SIGINFO;
    sigaction(SIGINT, &action, NULL);
    printf("pid = %d\n", getpid());
    pause();
    return 0;
}

