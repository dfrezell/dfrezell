#!/usr/bin/python

import euler

nfact = str(euler.factorial(100))
sum = 0

for c in nfact:
	sum += int(c)

print "100! digit sum =",sum
