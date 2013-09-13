/*
 * getip.c
 *
 * Developed by Drew Frezell
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <netdb.h>

#define HOSTNAME_LEN 256
int main(int argc, char *argv[])
{
    char mysubnet[16];
    char hostname[HOSTNAME_LEN];

    // start with a resonable default, in case we fail to look up our own ip
    // address.
    strcpy(mysubnet, "192.168.1");
    memset(hostname, 0, HOSTNAME_LEN);

    if (gethostname(hostname, HOSTNAME_LEN - 1) == 0)
    {
        struct hostent *host = gethostbyname(hostname);
        // make sure we are not dealing with a NULL pointer somewhere along
        // the path
        if (host && host->h_addr_list && host->h_addr)
        {
            char *ptr = NULL;
            inet_ntop(AF_INET, host->h_addr, mysubnet, 16);
            if (ptr = strrchr(mysubnet, '.'))
            {
                *ptr = '\0';
            }
        }
    }

    return 0;
}

/* vim: set ts=4: */

