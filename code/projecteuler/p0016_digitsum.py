#!/usr/bin/python

twopow = str(2**1000)
sum = 0

for c in twopow:
	sum += int(c)

print "2**1000 digit sum = ",sum
