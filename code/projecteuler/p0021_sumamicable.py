#!/usr/bin/python

import euler

ami = []
for n in xrange(2, 10001):
    d0 = sum(euler.divisor(n))
    d1 = sum(euler.divisor(d0))
    if (n == d1) and (n != d0):
        print d0, " and ", d1, " are amicable"
        ami.append(d1)

print "sum = ", sum(ami)
