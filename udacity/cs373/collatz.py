#!/usr/bin/python

import sys
import numpy as np

def main(*args):
    s = np.zeros(1000000, dtype=np.uint32)
    for i in range(1, 2**31 - 1):
        n = i
        while n != 1:
            if n & 1 == 1:
                n = 3 * n + 1
            else:
                n = n >> 1
            if n < len(s) and s[n] != 0:
                s[n] += 1
                break
            elif n < len(s):
                s[n] = 1
    s.sort()
    print s

if __name__ == "__main__":
    sys.exit(main(*sys.argv))

