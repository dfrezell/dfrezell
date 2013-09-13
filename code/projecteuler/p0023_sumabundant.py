#!/usr/bin/python

import euler

nums = set(xrange(1, 28124))
abun = []
sabun = set()

abun = [n for n in xrange(2, 28124) if n < sum(euler.divisor(n))]

for a in abun:
    for i in abun:
        sabun.add(a + i)

print sum(nums - sabun)
