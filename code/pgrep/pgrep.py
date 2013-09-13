#!/usr/bin/python

import os
import sys
import re

def parse_options():
    from optparse import OptionParser
    parser = OptionParser(version="%prog 0.1")
    parser.add_option("--debug", action="store_true", dest="debug",
            default=False, help="print extra debug output")
    return parser.parse_args()

def pgrep(name):
    prog = re.compile(name)
    pids = [(pid, os.path.join("/proc", pid, "stat"))
                for pid in os.listdir("/proc") if pid.isdigit()]
    matches = []
    for pid,cmdline in pids:
        fd = open(cmdline)
        # remove the carriage return and split on ' ' (space)
        line = fd.readline().strip().split(' ')
        fd.close()
        # first two fields in stat are [pid, name, ...]
        mpid, mname = line[0], line[1]
        if prog.search(mname):
            matches.append(mpid)
    return matches

def main(*argv):
    (options, args) = parse_options()
    m = pgrep("simweb")
    print m
    return 0

if __name__ == "__main__":
    sys.exit(main(*sys.argv))


