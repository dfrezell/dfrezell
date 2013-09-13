#!/usr/bin/python

sum = 0

for n in range(1,1001):
	sum += n**n

print str(sum)[-10:]
