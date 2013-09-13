#!/usr/bin/env python

from random import choice
import getopt
import string
import sys

def GenPasswd():
    chars = string.letters + string.digits
    for i in range(8):
        newpasswd = newpasswd + choice(chars)
    return newpasswd

def GenPasswd2(length=8, chars=string.letters + string.digits):
    return ''.join([choice(chars) for i in range(length)])

class Options(object):
    pass

def main(argv):
    (optionList,args) = getopt.getopt(argv[1:],"r:l:",["repeat=","length="])

    options = Options()
    options.repeat = 1
    options.length = 8
    for (key,value) in optionList:
        if key == "-r" or key == "--repeat":
            options.repeat = int(value)
        elif key == "-l" or key == "--length":
            options.length = int(value)

    for i in xrange(options.repeat):
        print GenPasswd2(options.length)

if __name__ == "__main__":
    sys.exit(main(sys.argv))
