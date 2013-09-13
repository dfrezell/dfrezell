#!/bin/sh

#
# dtrace.sh
#
# Changelog:
# 2008-09-22 - created
#

# run dtrace in one terminal then run the application you wish to monitor
# Files opened by process,
dtrace -n 'syscall::open*:entry { printf("%s %s",execname,copyinstr(arg0)); }'
